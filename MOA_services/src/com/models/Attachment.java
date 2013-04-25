/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abraham Krisnanda
 */
public class Attachment extends DBSimpleRecord {
    /*private int id_task;
    private String attachment;*/
    private static Attachment model;
    public static Attachment getModel() 
    {
        if (model==null)
        {
            model = new Attachment();
        }
        return model;
    }
    
    @Override
    protected String GetClassName() 
    {
        return "com.models.Attachment";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "task_attachment";
    }
    
    public static String getTableName()
    {
    	return "task_attachment";
    }
    
    public boolean save() 
    {
    	Connection connection = DBConnection.getConnection();
        // check same id_task and attachment
        try {
            PreparedStatement statement = connection.prepareStatement
            ("INSERT INTO `"+ getTableName()+"` (id_task, attachment) VALUES (?, ?)");
            // Parameters start with 1
            statement.setInt(1, getId_task());
            statement.setString(2, getAttachment());
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

    /**
     * @return the attachment
     */
    public String getAttachment() {
        return ((String)data.get("attachment"));
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(String attachment) {
        data.put("attachment", attachment);
    }
}
