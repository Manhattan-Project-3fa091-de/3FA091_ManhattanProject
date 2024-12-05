package de.manhattanproject;

import java.net.URI;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

public class Server {
    public static void startServer() {
        final ResourceConfig rc = new ResourceConfig().packages("de.manhattanproject.resource");
        JdkHttpServerFactory.createHttpServer(URI.create("http://127.0.0.1:8080/api"), rc);
    }

    public static void stopServer() {
        
    }
}
