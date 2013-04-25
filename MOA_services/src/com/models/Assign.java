/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.models;

import static com.models.Attachment.getTableName;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abraham Krisnanda
 */
public class Assign extends DBSimpleRecord {
    /*private int id_task;
    private String attachment;*/
    private static Assign model;
    public static Assign getModel() 
    {
        if (model==null)
        {
            model = new Assign();
        }
        return model;
    }
    
    @Override
    protected String GetClassName() 
    {
        return "com.models.Assign";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "assign";
    }
    
    public static String getTableName()
    {
    	return "assign";
    }
    
    public boolean save() 
    {
    	Connection connection = DBConnection.getConnection();
        // check same id_task and attachment
        try {
            PreparedStatement statement = connection.prepareStatement
            ("INSERT INTO `"+ getTableName()+"` (id_user, id_task) VALUES (?, ?)");
            // Parameters start with 1
            statement.setInt(1, getId_user());
            statement.setInt(2, getId_task());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean checkValidity() 
    {
        return true;
    }
    
    /**
     * @return the id_user
     */
    public int getId_user() {
        return (Integer)data.get("id_user");
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(int id_user) {
        data.put("id_user", id_user);
    }

     /**
     * @return the id_task
     */
    public int getId_task() {
        return (Integer)data.get("id_task");
    }

    /**
     * @param id_task the id_task to set
     */
    public void setId_task(int id_task) {
        data.put("id_task", id_task);
    }
    
    
}
