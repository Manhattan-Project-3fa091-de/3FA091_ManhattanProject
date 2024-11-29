package de.manhattanproject.db;

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
        try (PreparedStatement stmt = this._db.getConnection().prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=? LIMIT 1")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { //Update if customer exists
                try (PreparedStatement stmtUpdate = this._db.getConnection().prepareStatement("UPDATE customer SET id=?, firstName=?, lastName=?, birthDate=?, gender=? WHERE id=?")) {
                    stmtUpdate.setBytes(1, UUID.toBytes(customer.getId()));
                    stmtUpdate.setString(2, customer.getFirstName());
                    stmtUpdate.setString(3, customer.getLastName());
                    stmtUpdate.setDate(4, Date.valueOf(customer.getBirthDate()));
                    stmtUpdate.setInt(5, (customer.getGender()).ordinal());
                    stmtUpdate.setBytes(6, UUID.toBytes(customer.getId()));
                    stmtUpdate.executeUpdate();
                    this._db.getConnection().commit();
                } catch (Exception e) {
                    this._db.getConnection().rollback();
                    throw e;
                }
            } else { //Insert if customer doesn't exist
                try (PreparedStatement stmtInsert = this._db.getConnection().prepareStatement("INSERT INTO customer(id, firstName, lastName, birthDate, gender) VALUES(?,?,?,?,?)")) {
                    stmtInsert.setBytes(1, UUID.toBytes(customer.getId()));
                    stmtInsert.setString(2, customer.getFirstName());
                    stmtInsert.setString(3, customer.getLastName());
                    stmtInsert.setDate(4, Date.valueOf(customer.getBirthDate()));
                    stmtInsert.setInt(5, (customer.getGender()).ordinal());
                    stmtInsert.executeUpdate();
                    this._db.getConnection().commit();
                } catch (Exception e) {
                    this._db.getConnection().rollback();
                    throw e;
                }
            }
        } catch (Exception e) {
            this._db.getConnection().rollback();
            throw e;
        }        
    }

    //TODO: Dynamic query filter generation depending on class attributes
    @Override
    public de.manhattanproject.model.Customer load(de.manhattanproject.model.Customer customer) throws Exception {
        try (PreparedStatement stmt = this._db.getConnection().prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=? LIMIT 1")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            ResultSet rs = stmt.executeQuery();
            this._db.getConnection().commit();
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
        } catch (Exception e) {
            this._db.getConnection().rollback();
            throw e;
        }
    }

    @Override
    public void delete(de.manhattanproject.model.Customer customer) throws Exception {
        try (PreparedStatement stmt = this._db.getConnection().prepareStatement("DELETE FROM customer WHERE id=?")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            stmt.executeUpdate();
            this._db.getConnection().commit();
        } catch (Exception e) {
            this._db.getConnection().rollback();
            throw e;
        }

        try (PreparedStatement stmt = this._db.getConnection().prepareStatement("UPDATE reading SET customer_id=NULL WHERE customer_id=?")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            stmt.executeUpdate();
            this._db.getConnection().commit();
        } catch (Exception e) {
            this._db.getConnection().rollback();
            throw e;
        }
    }

    private DatabaseConnection _db;
}
