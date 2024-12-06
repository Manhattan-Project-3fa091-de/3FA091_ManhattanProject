package de.manhattanproject;

import java.net.URI;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import de.manhattanproject.resource.Resource;

public class Server {
    public static void start(Resource resource) {
        JdkHttpServerFactory.createHttpServer(URI.create("http://127.0.0.1:8080/api"), resource);
    }

    public static void stop() {
        
    }
}
