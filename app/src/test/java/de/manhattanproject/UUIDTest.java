package de.manhattanproject;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import de.manhattanproject.db.UUID;

public class UUIDTest extends TestCase {
    public static Test suite() {
        return new TestSuite(UUIDTest.class);
    }

    public void testTranscode() {
        java.util.UUID uuidOrig = java.util.UUID.randomUUID();
        System.out.println("Original UUID: "+uuidOrig);
        byte[] uuidBytes = UUID.toBytes(uuidOrig);
        java.util.UUID uuidConv = UUID.toUUID(uuidBytes);
        System.out.println("Converted UUID: "+uuidConv);
        assertEquals(uuidOrig, uuidConv);
    }
}
