package de.manhattanproject.db;

import de.manhattanproject;

class Customer implements IDatabaseInteraction {
    Customer(DatabaseConnection conn) {
        this._conn = conn;
    }
    public void save(Customer customer) {
        try (PreparedStatement stmt = this._conn.prepareStatement("INSERT INTO customer(id, firstName, lastName, birthDate, gender) VALUES(?,?,?,?,?)")) {
            stmt.setString(1, customer.getId());
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setString(4, customer.getBirthDate());
            stmt.setString(5, customer.getGender());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete() {

    }
    private DatabaseConnection _conn;
}
