/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abraham Krisnanda
 */
public class Category extends DBSimpleRecord{
    /*private int id_kategori;
    private String nama_kategori;
    private int id_user;*/
    /**
     * @return the id_kategori
     */
    private static Category model;
    public static Category getModel()
    {
        if (model==null)
        {
            model = new Category();
        }
        return model;
    }
    
    @Override
    protected String GetClassName() 
    {
        return "com.models.Category";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "kategori";
    }
    
    public static String getTableName() 
    {
    	return "kategori";
    }
    
    public boolean save() 
    {
    	Connection connection = DBConnection.getConnection();
        if(!this.data.containsKey("id_kategori"))
        {
            try {
                // new category
                PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO `"+ Category.getModel().GetTableName()+"` (nama_kategori, id_user) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                // Parameters start with 1
                statement.setString(1, getNama_kategori());
                statement.setInt(2, getId_user());
                
                statement.executeUpdate();

                ResultSet gen = statement.getGeneratedKeys();
                gen.next();
                setId_kategori(gen.getInt(1));
            } catch (SQLException ex) {
                Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
        else 
        {
            try {
                // new category
                PreparedStatement statement = connection.prepareStatement
                ("UPDATE `"+ Category.getModel().GetTableName()+"` SET nama_kategori = ? WHERE id_kategori = ?", Statement.RETURN_GENERATED_KEYS);
                // Parameters start with 1
                statement.setString(1, getNama_kategori());
                statement.setInt(2, getId_kategori());
                
                statement.executeUpdate();

                ResultSet gen = statement.getGeneratedKeys();
                gen.next();
                setId_kategori(gen.getInt(1));
            } catch (SQLException ex) {
                Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
            }
            return true;
        }
    }
    
    public boolean checkValidity() 
    {
        return false;
    }
    
    public boolean getEditable(int id_user)
    {
    	return ((!User.getModel().find("id_user IN (SELECT id_user FROM edit_kategori WHERE id_kategori = ? AND id_user = ? )", 
    						new Object[]{getId_kategori(), id_user}, new String[]{"integer", "integer"}, null).isEmpty()) || (getDeletable(id_user)));
    }
    
    public boolean getDeletable(int id_user)
    {
    	return (getId_user() == id_user);
    }
    
    public int getId_kategori() {
        return (Integer)data.get("id_kategori");
    }

    /**
     * @param id_kategori the id_kategori to set
     */
    public void setId_kategori(int id_kategori) {
        data.put("id_kategori",id_kategori);
    }

    /**
     * @return the nama_kategori
     */
    public String getNama_kategori() {
        return ((String)data.get("nama_kategori"));
    }

    /**
     * @param nama_kategori the nama_kategori to set
     */
    public void setNama_kategori(String nama_kategori) {
        data.put("nama_kategori",nama_kategori);
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
}
