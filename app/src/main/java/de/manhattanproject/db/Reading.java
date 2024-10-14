package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Date;

public class Reading implements IDatabaseInteraction<de.manhattanproject.model.Reading> {
    public Reading(DatabaseConnection db) {
        this._db = db;
    }
    public void save(de.manhattanproject.model.Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO reading(id, comment, dateOfReading, kindOfMeter, meterCount, meterId, substitute) VALUES(?,?,?,?,?,?,?)")) {
            stmt.setBytes(1, UUID.asBytes(reading.getId()));
            stmt.setString(2, reading.getComment());
            stmt.setBytes(3, UUID.asBytes((reading.getCustomer()).getId()));
            //(reading.getCustomer()).getId();
            stmt.setDate(4, Date.valueOf(reading.getDateOfReading()));
            stmt.setInt(5, reading.getKindOfMeter().ordinal());
            stmt.setDouble(6, reading.getMeterCount());
            stmt.setString(7, reading.getMeterId());
            stmt.setBoolean(8, reading.getSubstitude());
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
