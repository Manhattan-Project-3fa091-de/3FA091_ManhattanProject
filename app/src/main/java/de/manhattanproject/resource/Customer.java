package de.manhattanproject.resource;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import de.manhattanproject.db.DatabaseConnection;

@Path("v1")
public class Customer {
    de.manhattanproject.db.Customer _db;

    public Customer() {
        System.out.println("Loading properties...");
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(".properties")) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("Failed to load properties file: "+e.toString());
            return;
        }

        System.out.println("Connecting to database...");
        DatabaseConnection conn = new DatabaseConnection(props);
        this._db = new de.manhattanproject.db.Customer(conn);
    }

    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public de.manhattanproject.model.Customer test() throws Exception {
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setFirstName("Hubert");
        return customer;
    }

    @POST
    @Path("customer")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateCustomer(de.manhattanproject.model.Customer customer) {
        try {
            this._db.save(customer);
        } catch (SQLException | NullPointerException e) {
            System.err.println("Failed to save customer: "+e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
