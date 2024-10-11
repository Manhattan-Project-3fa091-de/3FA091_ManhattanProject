package de.manhattanproject;

import java.util.UUID;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import de.manhattanproject.db.DatabaseConnection;

public class Main {
    public static void main(String[] args) {
        Test t = new Test();
        t.setId(UUID.randomUUID());
        System.out.println((t.getId()).toString());
        System.out.println(Gender.M);

        try (InputStream input = new FileInputStream(".properties")) {
            //Load properties
            System.out.println("Loading properties...");
            Properties props = new Properties();
            props.load(input);

            //Connect to database
            DatabaseConnection conn = new DatabaseConnection();
            System.out.println("Connecting to database...");
            conn.openConnection(props);
            //System.out.println("Truncating tables...");
            //conn.truncateAllTables();
            System.out.println("Dropping tables...");
            conn.removeAllTables();
            System.out.println("Creating tables...");
            conn.createAllTables();

            System.out.println("Creating customer...");


            System.out.println("Closing database connection...");
            conn.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
