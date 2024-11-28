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
import java.util.NoSuchElementException;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.KindOfMeter;
import de.manhattanproject.model.Gender;
import de.manhattanproject.model.Reading;
import de.manhattanproject.model.Customer;

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

    public void testDateOfReading() {
        System.out.println("testDateOfReading");
        Reading reading = new Reading();
        LocalDate now = LocalDate.now();
        reading.setDateOfReading(now);
        LocalDate result = reading.getDateOfReading();
        assertEquals(now, result);
    }

    public void testPrintDateOfReading() {
        System.out.println("testPrintDateOfReading");
        Reading reading = new Reading();
        LocalDate now = LocalDate.now();
        reading.setDateOfReading(now);
        assertEquals(now.toString(), reading.printDateOfReading());
    }

    //Checks if saving a reading succeeds
    /*public void testSaveReading() {
        System.out.println("testSaveReading");

        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Hans");
        customer.setLastName("Peter");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        System.out.println(customer.getId());
        System.out.println(customer.getFirstName());

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
    }*/

    //Checks if saving a reading fails if the customer is missing
    public void testSaveReadingMissingCustomer() {
        System.out.println("testSaveReadingMissingCustomer");

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
        System.out.println("testLoadReading");

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

    //Check if loading a reading with existing customer succeeds
    /*public void testLoadReading() {
        System.out.println("testLoadReadingCustomer");

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
    }*/

    public void testDeleteReading() {
        System.out.println("testDeleteReading");

        try {
            Customer customer = new Customer();
            customer.setId(UUID.randomUUID());
            customer.setFirstName("Hans");
            customer.setLastName("Peter");
            customer.setGender(Gender.M);
            customer.setBirthDate(LocalDate.now());

            Reading reading = new Reading();
            reading.setId(UUID.randomUUID());
            reading.setComment("Test");
            reading.setCustomer(customer);
            reading.setDateOfReading(LocalDate.now());
            reading.setKindOfMeter(KindOfMeter.STROM);
            reading.setMeterCount(2.2);
            reading.setMeterId("id");
            reading.setSubstitute(true);

            de.manhattanproject.db.Reading readingDB = new de.manhattanproject.db.Reading(this._db);
            readingDB.save(reading);
            System.out.println("Reading wurde gespeichert");

            Reading newReading = readingDB.load(reading);
            System.out.println("Reading wurde geladen");

            readingDB.delete(newReading);
            System.out.println("Reading wurde gelöscht");

            Reading newReading2 = readingDB.load(reading);

            assertNotSame(newReading2, newReading);
        } catch (NoSuchElementException | SQLException e) {
            e.printStackTrace();
        }
    }

    private DatabaseConnection _db;
    private UUID _customerId;
    private UUID _readingId;
}
