package de.manhattanproject.resource;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import org.glassfish.jersey.server.ResourceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.manhattanproject.db.DatabaseConnection;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import de.manhattanproject.db.IDatabaseConnection;

@Path("v1")
public class Customer {
    de.manhattanproject.db.Customer _db;

    public Customer(IDatabaseConnection conn) {
       this._db = new de.manhattanproject.db.Customer(conn);
    }

    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public de.manhattanproject.model.Customer test() throws Exception {
        de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        customer.setFirstName("Hubert");
        return this._db.load(customer);
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
