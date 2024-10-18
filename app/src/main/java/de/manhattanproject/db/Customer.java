package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;
import de.manhattanproject.model.Gender;

public class Customer implements IDatabaseInteraction<de.manhattanproject.model.Customer> {
    public Customer(DatabaseConnection db) {
        this._db = db;
    }

    @Override
    public void save(de.manhattanproject.model.Customer customer) throws Exception {
        try (PreparedStatement stmt = (this._db.getConnection()).prepareStatement("INSERT INTO customer(id, firstName, lastName, birthDate, gender) VALUES(?,?,?,?,?)")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            stmt.setString(2, customer.getFirstName());
            stmt.setString(3, customer.getLastName());
            stmt.setDate(4, Date.valueOf(customer.getBirthDate()));
            stmt.setInt(5, (customer.getGender()).ordinal());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public de.manhattanproject.model.Customer load(de.manhattanproject.model.Customer customer) {
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=? LIMIT 1"))) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            de.manhattanproject.model.Customer customerRes = new de.manhattanproject.model.Customer();
            customerRes.setId(UUID.toUUID(rs.getBytes(1)));
            customerRes.setFirstName(rs.getString(2));
            customerRes.setLastName(rs.getString(3));
            customerRes.setBirthDate((rs.getDate(4)).toLocalDate());
            customerRes.setGender(Ordinal.toEnum(Gender.class, rs.getInt(5)));
            return customerRes;
        } catch (SQLException e) {
            System.err.println("Failed to execute load customer statement: "+e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(de.manhattanproject.model.Customer customer) {
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("DELETE FROM customer WHERE id=?"))) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            stmt.executeUpdate();
            //TODO: Set all reading "customer"-references to NULL
            PreparedStatement stmtReading = (this._db.getConnection().prepareStatement("UPDATE reading SET customer_id=NULL WHERE customer_id=?"));
            stmtReading.setBytes(1, UUID.toBytes(customer.getId()));
            stmtReading.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to execute delete customer statement: "+e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DatabaseConnection _db;
}
