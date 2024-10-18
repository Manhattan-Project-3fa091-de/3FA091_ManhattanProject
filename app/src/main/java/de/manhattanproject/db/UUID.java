package de.manhattanproject.db;

import java.nio.ByteBuffer;

public class UUID {
    public static byte[] toBytes(java.util.UUID uuid) {
        if (uuid == null) {
            return new byte[0];
        }
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public static java.util.UUID toUUID(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("Byte array must be 16 bytes long");
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new java.util.UUID(mostSigBits, leastSigBits);
    }
}