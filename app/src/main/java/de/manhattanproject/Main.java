package de.manhattanproject;

import java.util.UUID;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Test t = new Test();
        t.setId(UUID.randomUUID());
        System.out.println((t.getId()).toString());
        System.out.println(Gender.M);
        try (InputStream input = new FileInputStream(".properties")) {
            Properties prop = new Properties();
            prop.load(input);
            System.out.println(prop.getProperty("db.url"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
