package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import de.manhattanproject.db.UUID;
import de.manhattanproject.db.Ordinal;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.KindOfMeter;

public class DatabaseConnectionTest extends TestCase {
    public static Test suite() {
        return new TestSuite(DatabaseConnectionTest.class);
    }

    public void testDatabaseConnection() {
        //Load properties
        System.out.println("Loading properties...");
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(".properties")) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load properties file: "+e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Connect to database
        DatabaseConnection db = new DatabaseConnection();
        db.openConnection(props);

        //Reinitialize tables
        db.truncateAllTables();
        db.removeAllTables();
        db.createAllTables();
    }

    public void testDatabaseConnectionInvalid() {
        //Load properties
        System.out.println("Loading properties...");
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mariadb://127.0.0.1:3306/manhattanproject");
        props.setProperty("db.user", "asdf");
        props.setProperty("db.pass", "asdf");

        //Connect to database
        DatabaseConnection db = new DatabaseConnection();
        db.openConnection(props);
    }
}
