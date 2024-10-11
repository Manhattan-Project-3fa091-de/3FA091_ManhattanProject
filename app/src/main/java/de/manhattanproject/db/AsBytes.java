package de.manhattanproject.db;

import java.nio.ByteBuffer;

class UUID {
    public static byte[] asBytes(java.util.UUID uuid) {
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }
}