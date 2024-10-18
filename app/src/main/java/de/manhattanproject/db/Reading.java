package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.NoSuchElementException;
import de.manhattanproject.model.KindOfMeter;
import de.manhattanproject.model.Gender;

public class Reading implements IDatabaseInteraction<de.manhattanproject.model.Reading> {
    public Reading(DatabaseConnection db) {
        this._db = db;
    }

    @Override
    public void save(de.manhattanproject.model.Reading reading) throws SQLException, NoSuchElementException {
        if (reading.getCustomer() == null) {
            throw new NoSuchElementException("Customer not set");
        }
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
            throw e;
        }
    }

    @Override
    public de.manhattanproject.model.Reading load(de.manhattanproject.model.Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("SELECT id, comment, customer_id, dateOfReading, kindOfMeter, meterCount, meterId, substitute FROM reading WHERE id=? LIMIT 1"))) {
            stmt.setBytes(1, UUID.asBytes(reading.getId()));
            ResultSet rsReading = stmt.executeQuery();
            if (!rsReading.next()) {
                return null;
            }
            de.manhattanproject.model.Reading readingRes = new de.manhattanproject.model.Reading();
            readingRes.setId(UUID.asUUID(rsReading.getBytes(1)));
            readingRes.setComment(rsReading.getString(2));
            //readingRes.setCustomer(UUID.asUUID(rsReading.getBytes(3)));
            byte[] customerId = rsReading.getBytes(3);
            readingRes.setDateOfReading((rsReading.getDate(4)).toLocalDate());
            readingRes.setKindOfMeter(Ordinal.fromOrdinal(KindOfMeter.class, rsReading.getInt(5)));
            readingRes.setMeterCount(rsReading.getDouble(6));
            readingRes.setMeterId(rsReading.getString(7));
            readingRes.setSubstitute(rsReading.getBoolean(8));

            //Load customer from customer ID of reading
            PreparedStatement stmtCustomer = (this._db.getConnection().prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=?"));
            stmtCustomer.setBytes(1, customerId);
            ResultSet rsCustomer = stmtCustomer.executeQuery();
            if (!rsCustomer.next()) {
                return null;
            }
            de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
            customer.setId(UUID.asUUID(rsCustomer.getBytes(1)));
            customer.setFirstName(rsCustomer.getString(2));
            customer.setLastName(rsCustomer.getString(3));
            customer.setBirthDate((rsCustomer.getDate(4)).toLocalDate());
            customer.setGender(Ordinal.fromOrdinal(Gender.class, rsCustomer.getInt(5)));
            readingRes.setCustomer(customer);

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
