package de.manhattanproject.resource;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    /*@GET
    @Path("test2")
    @Produces(MediaType.APPLICATION_JSON)
    public Response test2() {
        ObjectNode json = mapper.createObjectNode();
        json.put("result", "Jersey JSON example using Jackson 2.x");
        return Response.status(Response.Status.OK).entity(json).build();
    }

    @POST
    @Path("customer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateCustomer(de.manhattanproject.model.Customer customer) {
        ObjectNode json = mapper.createObjectNode();
        json.put("status", "ok");
        return Response.status(Response.Status.CREATED).entity(json).build();
    }*/
}
