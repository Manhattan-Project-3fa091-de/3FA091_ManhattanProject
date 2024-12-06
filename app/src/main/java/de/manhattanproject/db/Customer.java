package de.manhattanproject.db;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import de.manhattanproject.model.Gender;

public class Customer implements IDatabaseInteraction<de.manhattanproject.model.Customer> {
    private Connection _conn;
    
    public Customer(IDatabaseConnection conn) {
        this._conn = conn.connection();
    }

    @Override
    public void save(de.manhattanproject.model.Customer customer) throws Exception {
        HashMap<String, Object> params = new HashMap<String, Object>();
        //List<String> params = new ArrayList<>();

        if (customer.getId() != null) {
            params.put("id", customer.getId());
        }
        if (customer.getFirstName() != null) {
            params.put("firstName", customer.getFirstName());
        }
        if (customer.getLastName() != null) {
            params.put("lastName", customer.getLastName());
        }
        if (customer.getBirthDate() != null) {
            params.put("birthDate", customer.getBirthDate());
        }
        if (customer.getGender() != null) {
            params.put("gender", customer.getGender());
        }

        try (PreparedStatement stmt = this._conn.prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=? LIMIT 1")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { //Update if customer exists
                String updateQuery = "UPDATE customer SET " + params.keySet().stream().collect(Collectors.joining(", "));
                System.out.println(updateQuery);
                try (PreparedStatement stmtUpdate = this._conn.prepareStatement("UPDATE customer SET id=?, firstName=?, lastName=?, birthDate=?, gender=? WHERE id=?")) {
                    stmtUpdate.setBytes(1, UUID.toBytes(customer.getId()));
                    stmtUpdate.setString(2, customer.getFirstName());
                    stmtUpdate.setString(3, customer.getLastName());
                    stmtUpdate.setDate(4, Date.valueOf(customer.getBirthDate()));
                    stmtUpdate.setInt(5, (customer.getGender()).ordinal());
                    stmtUpdate.setBytes(6, UUID.toBytes(customer.getId()));
                    stmtUpdate.executeUpdate();
                    this._conn.commit();
                } catch (Exception e) {
                    this._conn.rollback();
                    throw e;
                }
            } else { //Insert if customer doesn't exist
                String insertQuery = "INSERT INTO customer (" + params.keySet().stream().collect(Collectors.joining(", ")) + ")" + ("?");
                try (PreparedStatement stmtInsert = this._conn.prepareStatement("INSERT INTO customer(id, firstName, lastName, birthDate, gender) VALUES(?,?,?,?,?)")) {
                    stmtInsert.setBytes(1, UUID.toBytes(customer.getId()));
                    stmtInsert.setString(2, customer.getFirstName());
                    stmtInsert.setString(3, customer.getLastName());
                    stmtInsert.setDate(4, Date.valueOf(customer.getBirthDate()));
                    stmtInsert.setInt(5, (customer.getGender()).ordinal());
                    stmtInsert.executeUpdate();
                    this._conn.commit();
                } catch (Exception e) {
                    this._conn.rollback();
                    throw e;
                }
            }
        } catch (Exception e) {
            this._conn.rollback();
            throw e;
        }        
    }

    //TODO: Dynamic query filter generation depending on class attributes
    @Override
    public de.manhattanproject.model.Customer load(de.manhattanproject.model.Customer customer) throws Exception {
        try (PreparedStatement stmt = this._conn.prepareStatement("SELECT id, firstName, lastName, birthDate, gender FROM customer WHERE id=? LIMIT 1")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            ResultSet rs = stmt.executeQuery();
            this._conn.commit();
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
            this._conn.rollback();
            throw e;
        }
    }

    @Override
    public void delete(de.manhattanproject.model.Customer customer) throws Exception {
        try (PreparedStatement stmt = this._conn.prepareStatement("DELETE FROM customer WHERE id=?")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            stmt.executeUpdate();
            this._conn.commit();
        } catch (Exception e) {
            this._conn.rollback();
            throw e;
        }

        try (PreparedStatement stmt = this._conn.prepareStatement("UPDATE reading SET customer_id=NULL WHERE customer_id=?")) {
            stmt.setBytes(1, UUID.toBytes(customer.getId()));
            stmt.executeUpdate();
            this._conn.commit();
        } catch (Exception e) {
            this._conn.rollback();
            throw e;
        }
    }
}
