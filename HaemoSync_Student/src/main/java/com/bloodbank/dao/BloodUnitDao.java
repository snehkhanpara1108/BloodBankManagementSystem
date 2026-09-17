package com.bloodbank.dao;

import com.bloodbank.model.*;
import com.bloodbank.util.Database;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class BloodUnitDao {
    public void save(BloodUnit u) {
        String sql = "INSERT INTO blood_units(id,group_name,component,collected_on,expires_on,donor_id,status) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement ps = Database.connection().prepareStatement(sql)) {
            ps.setString(1,u.getId()); ps.setString(2,u.getGroup().name());
            ps.setString(3,u.getComponent().name()); ps.setString(4,u.getCollectedOn().toString());
            ps.setString(5,u.getExpiresOn().toString()); ps.setString(6,u.getDonorId());
            ps.setString(7,u.getStatus().name()); ps.executeUpdate();
        } catch(SQLException e){ throw new RuntimeException("Could not save blood unit: "+e.getMessage(),e); }
    }

    public List<BloodUnit> findAvailable() {
        List<BloodUnit> list = new ArrayList<>();
        String sql="SELECT * FROM blood_units WHERE status='AVAILABLE' ORDER BY expires_on";
        try(Statement st=Database.connection().createStatement(); ResultSet rs=st.executeQuery(sql)){
            while(rs.next()) list.add(read(rs));
            return list;
        } catch(SQLException e){ throw new RuntimeException(e); }
    }

    private BloodUnit read(ResultSet rs)throws SQLException{
        BloodUnit u=new BloodUnit(rs.getString("id"),
                BloodGroup.valueOf(rs.getString("group_name")),
                ComponentType.valueOf(rs.getString("component")),
                LocalDate.parse(rs.getString("collected_on")),rs.getString("donor_id"));
        u.setStatus(BloodUnit.Status.valueOf(rs.getString("status")));
        return u;
    }

    public void updateStatus(String id, BloodUnit.Status status){
        try(PreparedStatement ps=Database.connection().prepareStatement(
                "UPDATE blood_units SET status=? WHERE id=?")){
            ps.setString(1,status.name()); ps.setString(2,id); ps.executeUpdate();
        }catch(SQLException e){throw new RuntimeException(e);}
    }
}
