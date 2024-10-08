package de.manhattanproject;

interface IDatabaseConnection {
    public IDatabaseConnection openConnection(Properties properties);
    public void createAllTables();
    public void truncateAllTables();
    public void removeAllTables();
    public void closeConnection();
}
