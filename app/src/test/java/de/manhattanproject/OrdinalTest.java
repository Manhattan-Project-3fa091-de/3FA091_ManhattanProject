package de.manhattanproject;

import de.manhattanproject.model.Gender;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import de.manhattanproject.db.Ordinal;
import de.manhattanproject.model.KindOfMeter;

public class OrdinalTest extends TestCase {
    public static Test suite() {
        return new TestSuite(OrdinalTest.class);
    }

    public void testOrdinalToEnum() {
        KindOfMeter t = Ordinal.toEnum(KindOfMeter.class, 0);
        assertEquals(t, KindOfMeter.HEIZUNG);
    }

    public void testEnumToOrdinal() {
        int t = KindOfMeter.HEIZUNG.ordinal();
        assertEquals(t, 0);
    }

    public void testOrdinalNull() {
        Gender gender = Ordinal.toEnum(Gender.class, 10);
        assertEquals(gender, null);
    }
}
