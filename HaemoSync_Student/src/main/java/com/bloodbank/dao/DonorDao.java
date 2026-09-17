package com.bloodbank.dao;

import com.bloodbank.model.*;
import com.bloodbank.util.Database;
import java.sql.*;
import java.util.*;

public class DonorDao {
    public void save(Donor donor) {
        String sql = "INSERT INTO donors(id,name,phone,age,weight,group_name,last_donation) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = Database.connection().prepareStatement(sql)) {
            ps.setString(1, donor.getId()); ps.setString(2, donor.getName());
            ps.setString(3, donor.getPhone()); ps.setInt(4, donor.getAge());
            ps.setDouble(5, donor.getWeight()); ps.setString(6, donor.getBloodGroup().name());
            ps.setString(7, donor.getLastDonation() == null ? null : donor.getLastDonation().toString());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException("Could not save donor: " + e.getMessage(), e); }
    }

    public Optional<Donor> findById(String id) {
        String sql = "SELECT * FROM donors WHERE id=?";
        try (PreparedStatement ps = Database.connection().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                Donor d = new Donor(rs.getString("id"), rs.getString("name"), rs.getString("phone"),
                        rs.getInt("age"), rs.getDouble("weight"),
                        BloodGroup.valueOf(rs.getString("group_name")));
                String date = rs.getString("last_donation");
                if (date != null) d.setLastDonation(java.time.LocalDate.parse(date));
                return Optional.of(d);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public List<Donor> findAll() {
        List<Donor> list = new ArrayList<>();
        try (Statement st = Database.connection().createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM donors ORDER BY name")) {
            while (rs.next()) {
                Donor d = new Donor(rs.getString("id"), rs.getString("name"), rs.getString("phone"),
                        rs.getInt("age"), rs.getDouble("weight"),
                        BloodGroup.valueOf(rs.getString("group_name")));
                String date = rs.getString("last_donation");
                if (date != null) d.setLastDonation(java.time.LocalDate.parse(date));
                list.add(d);
            }
            return list;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void updateLastDonation(String id, java.time.LocalDate date) {
        try (PreparedStatement ps = Database.connection().prepareStatement(
                "UPDATE donors SET last_donation=? WHERE id=?")) {
            ps.setString(1, date.toString()); ps.setString(2, id); ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
