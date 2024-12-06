package de.manhattanproject.db;

import java.sql.Connection;

public interface IDatabaseConnection {
    public Connection connection();
    public void createAllTables();
    public void truncateAllTables();
    public void removeAllTables();
    public void close();
}
