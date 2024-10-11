package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;

class Reading implements IDatabaseInteraction<Reading> {
    Reading(DatabaseConnection db) {
        this._db = db;
    }
    public void save(Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO reading() VALUES()")) {
            stmt.setString(1, reading.getId());
            stmt.setString(2, reading.getFirstName());
            stmt.setString(3, reading.getLastName());
            stmt.setString(4, reading.getBirthDate());
            stmt.setString(5, reading.getGender());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void delete() {

    }
    private DatabaseConnection _db;
}
