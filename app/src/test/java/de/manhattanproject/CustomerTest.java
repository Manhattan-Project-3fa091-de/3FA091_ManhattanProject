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

    //Database

    public void testSaveCustomer() {
        this._db.truncateAllTables();

        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Tom");
        customer.setLastName("Helmut");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);

        //Save customer
        System.out.println("Saving customer...");
        try {
            customerDB.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        this._db.truncateAllTables();
    }

    public void testSaveUpdateCustomer() {
        this._db.truncateAllTables();

        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Tom");
        customer.setLastName("Helmut");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);

        //Save customer
        System.out.println("Saving customer...");
        try {
            customerDB.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Update customer
        System.out.println("Updating customer...");
        try {
            customer.setFirstName("Braun");
            customerDB.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to update customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Load customer
        System.out.println("Loading customer...");
        try {
            de.manhattanproject.model.Customer loadCustomer = customerDB.load(customer);
            assertNotNull(loadCustomer);
            assertEquals(loadCustomer.getFirstName(), "Braun");
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to load customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        this._db.truncateAllTables();
    }

    public void testLoadCustomer() {
        this._db.truncateAllTables();

        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Tom");
        customer.setLastName("Helmut");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);

        //Save customer
        System.out.println("Saving customer...");
        try {
            customerDB.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Load customer
        System.out.println("Loading customer...");
        try {
            de.manhattanproject.model.Customer loadCustomer = customerDB.load(customer);
            assertNotNull(loadCustomer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to load customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        this._db.truncateAllTables();
    }

    //Check if deleting a customer succeeds 
    public void testDeleteCustomer() {
        this._db.truncateAllTables();

        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Tom");
        customer.setLastName("Helmut");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);

        //Save customer
        System.out.println("Saving customer...");
        try {
            customerDB.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        //Delete customer
        System.out.println("Deleting customer...");
        try {
            customerDB.delete(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
            assertTrue(false);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }

        this._db.truncateAllTables();
    }

    //Check if customer id reference in readings is set to NULL when a customer is deleted
    public void testDeleteCustomerReadingReference() {
        this._db.truncateAllTables();

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
            this._db.getConnection().commit();
            if (!rs.next()) {
                assertTrue(false);
            }
            byte[] customerId = rs.getBytes(1);
            assertNull(customerId);
        } catch (Exception e) {
            System.err.println("Failed to load reading: "+e.toString());
            assertTrue(false);
        }

        this._db.truncateAllTables();
    }

    // Check if loading a customer and a reading succeeds
    public void testLoadCustomerAndReading() {
        this._db.truncateAllTables();

        // Create and save a customer
        System.out.println("Creating and saving customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(this._customerId);
        customer.setFirstName("Anna");
        customer.setLastName("Müller");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.W);

        try {
            de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);
            customerDB.save(customer);
        } catch (Exception e) {
            System.err.println("Failed to save customer: " + e.toString());
            assertTrue(false);
        }

        // Load the customer
        System.out.println("Loading customer...");
        de.manhattanproject.model.Customer loadedCustomer = null;
        try {
            de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(this._db);
            loadedCustomer = customerDB.load(customer);
            assertNotNull("Loaded customer should not be null", loadedCustomer);
            assertEquals("Loaded customer should match saved customer", customer.getId(), loadedCustomer.getId());
        } catch (Exception e) {
            System.err.println("Failed to load customer: " + e.toString());
            assertTrue(false);
        }

        // Create and save a reading
        System.out.println("Creating and saving reading...");
        de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
        reading.setId(this._readingId);
        reading.setComment("Test Reading");
        reading.setCustomer(loadedCustomer); // Link to the loaded customer
        reading.setDateOfReading(LocalDate.now());
        reading.setKindOfMeter(KindOfMeter.STROM);
        reading.setMeterCount(5.5);
        reading.setMeterId("Meter123");
        reading.setSubstitute(false);

        try {
            de.manhattanproject.db.Reading readingDB = new de.manhattanproject.db.Reading(this._db);
            readingDB.save(reading);
        } catch (Exception e) {
            System.err.println("Failed to save reading: " + e.toString());
            assertTrue(false);
        }

        //Load the reading
        System.out.println("Loading reading...");
        de.manhattanproject.model.Reading loadedReading = null;
        try {
            de.manhattanproject.db.Reading readingDB = new de.manhattanproject.db.Reading(this._db);
            loadedReading = readingDB.load(reading);
            assertNotNull("Loaded reading should not be null", loadedReading);
            assertEquals("Loaded reading should match saved reading", reading.getId(), loadedReading.getId());
            assertEquals("Loaded reading should have correct customer", loadedCustomer.getId(), loadedReading.getCustomer().getId());
        } catch (Exception e) {
            System.err.println("Failed to load reading: " + e.toString());
            assertTrue(false);
        }

        this._db.truncateAllTables();
    }

    //Model

    public void testId () {
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"));
        UUID result = customer.getId();

        assertEquals(UUID.fromString("e58ed763-928c-4155-bee9-fdbaaadc15f3"), result);
    }
    
    public void testFirstName () {
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setFirstName("Michi");
        String result = customer.getFirstName();
        assertEquals("Michi", result);
    }
    
    public void testLastName () {
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setLastName("Grassl");
        String result = customer.getLastName();
        assertEquals("Grassl", result);
    }

    public void testBirthDate () {
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setBirthDate(LocalDate.of(2024, 10, 18));
        LocalDate result = customer.getBirthDate();
        assertEquals(LocalDate.of(2024, 10, 18), result);
    }

    public void testGender () {
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setGender(Gender.M);
        Gender result = customer.getGender();
        assertEquals(Gender.M, result);
    }

    private DatabaseConnection _db;
    private UUID _customerId;
    private UUID _readingId;
}
