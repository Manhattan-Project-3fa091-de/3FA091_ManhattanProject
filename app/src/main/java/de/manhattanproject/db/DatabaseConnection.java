package de.manhattanproject.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.stream.Stream;

public class DatabaseConnection implements IDatabaseConnection {
    private Connection _conn;
    private String _dbUrl;
    private String _dbUser;
    private String _dbPass;

    public DatabaseConnection(Properties props) {
        //Get properties
        this._dbUrl = props.getProperty("db.url");
        this._dbUser = props.getProperty("db.user");
        this._dbPass = props.getProperty("db.pass");
    }

    @Override
    public Connection connection() {
        //Return this object if connection already established
        if (this._conn != null) {
            return this._conn;
        }

        //Establish connection
        try {
            this._conn = DriverManager.getConnection(this._dbUrl, this._dbUser, this._dbPass);
            this._conn.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Failed to get database connection: "+e.toString());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return this._conn;
    }

    /*public Connection getConnection() throws SQLException {
        if (this._conn == null) {
            throw new SQLException("Connection is not established");
        }
        return this._conn;
    }*/

    @Override
    public void createAllTables() {
        //Execute SQL files
        try (Stream<Path> paths = Files.list(Paths.get("./sql/"))) {
            paths.forEach(path -> {
                String query;

                //Read SQL file
                try {
                    query = new String(Files.readAllBytes(path));
                } catch (IOException e) {
                    System.err.println("Failed to read SQL file: "+e.toString());
                    return;
                }

                //Execute query
                try {
                    PreparedStatement stmt = this._conn.prepareStatement(query);
                    stmt.executeUpdate();
                    this._conn.commit();
                } catch (SQLException e) {
                    System.err.println("Failed to execute SQL statement: "+e.toString());
                    try {
                        this._conn.rollback();
                    } catch (SQLException e2) {
                        System.err.println("Failed to roll back create table query: "+e2.toString());
                    }
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to get SQL file paths: "+e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void truncateAllTables() {
        try (PreparedStatement stmt = this._conn.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema=?")) {
            String catalog = this._conn.getCatalog();
            stmt.setString(1, catalog);
            ResultSet rs = stmt.executeQuery();
            this._conn.commit();
            while (rs.next()) {
                String table = rs.getString("table_name");
                PreparedStatement stmtDrop = this._conn.prepareStatement("TRUNCATE TABLE "+table);
                stmtDrop.executeUpdate();
            }
            this._conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                this._conn.rollback();
            } catch (SQLException e2) {
                System.err.println("Failed to roll back truncate table query: "+e2.toString());
            }
        }
    }

    @Override
    public void removeAllTables() {
        try (PreparedStatement stmt = this._conn.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema=?")) {
            String catalog = this._conn.getCatalog();
            stmt.setString(1, catalog);
            ResultSet rs = stmt.executeQuery();
            this._conn.commit();
            while (rs.next()) {
                String table = rs.getString("table_name");
                PreparedStatement stmtDrop = this._conn.prepareStatement("DROP TABLE "+table);
                stmtDrop.executeUpdate();
            }
            this._conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                this._conn.rollback();
            } catch (SQLException e2) {
                System.err.println("Failed to roll back create table query: "+e2.toString());
            }
        }
    }

    @Override
    public void close() {
        try {
            this._conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                this._conn.rollback();
            } catch (SQLException e2) {
                System.err.println("Failed to roll back create table query: "+e2.toString());
            }
        }
    }
}
