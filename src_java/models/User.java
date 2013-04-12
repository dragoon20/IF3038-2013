/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;

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
    protected  String GetClassName() 
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
        return true;
    }
    
    public boolean checkValidity() 
    {
        return true;
    }
    
    /**
     * @return the id_user
     */
    public int getId_user() 
    {
    	return (int)data.get("id_user");
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
    public Date getBirthdate() 
    {
    	return ((Date)data.get("birthdate"));
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
