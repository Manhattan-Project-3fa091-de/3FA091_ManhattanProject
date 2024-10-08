package de.manhattanproject;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Test t = new Test();
        t.setId(UUID.randomUUID());
        System.out.println((t.getId()).toString());
        System.out.println(Gender.M);
    }
}
