package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import de.manhattanproject.model.KindOfMeter;
import de.manhattanproject.model.Gender;

public class Reading implements IDatabaseInteraction<de.manhattanproject.model.Reading> {
    public Reading(DatabaseConnection db) {
        this._db = db;
    }

    @Override
    public void save(de.manhattanproject.model.Reading reading) throws SQLException, NullPointerException {
        //Don't allow saving reading without customer
        if (reading.getCustomer() == null) {
            throw new NullPointerException("Customer not set");
        }

        //Save reading
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO reading(id, comment, customer_id, dateOfReading, kindOfMeter, meterCount, meterId, substitute) VALUES(?,?,?,?,?,?,?,?)")) {
            stmt.setBytes(1, UUID.toBytes(reading.getId()));
            stmt.setString(2, reading.getComment());
            stmt.setBytes(3, UUID.toBytes((reading.getCustomer()).getId()));
            stmt.setDate(4, Date.valueOf(reading.getDateOfReading()));
            stmt.setInt(5, reading.getKindOfMeter().ordinal());
            stmt.setDouble(6, reading.getMeterCount());
            stmt.setString(7, reading.getMeterId());
            stmt.setBoolean(8, reading.getSubstitude());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    //TODO: Dynamic query filter generation depending on class attributes
    @Override
    public de.manhattanproject.model.Reading load(de.manhattanproject.model.Reading reading) {
        byte[] customerId;
        de.manhattanproject.model.Reading readingRes = new de.manhattanproject.model.Reading();

        //Load reading
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("SELECT id, comment, customer_id, dateOfReading, kindOfMeter, meterCount, meterId, substitute FROM reading WHERE id=? LIMIT 1"))) {
            stmt.setBytes(1, UUID.toBytes(reading.getId()));
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            
            readingRes.setId(UUID.toUUID(rs.getBytes(1)));
            readingRes.setComment(rs.getString(2));
            //readingRes.setCustomer(UUID.asUUID(rsReading.getBytes(3)));
            customerId = rs.getBytes(3);
            readingRes.setDateOfReading((rs.getDate(4)).toLocalDate());
            readingRes.setKindOfMeter(Ordinal.toEnum(KindOfMeter.class, rs.getInt(5)));
            readingRes.setMeterCount(rs.getDouble(6));
            readingRes.setMeterId(rs.getString(7));
            readingRes.setSubstitute(rs.getBoolean(8));
        } catch (SQLException e) {
            System.err.println("Failed to execute load reading statement: "+e.toString());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        //Load customer from customer ID of reading
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=? LIMIT 1"))) {
            stmt.setBytes(1, customerId);

            ResultSet rsCustomer = stmt.executeQuery();
            if (!rsCustomer.next()) {
                return readingRes;
            }
            de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
            customer.setId(UUID.toUUID(rsCustomer.getBytes(1)));
            customer.setFirstName(rsCustomer.getString(2));
            customer.setLastName(rsCustomer.getString(3));
            customer.setBirthDate((rsCustomer.getDate(4)).toLocalDate());
            customer.setGender(Ordinal.toEnum(Gender.class, rsCustomer.getInt(5)));
            readingRes.setCustomer(customer);

            return readingRes;
        } catch (SQLException e) {
            System.err.println("Failed to execute load customer statement: "+e.toString());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void delete(de.manhattanproject.model.Reading reading) {
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("DELETE FROM reading WHERE id=?"))) {
            stmt.setBytes(1, UUID.toBytes(reading.getId()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to execute delete reading statement: "+e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DatabaseConnection _db;
}
