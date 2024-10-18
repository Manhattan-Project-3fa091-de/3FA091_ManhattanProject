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
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Stream;

public class DatabaseConnection implements IDatabaseConnection {
    @Override
    public IDatabaseConnection openConnection(Properties props) {
        //Get properties
        this._dbUrl = props.getProperty("db.url");
        this._dbUser = props.getProperty("db.user");
        this._dbPass = props.getProperty("db.pass");

        //Return this object if connection already established
        if (this._conn != null) {
            return this;
        }

        //Establish connection
        try {
            this._conn = DriverManager.getConnection(this._dbUrl, this._dbUser, this._dbPass);
        } catch (SQLException e) {
            System.err.println("Failed to get database connection: "+e.toString());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return this;
    }

    public Connection getConnection() throws SQLException {
        if (this._conn == null) {
            throw new SQLException("Connection is not established");
        }
        return this._conn;
    }

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
                } catch(Exception e) {
                    e.printStackTrace();
                    return;
                }

                //Execute query
                try {
                    Statement stmt = this._conn.createStatement();
                    stmt.executeUpdate(query);
                } catch (SQLException e) {
                    System.err.println("Failed to execute SQL statement: "+e.toString());
                } catch(Exception e) {
                    e.printStackTrace();
                    return;
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
            while (rs.next()) {
                String table = rs.getString("table_name");
                PreparedStatement stmtDrop = this._conn.prepareStatement("TRUNCATE TABLE "+table);
                stmtDrop.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAllTables() {
        try (PreparedStatement stmt = this._conn.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema=?")) {
            String catalog = this._conn.getCatalog();
            stmt.setString(1, catalog);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String table = rs.getString("table_name");
                PreparedStatement stmtDrop = this._conn.prepareStatement("DROP TABLE "+table);
                stmtDrop.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void closeConnection() {
        try {
            this._conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection _conn;
    private String _dbUrl;
    private String _dbUser;
    private String _dbPass;
}
