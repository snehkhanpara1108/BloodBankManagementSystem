package com.bloodbank.dao;

import com.bloodbank.model.BloodRequest;
import com.bloodbank.util.Database;
import java.sql.*;
import java.util.*;

public class RequestDao {
    public void save(BloodRequest r) {
        String sql="INSERT INTO requests(id,patient,hospital,group_name,component,units,priority,created_at,status) VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps=Database.connection().prepareStatement(sql)){
            ps.setString(1,r.getId()); ps.setString(2,r.getPatient()); ps.setString(3,r.getHospital());
            ps.setString(4,r.getGroup().name()); ps.setString(5,r.getComponent().name());
            ps.setInt(6,r.getUnits()); ps.setString(7,r.getPriority().name());
            ps.setString(8,r.getCreatedAt().toString()); ps.setString(9,r.getStatus()); ps.executeUpdate();
        }catch(SQLException e){throw new RuntimeException("Could not save request: "+e.getMessage(),e);}
    }

    public List<String> recent() {
        List<String> rows=new ArrayList<>();
        try(Statement st=Database.connection().createStatement();
            ResultSet rs=st.executeQuery("SELECT id,patient,hospital,group_name,component,units,priority,status FROM requests ORDER BY created_at DESC")){
            while(rs.next()) rows.add(String.format("%s | %s | %s | %s | %s | %d | %s | %s",
                    rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),
                    rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8)));
            return rows;
        }catch(SQLException e){throw new RuntimeException(e);}
    }

    public void updateStatus(String id,String status){
        try(PreparedStatement ps=Database.connection().prepareStatement("UPDATE requests SET status=? WHERE id=?")){
            ps.setString(1,status);ps.setString(2,id);ps.executeUpdate();
        }catch(SQLException e){throw new RuntimeException(e);}
    }
}
