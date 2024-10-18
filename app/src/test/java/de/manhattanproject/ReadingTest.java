package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.time.LocalDate;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.NoSuchElementException;
import java.util.UUID;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.KindOfMeter;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.Gender;

public class ReadingTest extends TestCase {
    public ReadingTest() {
        try (InputStream input = new FileInputStream(".properties")) {
            //Load properties
            System.out.println("Loading properties...");
            Properties props = new Properties();
            props.load(input);
            this._conn = new DatabaseConnection();
            this._conn.openConnection(props);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this._customerId = UUID.randomUUID();
        this._readingId = UUID.randomUUID();
    }

    public static Test suite() {
        return new TestSuite(ReadingTest.class);
    }

    //Checks if saving a reading succeeds
    public void testSaveReading() {
        try {
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
            reading.setId(this._customerId);
            reading.setComment("Important note");
            reading.setCustomer(customer);
            reading.setDateOfReading(LocalDate.now());
            reading.setKindOfMeter(KindOfMeter.HEIZUNG);
            reading.setMeterCount(1.2);
            reading.setMeterId("Meow");
            reading.setSubstitute(true);
            //Save reading
            System.out.println("Saving reading...");
            de.manhattanproject.db.Reading readingDB = new de.manhattanproject.db.Reading(this._conn);
            readingDB.save(reading);
            assertTrue(true);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    //Checks if saving a reading fails if the customer is missing
    public void testSaveReadingMissingCustomer() {
        try {
            //Create reading
            System.out.println("Creating reading...");
            de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
            reading.setId(this._customerId);
            reading.setComment("Important note");
            //reading.setCustomer(customer); //Missing intentionally
            reading.setDateOfReading(LocalDate.now());
            reading.setKindOfMeter(KindOfMeter.HEIZUNG);
            reading.setMeterCount(1.2);
            reading.setMeterId("Meow");
            reading.setSubstitute(true);
            //Save reading
            System.out.println("Saving reading...");
            de.manhattanproject.db.Reading readingDB = new de.manhattanproject.db.Reading(this._conn);
            readingDB.save(reading);
            assertTrue(false);
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    //Check if loading a reading succeeds
    public void testLoadReading() {
        try {
            //Create customer
            System.out.println("Creating customer...");
            de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
            customer.setId(this._customerId);

            //Create reading
            System.out.println("Creating reading...");
            de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
            reading.setId(this._customerId);
            reading.setComment("Important note");
            reading.setCustomer(customer);
            reading.setDateOfReading(LocalDate.now());
            reading.setKindOfMeter(KindOfMeter.HEIZUNG);
            reading.setMeterCount(1.2);
            reading.setMeterId("Meow");
            reading.setSubstitute(true);
            de.manhattanproject.db.Reading readingDB = new de.manhattanproject.db.Reading(this._conn);
            readingDB.save(reading);

            //Load reading
            System.out.println("Loading reading...");
            de.manhattanproject.model.Reading readingLoad = readingDB.load(reading);
            assertNotNull(readingLoad);
            assertNotNull(readingLoad.getCustomer());
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    private DatabaseConnection _conn;
    private UUID _customerId;
    private UUID _readingId;
}
