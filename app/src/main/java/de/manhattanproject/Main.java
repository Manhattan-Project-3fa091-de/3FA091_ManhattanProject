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
        DatabaseConnection conn = new DatabaseConnection();
        System.out.println("Connecting to database...");
        conn.openConnection(props);
        //System.out.println("Truncating tables...");
        //conn.truncateAllTables();
        System.out.println("Dropping tables...");
        conn.removeAllTables();
        System.out.println("Creating tables...");
        conn.createAllTables();

        //Create customer
        System.out.println("Creating customer...");
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setId(UUID.randomUUID());
        customer.setFirstName("Hans");
        customer.setLastName("Peter");
        customer.setBirthDate(LocalDate.now());
        customer.setGender(Gender.M);

        //Save customer
        System.out.println("Saving customer...");
        de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(conn);
        try {
            customerDB.save(customer);
        } catch (Exception e) {
            System.err.println("Failed to save customer: "+e.toString());
            return;
        }

        //Load customer
        System.out.println("Loading customer...");
        de.manhattanproject.model.Customer customerLoad = null;
        try {
            customerLoad = customerDB.load(customer);
            if (customerLoad == null) {
                System.out.println("No customer with ID found: "+customer.getId());
            }
        } catch (Exception e) {
            System.err.println("Failed to load customer: "+e.toString());
            return;
        }

        //Create reading
        System.out.println("Creating reading...");
        de.manhattanproject.model.Reading reading = new de.manhattanproject.model.Reading();
        reading.setId(UUID.randomUUID());
        reading.setComment("Important note");
        reading.setCustomer(customerLoad);
        reading.setDateOfReading(LocalDate.now());
        reading.setKindOfMeter(KindOfMeter.HEIZUNG);
        reading.setMeterCount(1.2);
        reading.setMeterId("Meow");
        reading.setSubstitute(true);

        //Save reading
        System.out.println("Saving reading...");
        de.manhattanproject.db.Reading readingDB = null;
        try {
            readingDB = new de.manhattanproject.db.Reading(conn);
            readingDB.save(reading);
        } catch (Exception e) {
            System.err.println("Failed to save reading: "+e.toString());
            return;
        }

        //Load reading
        System.out.println("Loading reading...");
        de.manhattanproject.model.Reading readingLoad = null;
        try {
            readingLoad = readingDB.load(reading);
            if (readingLoad == null) {
                System.out.println("No reading with ID found: "+reading.getId());
            }
        } catch (Exception e) {
            System.err.println("Failed to load reading: "+e.toString());
            return;
        }

        //Delete customer
        System.out.println("Deleting customer...");
        try {
            customerDB.delete(customerLoad);
        } catch (Exception e) {
            System.err.println("Failed to delete customer: "+e.toString());
            return;
        }

        //Delete reading
        System.out.println("Deleting reading...");
        try {
            readingDB.delete(readingLoad);
        } catch (Exception e) {
            System.err.println("Failed to delete reading: "+e.toString());
            return;
        }

        System.out.println("Closing database connection...");
        conn.closeConnection();
    }
}
