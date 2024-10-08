package de.manhattanproject;

import java.util.UUID;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Test t = new Test();
        t.setId(UUID.randomUUID());
        System.out.println((t.getId()).toString());
        System.out.println(Gender.M);

        try (InputStream input = new FileInputStream(".properties")) {
            System.out.println("Loading properties...");
            Properties props = new Properties();
            props.load(input);
            DatabaseConnection conn = new DatabaseConnection();
            System.out.println("Connecting to database...");
            conn.openConnection(props);
            System.out.println("Truncating tables...");
            conn.truncateAllTables();
            System.out.println("Creating tables...");
            conn.createAllTables();
            System.out.println("Closing database connection...");
            conn.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
