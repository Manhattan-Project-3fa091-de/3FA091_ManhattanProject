package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;

public class Reading implements IDatabaseInteraction<de.manhattanproject.model.Reading> {
    Reading(DatabaseConnection db) {
        this._db = db;
    }
    public void save(de.manhattanproject.model.Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO reading() VALUES()")) {
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void load(de.manhattanproject.model.Reading reading) {
        return;
    }
    public void delete(de.manhattanproject.model.Reading reading) {
        return;
    }
    private DatabaseConnection _db;
}
