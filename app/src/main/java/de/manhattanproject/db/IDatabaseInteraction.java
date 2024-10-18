package de.manhattanproject.db;

interface IDatabaseInteraction<T> {
    public void save(T t) throws Exception;
    public T load(T t);
    public void delete(T t);
}
