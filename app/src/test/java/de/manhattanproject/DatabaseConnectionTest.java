/*package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import de.manhattanproject.db.DatabaseConnection;

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
} */

package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import de.manhattanproject.db.DatabaseConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DatabaseConnectionTest extends TestCase {
    public static Test suite() {
        return new TestSuite(DatabaseConnectionTest.class);
    }

    public void testDatabaseConnection() {
        // Load properties
        System.out.println("Loading properties...");
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(".properties")) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load properties file: " + e.toString());
            fail("Properties file loading failed.");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Unexpected exception occurred.");
        }

        // Connect to database
        DatabaseConnection db = new DatabaseConnection();
        db = (DatabaseConnection) db.openConnection(props);
        assertNotNull("Database connection should not be null", db);

        // Reinitialize tables
        db.truncateAllTables();
        db.removeAllTables();
        db.createAllTables();

        // Close connection
        db.closeConnection();
    }

    public void testDatabaseConnectionInvalid() {
        // Load invalid properties
        System.out.println("Loading invalid properties...");
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mariadb://127.0.0.1:3306/invalid_database");
        props.setProperty("db.user", "invalid_user");
        props.setProperty("db.pass", "invalid_pass");

        // Connect to database
        DatabaseConnection db = new DatabaseConnection();
        db = (DatabaseConnection) db.openConnection(props);

        assertNull("Database connection should be null due to invalid credentials", db);
    }

    public void testOpenConnectionAlreadyEstablished() {
        // Load properties
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mariadb://127.0.0.1:3306/manhattanproject");
        props.setProperty("db.user", "root");
        props.setProperty("db.pass", "root");

        // Connect to database
        DatabaseConnection db = new DatabaseConnection();
        db = (DatabaseConnection) db.openConnection(props);
        assertNotNull("Database connection should not be null", db);

        // Attempt to open connection again
        DatabaseConnection dbSecond = (DatabaseConnection) db.openConnection(props);
        assertEquals("Database connection should be the same instance", db, dbSecond);

        // Close connection
        db.closeConnection();
    }

    public void testCreateAllTablesIOException() {
        // Load properties
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mariadb://127.0.0.1:3306/manhattanproject");
        props.setProperty("db.user", "root");
        props.setProperty("db.pass", "root");

        // Connect to database
        DatabaseConnection db = new DatabaseConnection();
        db = (DatabaseConnection) db.openConnection(props);
        assertNotNull("Database connection should not be null", db);

        // Rename the SQL directory to simulate IOException
        try {
            Path sqlDir = Paths.get("./sql/");
            if (Files.exists(sqlDir)) {
                Files.move(sqlDir, Paths.get("./sql_backup/"));
            }

            db.createAllTables();
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException occurred during test setup.");
        } finally {
            // Restore the SQL directory
            try {
                Path sqlBackupDir = Paths.get("./sql_backup/");
                if (Files.exists(sqlBackupDir)) {
                    Files.move(sqlBackupDir, Paths.get("./sql/"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                fail("Failed to restore SQL directory.");
            }
            db.closeConnection();
        }
    }

    public void testGetConnectionNotEstablished() {
        DatabaseConnection db = new DatabaseConnection();
        try {
            Connection conn = db.getConnection();
            fail("Expected SQLException to be thrown due to unestablished connection.");
        } catch (SQLException e) {
            // Expected exception
            assertEquals("Connection is not established", e.getMessage());
        }
    }

    public void testCloseConnectionExceptionHandling() {
        // Load properties
        Properties props = new Properties();
        props.setProperty("db.url", "jdbc:mariadb://127.0.0.1:3306/manhattanproject");
        props.setProperty("db.user", "root");
        props.setProperty("db.pass", "root");

        // Connect to database
        DatabaseConnection db = new DatabaseConnection();
        db = (DatabaseConnection) db.openConnection(props);
        assertNotNull("Database connection should not be null", db);

        // Simulate exception during close
        try {
            db.getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to close the connection manually.");
        }

        // Now call closeConnection, which should handle the already closed connection
        try {
            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            fail("closeConnection should handle exceptions internally.");
        }
    }
}

