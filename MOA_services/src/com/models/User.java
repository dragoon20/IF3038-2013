/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
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
        return "com.models.User";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "user";
    }
    
    public static String getTableName() 
    {
    	return "user";
    }
    
    public boolean save() 
    {
        if (!this.data.containsKey("id_user"))
        {
            // new user
        	if (User.getModel().find("username = ? or email = ?", new Object[]{getUsername(), getEmail()}, new String[]{"string", "string"}, null).isEmpty())
            {
                try {
                	Connection connection = DBConnection.getConnection();
                    PreparedStatement statement = connection.prepareStatement
                    ("INSERT INTO `"+ User.getModel().GetTableName()+"` (username, email, fullname, avatar, birthdate, password) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    // Parameters start with 1
                    statement.setString(1, getUsername());
                    statement.setString(2, getEmail());
                    statement.setString(3, getFullname());
                    statement.setString(4, getAvatar());
                    statement.setDate(5, getBirthdate());
                    statement.setString(6, getPassword());
                    statement.executeUpdate();
                    
                    ResultSet gen = statement.getGeneratedKeys();
                    if (gen.next())
                    {
                    	setId_user(gen.getInt(1));
                    }
                    return true;
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
                    return false;
                }
            }
            else
            {   
                // username and email already used
                return false;
            }            
        }
        else
        {
                System.out.println(">>>test update");
        	List<DBSimpleRecord> list = Arrays.asList(User.getModel().findAll("username = ? or email = ?", new Object[]{getUsername(), getEmail()}, new String[]{"string", "string"}, null));
        	if (list.size() > 1)
        	{
        		return false;
        	}
        	else
        	{
        		try {
        			Connection connection = DBConnection.getConnection();
        			PreparedStatement statement = connection.prepareStatement
                        ("UPDATE `"+ User.getModel().GetTableName()+"` SET username = ?, email = ?, fullname = ?, avatar = ?, birthdate = ?, password = ? WHERE id_user = ?");
	                // Parameters start with 1
	                statement.setString(1, getUsername());
	                statement.setString(2, getEmail());
	                statement.setString(3, getFullname());
	                statement.setString(4, getAvatar());
	                statement.setDate(5, getBirthdate());
	                statement.setString(6, getPassword());
	                statement.setInt(7, getId_user());
	                statement.executeUpdate();
	                return true;
        		} catch (SQLException ex) {
        			ex.printStackTrace();
        			return false;
        		}
        	}
        }
    }
    
    public boolean checkValidity() 
    {
    	boolean status = false;
    	/*if (!getUsername().matches(".{5,}"))
    	{
    		status = true;
    	}
    	if ((!getPassword().matches(".{8,}")) || (data.get("confirm_password")!=getPassword()) || 
    			(getPassword()==getEmail()) || (getPassword()==getUsername()))
    	{
    		status = true;
    	}
    	if (!getFullname().matches(".+ .+"))
    	{
    		status = true;
    	}
		if (!((DBSimpleRecord.sdf.format(getBirthdate()).matches("[1-9][0-9][0-9][0-9]-[0-1][0-9]-[0-3][0-9]")) && (getBirthdate().getYear()>=1955)))
		{
			status = true;
		}
		if (!getEmail().matches(".+@.+\\...+"))
		{
			status = true;
		}
		if (!getAvatar().matches(".+\\.(jpe?g|JPE?G)"))
		{
			status = true;
		}*/
		return status;
    }
    
    public void hashPassword()
    {
    	setPassword(DBSimpleRecord.MD5(getPassword()));
    }
    
    public Task[] getCreatedTasks()
    {
    	List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("id_user=?", new Object[]{getId_user()}, new String[]{"integer"}, null));
    	return list.toArray(new Task[list.size()]);
    }
    
    public Task[] getAssignedTasks()
    {
    	List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("id_task IN (SELECT id_task FROM assign WHERE id_user=?) OR id_user=?", new Object[]{getId_user(), getId_user()}, new String[]{"integer", "integer"}, null));
    	return list.toArray(new Task[list.size()]);
    }
        
    public Category[] getCategories()
    {
    	List<DBSimpleRecord> list = Arrays.asList(Category.getModel().findAll(
			"(id_user=? OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user=?) "+
			"OR id_kategori IN (SELECT id_kategori FROM "+Task.getTableName()+" AS t LEFT OUTER JOIN assign AS a "+
			"ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ? ))", 
			new Object[]{getId_user(), getId_user(), getId_user(), getId_user()}, 
			new String[]{"integer", "integer", "integer", "integer"}, null));
		return list.toArray(new Category[list.size()]);
    }
    
    public Task[] getTasks()
    {
    	List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("(id_kategori IN ( SELECT id_kategori FROM "+Category.getTableName()+
    			" WHERE id_user=? OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user=?) "+
    			"OR id_kategori IN (SELECT id_kategori FROM "+ Task.getTableName() +" AS t LEFT OUTER JOIN assign AS a "+
    			"ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ? )))", 
    			new Object[]{getId_user(), getId_user(), getId_user(), getId_user()}, 
    			new String[]{"integer", "integer", "integer", "integer"}, null));
    	return list.toArray(new Task[list.size()]);
    }
    
    public Task[] getTasks(int status, int category_id)
    {
    	List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("(id_kategori IN ( SELECT id_kategori FROM "+Category.getTableName()+
    			" WHERE id_user=? OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user=?) "+
    			"OR id_kategori IN (SELECT id_kategori FROM "+ Task.getTableName() +" AS t LEFT OUTER JOIN assign AS a "+
    			"ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ? ))) AND status = ? AND (id_kategori = ? OR 0=?)", 
    			new Object[]{getId_user(), getId_user(), getId_user(), getId_user(), status, category_id, category_id}, 
    			new String[]{"integer", "integer", "integer", "integer", "integer", "integer", "integer"}, null));
    	return list.toArray(new Task[list.size()]);
    }
    
    public Task[] getTasksLike(String q)
    {
    	q = "%"+q.replaceAll(" ", "%")+"%";
    	
    	List<DBSimpleRecord> list = Arrays.asList(Task.getModel().findAll("(id_kategori IN ( SELECT id_kategori FROM "+Category.getTableName()+
    			" WHERE id_user=? OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user=?) "+
    			"OR id_kategori IN (SELECT id_kategori FROM "+ Task.getTableName() +" AS t LEFT OUTER JOIN assign AS a "+
    			"ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ? ))) AND nama_task LIKE ?", 
    			new Object[]{getId_user(), getId_user(), getId_user(), getId_user(), q}, 
    			new String[]{"integer", "integer", "integer", "integer", "string"}, null));
    	return list.toArray(new Task[list.size()]);
    }
    
    public Category[] getCategoriesLike(String q)
    {
    	q = "%"+q.replaceAll(" ", "%")+"%";
    	
    	List<DBSimpleRecord> list = Arrays.asList(Category.getModel().findAll(
    			"(id_user=? OR id_kategori IN (SELECT id_kategori FROM edit_kategori WHERE id_user=?) "+
    			"OR id_kategori IN (SELECT id_kategori FROM "+Task.getTableName()+" AS t LEFT OUTER JOIN assign AS a "+
    			"ON t.id_task=a.id_task WHERE t.id_user = ? OR a.id_user = ? )) AND nama_kategori LIKE ?", 
    			new Object[]{getId_user(), getId_user(), getId_user(), getId_user(), q}, 
    			new String[]{"integer", "integer", "integer", "integer", "string"}, null));
    	return list.toArray(new Category[list.size()]);
    }
    
    public User[] findAllLike(String q)
    {
    	q = "%"+q.replaceAll(" ", "%")+"%";
    	
    	List<DBSimpleRecord> list = Arrays.asList(User.getModel().findAll("username LIKE ? OR fullname LIKE ? OR email LIKE ? OR birthdate LIKE ? LIMIT 0, 10", 
				new Object[]{q, q, q, q}, new String[]{"string", "string", "string", "string"}, null));
    	return list.toArray(new User[list.size()]);
    }
	
	public User findByUsername(String username)
	{
		return (User)User.getModel().find("username = ?", new Object[]{username}, new String[]{"string"}, null);
	}
	    
    /**
     * @return the id_user
     */
    public int getId_user() 
    {
    	return  Integer.parseInt(data.get("id_user").toString());
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
    public java.sql.Date getBirthdate() 
    {
        try {
            String [] date = data.get("birthdate").toString().split("-");
            return (new java.sql.Date(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])));
        } catch (Exception ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
