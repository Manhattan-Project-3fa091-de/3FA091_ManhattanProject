package de.manhattanproject.resource;

import org.glassfish.jersey.server.ResourceConfig;

public class Resource extends ResourceConfig {
    public Resource() {
        packages("de.manhattanproject.resource");
        register(org.glassfish.jersey.jackson.JacksonFeature.class);
    }
}
