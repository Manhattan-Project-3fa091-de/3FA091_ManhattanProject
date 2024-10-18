package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;
import java.util.UUID;
import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.model.Gender;
import de.manhattanproject.model.KindOfMeter;

public class CustomerTest extends TestCase {
    public CustomerTest() {
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
        return new TestSuite(CustomerTest.class);
    }

    //Check if deleting a customer succeeds 
    public void testDeleteCustomer() {
        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Tom");
        customer.setLastName("Helmut");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        //Save customer
        System.out.println("Saving customer...");
        try {
            de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);
            customerDB.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    //Check if customer id reference in readings is set to NULL when a customer is deleted
    public void testDeleteCustomerReadingReference() {
        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Helmut");
        customer.setLastName("Kohl");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        //Save customer
        System.out.println("Saving customer...");
        try {
            de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);
            customerDB.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Create reading
        System.out.println("Creating reading...");
        de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
        reading.setId(this._readingId);
        reading.setComment("Grrr");
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

        //Delete customer
        try {
            de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);
            customerDB = new de.manhattanproject.db.Customer(this._db);
            customerDB.delete(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save reading: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Get all readings by customer id using previously created customer
        System.out.println("Loading reading...");
        try {
            PreparedStatement stmt = this._db.getConnection().prepareStatement("SELECT customer_id FROM reading WHERE id=?");
            stmt.setBytes(1, de.manhattanproject.db.UUID.toBytes(this._readingId));
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                assertTrue(false);
            }
            byte[] customerId = rs.getBytes(1);
            assertNull(customerId);
        } catch (Exception e) {
            System.err.println("Failed to load reading: "+e.toString());
            assertTrue(false);
        }
    }

    private DatabaseConnection _db;
    private UUID _customerId;
    private UUID _readingId;
}
