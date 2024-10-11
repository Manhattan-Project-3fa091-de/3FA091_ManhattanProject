package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.UUID;
import de.manhattanproject.db.IDatabaseConnection;

class Customer implements IDatabaseInteraction<de.manhattanproject.Customer> {
    Customer(DatabaseConnection db) {
        this._db = db;
    }
    public void save(de.manhattanproject.Customer customer) {
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO customer(id, firstName, lastName, birthDate, gender) VALUES(?,?,?,?,?)")) {
            stmt.setBytes(1, UUID.asBytes(customer.getId()));
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setTimestamp(4, Timestamp.valueOf(customer.getBirthDate()));
            stmt.setString(5, customer.getGender());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete() {

    }
    private DatabaseConnection _db;
}
