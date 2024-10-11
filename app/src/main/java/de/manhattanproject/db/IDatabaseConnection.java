package de.manhattanproject.db;

import java.util.Properties;

interface IDatabaseConnection {
    public IDatabaseConnection openConnection(Properties properties);
    public void createAllTables();
    public void truncateAllTables();
    public void removeAllTables();
    public void closeConnection();
}
