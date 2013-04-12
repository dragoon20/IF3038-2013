/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abraham Krisnanda
 */
public class Comment extends DBSimpleRecord{
    /*private int id_komentar;
    private Timestamp timestamp;
    private String komentar;
    private int id_user;
    private int id_task;*/
    
    private static Comment model;
    public static Comment getModel()
    {
        if (model==null)
        {
            model = new Comment();
        }
        return model;
    }
    
    @Override
    protected String GetClassName()
    {
        return "models.Comment";
    }
    
    @Override
    protected String GetTableName()
    {
        return "comment";
    }
    
    public boolean save() 
    {
        try {
            PreparedStatement statement = connection.prepareStatement
            ("INSERT INTO `"+ User.getModel().GetTableName()+"` (id_komentar, komentar, id_user, id_task) VALUES (?, ?, ?)");
            // Parameters start with 1
            statement.setInt(1, getId_komentar());
            statement.setString(2, getKomentar());
            statement.setInt(3, getId_user());
            statement.setInt(4, getId_task());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean checkValidity() 
    {
        return true;
    }
    /**
     * @return the id_komentar
     */
    public int getId_komentar() {
        return (Integer)data.get("id_komentar");
    }

    /**
     * @param id_komentar the id_komentar to set
     */
    public void setId_komentar(int id_komentar) {
        data.put("id_komentar",id_komentar);
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return ((Date)data.get("timestamp"));
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        data.put ("timestamp",timestamp);
    }

    /**
     * @return the komentar
     */
    public String getKomentar() {
        return ((String)data.get("komentar"));
    }

    /**
     * @param komentar the komentar to set
     */
    public void setKomentar(String komentar) {
        data.put("komentar",komentar);
    }

    /**
     * @return the id_user
     */
    public int getId_user() {
        return ((Integer)data.get("id_user"));
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(int id_user) {
        data.put("id_user",id_user);
    }

    /**
     * @return the id_task
     */
    public int getId_task() {
        return ((Integer)data.get("id_task"));
    }

    /**
     * @param id_task the id_task to set
     */
    public void setId_task(int id_task) {
        data.put("id_task",id_task);
    }
    
}
