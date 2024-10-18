package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.time.LocalDate;
import java.sql.SQLException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.KindOfMeter;
import de.manhattanproject.model.Gender;

public class ReadingTest extends TestCase {
    public ReadingTest() {
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

        //Generate UUIDs
        this._customerId = UUID.randomUUID();
        this._readingId = UUID.randomUUID();

        //Connect to database
        this._db = new DatabaseConnection();
        this._db.openConnection(props);

        //Reinitialize tables
        this._db.removeAllTables();
        this._db.createAllTables();
    }

    public static Test suite() {
        return new TestSuite(ReadingTest.class);
    }

    //Checks if saving a reading succeeds
    public void testSaveReading() {
        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Hans");
        customer.setLastName("Peter");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        //Create reading
        System.out.println("Creating reading...");
        de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
        reading.setId(this._readingId);
        reading.setComment("Important note");
        reading.setCustomer(customer);
        reading.setDateOfReading(LocalDate.now());
        reading.setKindOfMeter(KindOfMeter.HEIZUNG);
        reading.setMeterCount(1.2);
        reading.setMeterId("Meow");
        reading.setSubstitute(true);

        //Save reading
        System.out.println("Saving reading...");
        try {
            de.manhattanproject.db.Reading readingDB = new de.manhattanproject.db.Reading(this._db);
            readingDB.save(reading);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save reading: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Load customer
        System.out.println("Loading customer...");
        try {
            de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);
            de.manhattanproject.model.Customer customerLoad = customerDB.load(customer);
            assertNotNull(customerLoad);
        } catch (Exception e) {
            System.err.println("Failed to load customer: "+e.toString());
            assertTrue(false);
        }
    }

    //Checks if saving a reading fails if the customer is missing
    public void testSaveReadingMissingCustomer() {
        //Create reading
        System.out.println("Creating reading...");
        de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
        reading.setId(this._readingId);
        reading.setComment("Important note");
        //reading.setCustomer(customer); //Missing intentionally
        reading.setDateOfReading(LocalDate.now());
        reading.setKindOfMeter(KindOfMeter.HEIZUNG);
        reading.setMeterCount(1.2);
        reading.setMeterId("Meow");
        reading.setSubstitute(true);

        //Save reading
        System.out.println("Saving reading...");
        de.manhattanproject.db.Reading readingDB = null;
        try {
            readingDB = new de.manhattanproject.db.Reading(this._db);
            readingDB.save(reading);
            assertTrue(false);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save reading: "+e.toString());
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    //Check if loading a reading succeeds
    public void testLoadReading() {
        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Lisa");
        customer.setLastName("Braun");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.W);

        //Create reading
        System.out.println("Creating reading...");
        de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
        reading.setId(this._readingId);
        reading.setComment("Important note");
        reading.setCustomer(customer);
        reading.setDateOfReading(LocalDate.now());
        reading.setKindOfMeter(KindOfMeter.HEIZUNG);
        reading.setMeterCount(1.2);
        reading.setMeterId("Meow");
        reading.setSubstitute(true);

        //Save reading
        de.manhattanproject.db.Reading readingDB = null;
        try {
            readingDB = new de.manhattanproject.db.Reading(this._db);
            readingDB.save(reading);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save reading: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Load reading
        System.out.println("Loading reading...");
        try {
            de.manhattanproject.model.Reading readingLoad = readingDB.load(reading);
            assertNotNull(readingLoad);
        } catch (Exception e) {
            System.err.println("Failed to load reading: "+e.toString());
            assertTrue(false);
        }
    }

    private DatabaseConnection _db;
    private UUID _customerId;
    private UUID _readingId;
}
