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
        //Properties
        this._dbUrl = props.getProperty("db.url");
        this._dbUser = props.getProperty("db.user");
        this._dbPass = props.getProperty("db.pass");

        //DB
        try {
            if (this._conn.isValid(1000)) {
                return this;
            }
            this._conn = DriverManager.getConnection(this._dbUrl, this._dbUser, this._dbPass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return this;
    }

    public Connection getConnection() {
        return this._conn;
    }

    @Override
    public void createAllTables() {
        try {
            try (Stream<Path> paths = Files.list(Paths.get("./sql/"))) {
                paths.forEach(path -> {
                    try {
                        String query = new String(Files.readAllBytes(path));
                        //System.out.println(query);
                        Statement stmt = this._conn.createStatement();
                        stmt.executeUpdate(query);
                    //stmt.execute(query);
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                });
                //String query = new String(Files.readAllBytes());
                //System.out.println(query);
                //stmt.execute(query);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void truncateAllTables() {
        //Statement stmt = this._conn.createStatement();
        //stmt.execute("SET FOREIGN_KEY_CHECKS=0;");
        try (PreparedStatement stmt = this._conn.prepareStatement("SELECT table_name FROM information_schema.tables WHERE table_schema=?")) {
            String catalog = this._conn.getCatalog();
            stmt.setString(1, catalog);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String table = rs.getString("table_name");
                System.out.println(table);
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
                System.out.println(table);
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
    String _dbUrl;
    String _dbUser;
    String _dbPass;
}
