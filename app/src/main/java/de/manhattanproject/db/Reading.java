package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import de.manhattanproject.model.KindOfMeter;

public class Reading implements IDatabaseInteraction<de.manhattanproject.model.Reading> {
    public Reading(DatabaseConnection db) {
        this._db = db;
    }
    @Override
    public void save(de.manhattanproject.model.Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO reading(id, comment, customer_id, dateOfReading, kindOfMeter, meterCount, meterId, substitute) VALUES(?,?,?,?,?,?,?,?)")) {
            stmt.setBytes(1, UUID.asBytes(reading.getId()));
            stmt.setString(2, reading.getComment());
            stmt.setBytes(3, UUID.asBytes((reading.getCustomer()).getId()));
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
    @Override
    public de.manhattanproject.model.Reading load(de.manhattanproject.model.Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("SELECT id, comment, customer_id, dateOfReading, kindOfMeter, meterCount, meterId, substitute FROM reading WHERE id=? LIMIT 1"))) {
            stmt.setBytes(1, UUID.asBytes(reading.getId()));
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No results");
                return new de.manhattanproject.model.Reading();
            }
            de.manhattanproject.model.Reading readingRes = new de.manhattanproject.model.Reading();
            readingRes.setId(UUID.asUUID(rs.getBytes(1)));
            readingRes.setComment(rs.getString(2));
            //readingRes.setCustomer(UUID.asUUID(rs.getBytes(3)));
            readingRes.setDateOfReading((rs.getDate(4)).toLocalDate());
            readingRes.setKindOfMeter(Ordinal.fromOrdinal(KindOfMeter.class, rs.getInt(5)));
            readingRes.setMeterCount(rs.getDouble(6));
            readingRes.setMeterId(rs.getString(7));
            readingRes.setSubstitute(rs.getBoolean(8));
            return readingRes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new de.manhattanproject.model.Reading();
    }
    @Override
    public void delete(de.manhattanproject.model.Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("DELETE FROM reading WHERE id=?"))) {
            stmt.setBytes(1, UUID.asBytes(reading.getId()));
            int rows = stmt.executeUpdate();
            System.out.println(rows+" affected");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private DatabaseConnection _db;
}
