package de.manhattanproject.db;

public class Ordinal {
    public static <E extends Enum<E>> E fromOrdinal(Class <E> enumClass, int ordinal) {
        E[] enumConstants = enumClass.getEnumConstants();

        if (ordinal >= 0 && ordinal < enumConstants.length) {
            return enumConstants[ordinal];
        } else {
            return null;
        }
    }
}