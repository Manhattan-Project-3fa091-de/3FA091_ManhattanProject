package de.manhattanproject.db;

interface IDatabaseInteraction<T> {
    public void save(T t);
    public void load(T t);
    public void delete(T t);
}
