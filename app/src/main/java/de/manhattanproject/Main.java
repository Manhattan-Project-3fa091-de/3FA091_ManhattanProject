package de.manhattanproject;

import java.util.UUID;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Test t = new Test();
        t.setId(UUID.randomUUID());
        System.out.println((t.getId()).toString());
        System.out.println(Gender.M);

        //Properties
        String dbUrl = "";
        String dbUser = "";
        String dbPass = "";
        try (InputStream input = new FileInputStream(".properties")) {
            Properties prop = new Properties();
            prop.load(input);
            dbUrl = prop.getProperty("db.url");
            dbUser = prop.getProperty("db.user");
            dbPass = prop.getProperty("db.pass");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //DB
        try {
            Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
