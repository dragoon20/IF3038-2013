/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Abraham Krisnanda
 */
/*private int id_user;
private String username;
private String email;
private String fullname;
private String avatar;
private Timestamp birthdate;
private String password;*/
public class User extends DBSimpleRecord 
{
    private static User model;    
    public static User getModel()
    {
        if (model==null)
        {
            model = new User();
        }
        return model;
    }
    
    @Override
    protected String GetClassName() 
    {
        return "models.User";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "user";
    }
    
    public boolean save() 
    {
        // check same username or email
        if (!this.data.containsKey("id_user"))
        {
            // new user
            if ((User.getModel().find("username = ?",new Object[] {this.data.get("username")}, new String[]{"string"}, null)).isEmpty())
            {
                try {
                    PreparedStatement statement = connection.prepareStatement
                    ("INSERT INTO `"+ User.getModel().GetTableName()+"` (username, email, fullname, avatar, birthdate, password) VALUES (?, ?, ?, ?, ?, ?)");
                    // Parameters start with 1
                    statement.setString(1, getUsername());
                    statement.setString(2, getEmail());
                    statement.setString(3, getFullname());
                    statement.setString(4, getAvatar());
                    statement.setString(5, getBirthdate());
                    statement.setString(6, getPassword());
                    statement.executeUpdate();
                } catch (SQLException ex) {
                	System.out.println("----------------------------------------------------------");
                	System.out.println("INSERT INTO `"+ User.getModel().GetTableName()+"` (username, email, fullname, avatar, birthdate, password) VALUES ('" + 
                            getUsername() + "','" +
                            getEmail() + "','" +
                            getFullname() + "','" +
                            getAvatar() + "','" +
                            getBirthdate() + "','" +
                            getPassword() + "')");
                	System.out.println("----------------------------------------------------------");
                    Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
                }
                return true;
            }
            else
            {   
                // username and email already used
                return false;
            }
            
            //if(User.find("username = ?",new Object[]{this.data.containsKey("username")}, new String[]{"integer"}, null)
        }
        return true;
    }
    
    public boolean checkValidity() 
    {
        return false;
    }
    
    /**
     * @return the id_user
     */
    public int getId_user() 
    {
    	return (Integer)data.get("id_user");
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(int id_user) 
    {
    	data.put("id_user", id_user);
    }

    /**
     * @return the username
     */
    public String getUsername() 
    {
    	return ((String)data.get("username"));
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) 
    {
    	data.put("username", username);
    }

    /**
     * @return the email
     */
    public String getEmail() 
    {
    	return ((String)data.get("email"));
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) 
    {
    	data.put("email", email);
    }

    /**
     * @return the fullname
     */
    public String getFullname() 
    {
    	return ((String)data.get("fullname"));
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) 
    {
    	data.put("fullname", fullname);
    }

    /**
     * @return the avatar
     */
    public String getAvatar() 
    {
    	return ((String)data.get("avatar"));
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) 
    {
    	data.put("avatar", avatar);
    }

    /**
     * @return the birthdate
     */
    public String getBirthdate() 
    {
    	return ((String)data.get("birthdate"));
    }

    /**
     * @param birthdate the birthdate to set
     */
    public void setBirthdate(Date birthdate) 
    {
    	data.put("birthdate", birthdate);
    }

    /**
     * @return the password
     */
    public String getPassword() 
    {
    	return ((String)data.get("password"));
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) 
    {
    	data.put("password", password);
    }
}
