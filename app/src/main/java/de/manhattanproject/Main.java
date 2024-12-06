package de.manhattanproject;

import java.util.UUID;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.Gender;
import de.manhattanproject.model.KindOfMeter;

public class Main {
    public static void main(String[] args) {
        //Load properties
        System.out.println("Loading properties...");
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(".properties")) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load properties file: "+e.toString());
            return;
        }

        //Connect to database
        DatabaseConnection conn = new DatabaseConnection(props);
        System.out.println("Connecting to database...");
        conn.connection();
        //System.out.println("Truncating tables...");
        //conn.truncateAllTables();
        System.out.println("Dropping tables...");
        conn.removeAllTables();
        System.out.println("Creating tables...");
        conn.createAllTables();

        //Start server
        Server.start(conn);
    }
}
