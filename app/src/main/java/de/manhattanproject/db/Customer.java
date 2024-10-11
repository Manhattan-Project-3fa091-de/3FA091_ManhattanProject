package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;

public class Customer implements IDatabaseInteraction<de.manhattanproject.model.Customer> {
    public Customer(DatabaseConnection db) {
        this._db = db;
    }
    @Override
    public void save(de.manhattanproject.model.Customer customer) {
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO customer(id, firstName, lastName, birthDate, gender) VALUES(?,?,?,?,?)")) {
            stmt.setBytes(1, UUID.asBytes(customer.getId()));
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setDate(4, Date.valueOf(customer.getBirthDate()));
            stmt.setInt(5, (customer.getGender()).ordinal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void load(de.manhattanproject.model.Customer customer) {
        return;
    }
    @Override
    public void delete(de.manhattanproject.model.Customer customer) {
        return;
    }
    private DatabaseConnection _db;
}
