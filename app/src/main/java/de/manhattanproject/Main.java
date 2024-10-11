package de.manhattanproject;

import java.util.UUID;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import de.manhattanproject.db.DatabaseConnection;
//import de.manhattanproject.Customer;
//import de.manhattanproject.db.Customer;
import de.manhattanproject.model.Gender;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        try (InputStream input = new FileInputStream(".properties")) {
            //Load properties
            System.out.println("Loading properties...");
            Properties props = new Properties();
            props.load(input);

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

            System.out.println("Creating customer...");
            de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
            customer.setId(UUID.randomUUID());
            customer.setFirstName("Hans");
            customer.setLastName("Peter");
            customer.setBirthDate(LocalDate.now());
            customer.setGender(Gender.M);
            de.manhattanproject.db.Customer customerDB = new de.manhattanproject.db.Customer(conn);
            customerDB.save(customer);

            System.out.println("Closing database connection...");
            conn.closeConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
