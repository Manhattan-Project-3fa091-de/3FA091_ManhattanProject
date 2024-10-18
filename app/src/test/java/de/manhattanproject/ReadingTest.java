package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.KindOfMeter;
import java.time.LocalDate;
import java.util.UUID;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import de.manhattanproject.db.DatabaseConnection;
import java.util.NoSuchElementException;

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
    }

    public static Test suite() {
        return new TestSuite(ReadingTest.class);
    }

    public void testCreateReading() {
        try {
            //Create reading
            System.out.println("Creating reading...");
            de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
            reading.setId(UUID.randomUUID());
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

    private DatabaseConnection _conn;
}
