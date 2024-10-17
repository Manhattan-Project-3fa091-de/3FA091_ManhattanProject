package de.manhattanproject.db;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.ResultSet;
import de.manhattanproject.model.Gender;

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
    public de.manhattanproject.model.Customer load(de.manhattanproject.model.Customer customer) {
        //de.manhattanproject.model.Customer customer = new de.manhattanproject.model.Customer();
        try (PreparedStatement stmt = (this._db.getConnection().prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=? LIMIT 1"))) {
            stmt.setBytes(1, UUID.asBytes(customer.getId()));
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                System.out.println("No results");
                return null;
            }
            de.manhattanproject.model.Customer customerRes = new de.manhattanproject.model.Customer();
            customerRes.setId(UUID.asUUID(rs.getBytes(1)));
            customerRes.setFirstName(rs.getString(2));
            customerRes.setLastName(rs.getString(3));
            customerRes.setBirthDate((rs.getDate(4)).toLocalDate());
            customerRes.setGender(Ordinal.fromOrdinal(Gender.class, rs.getInt(5)));
            return customerRes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void delete(de.manhattanproject.model.Customer customer) {
        return;
    }
    private DatabaseConnection _db;
}
