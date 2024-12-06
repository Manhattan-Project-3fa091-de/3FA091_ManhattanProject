package de.manhattanproject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

import de.manhattanproject.db.DatabaseConnection;
import de.manhattanproject.db.IDatabaseConnection;
import de.manhattanproject.resource.Customer;
import de.manhattanproject.resource.Resource;

import org.glassfish.jersey.jackson.JacksonFeature;

public class Server {
    public static void start(IDatabaseConnection conn) {
        //Customer rs = new Customer(conn);
        Resource r = new Resource();
        JdkHttpServerFactory.createHttpServer(URI.create("http://127.0.0.1:8080/api"), r);
    }

    public static void stop() {
        
    }
}
