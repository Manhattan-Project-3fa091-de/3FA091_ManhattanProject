package de.manhattanproject;

import java.util.UUID;

public class Test implements IID {
    public UUID getId() {
        return _id;
    }
    public void setId(UUID id) {
        _id = id;
    }
    private UUID _id;
}