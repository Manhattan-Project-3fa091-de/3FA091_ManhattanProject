package de.manhattanproject.db;

import java.sql.SQLException;
import java.util.NoSuchElementException;

interface IDatabaseInteraction<T> {
    public void save(T t) throws SQLException, NoSuchElementException;
    public T load(T t);
    public void delete(T t);
}
