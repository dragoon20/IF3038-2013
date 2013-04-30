/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import controllers.MainApp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

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
    
    public static String getTableName() 
    {
    	return "user";
    }
    
    public boolean save() 
    {
    	Connection connection = DBConnection.getConnection();
        if (!this.data.containsKey("id_user"))
        {
            // new user
        	if (User.getModel().find("username = ? or email = ?", new Object[]{getUsername(), getEmail()}, new String[]{"string", "string"}, null).isEmpty())
            {
                try {
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
        	List<DBSimpleRecord> list = Arrays.asList(User.getModel().findAll("username = ? or email = ?", new Object[]{getUsername(), getEmail()}, new String[]{"string", "string"}, null));
        	if (list.size() > 1)
        	{
        		return false;
        	}
        	else
        	{
        		try {
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
    
    public Task[] getCreatedTasks(String token)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_created_tasks", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Task> listOfTask = new ArrayList<Task>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Task tsk = new Task();
                    tsk.setNama_task(js_obj.get("nama_task").toString());
                    tsk.setDeadline(Date.valueOf(js_obj.get("deadline").toString()));
                    tsk.setId_kategori(Integer.valueOf(js_obj.get("id_kategori").toString()));
                    tsk.setId_task(Integer.valueOf(js_obj.get("id_task").toString()));
                    tsk.setId_user(Integer.valueOf(js_obj.get("id_User").toString()));
                    tsk.setStatus(Boolean.valueOf(js_obj.get("status").toString()));
                    listOfTask.add(tsk);
            }
            Task [] tasks = new Task[listOfTask.size()];
            int i = 0;
            for(Task tsk : listOfTask){
                tasks[i] = tsk;
                i++;
            }
            return  tasks;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
    
    public Task[] getAssignedTasks(String token)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_assigned_tasks", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Task> listOfTask = new ArrayList<Task>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Task tsk = new Task();
                    tsk.setNama_task((String)js_obj.get("nama_task"));
                    tsk.setDeadline(Date.valueOf(js_obj.get("deadline").toString()));
                    tsk.setId_kategori(Integer.parseInt(js_obj.get("id_kategori").toString()));
                    tsk.setId_task(Integer.parseInt(js_obj.get("id_task").toString()));
                    tsk.setId_user(Integer.parseInt(js_obj.get("id_user").toString()));
                    tsk.setStatus(Boolean.parseBoolean(js_obj.get("status").toString()));
                    listOfTask.add(tsk);
            }
            
            Task [] tasks = new Task[listOfTask.size()];
            int i = 0;
            for(Task tsk : listOfTask){
                tasks[i] = tsk;
                i++;
            }
            return  tasks;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
        
    public Category[] getCategories(String token)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_categories", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Category> listOfCategory = new ArrayList<Category>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Category ctg = new Category();
                    ctg.setId_kategori(Integer.valueOf(js_obj.get("id_kategori").toString()));
                    ctg.setId_user(Integer.valueOf(js_obj.get("id_user").toString()));
                    ctg.setNama_kategori(String.valueOf(js_obj.get("nama_kategori")));
                    listOfCategory.add(ctg);
            }
            Category [] ctgs = new Category[listOfCategory.size()];
            int i = 0;
            for(Category ctg : listOfCategory){
                ctgs[i] = ctg;
                i++;
            }
            return  ctgs;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
    
    public Task[] getTasks(String token)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_tasks", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Task> listOfTask = new ArrayList<Task>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Task tsk = new Task();
                    tsk.setNama_task((String)js_obj.get("nama_task"));
                    tsk.setDeadline(Date.valueOf(js_obj.get("deadline").toString()));
                    tsk.setId_kategori(Integer.parseInt(js_obj.get("id_kategori").toString()));
                    tsk.setId_task(Integer.parseInt(js_obj.get("id_task").toString()));
                    tsk.setId_user(Integer.parseInt(js_obj.get("id_user").toString()));
                    tsk.setStatus(Boolean.parseBoolean(js_obj.get("status").toString()));
                    listOfTask.add(tsk);
            }
            
            Task [] tasks= new Task[listOfTask.size()];
            int i = 0;
            for(Task tsk : listOfTask){
                tasks[i] = tsk;
                i++;
            }
            return  tasks;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
    
    public Task[] getTasks(String token, int status, int category_id)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("status", ""+status);
            parameter.put("id_kategori", ""+category_id);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_tasks", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Task> listOfTask = new ArrayList<Task>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Task tsk = new Task();
                    tsk.setNama_task((String)js_obj.get("nama_task"));
                    tsk.setDeadline(Date.valueOf(js_obj.get("deadline").toString()));
                    tsk.setId_kategori(Integer.parseInt(js_obj.get("id_kategori").toString()));
                    tsk.setId_task(Integer.parseInt(js_obj.get("id_task").toString()));
                    tsk.setId_user(Integer.parseInt(js_obj.get("id_user").toString()));
                    tsk.setStatus(Boolean.parseBoolean(js_obj.get("status").toString()));
                    listOfTask.add(tsk);
            }
            
            Task [] tasks = new Task[listOfTask.size()];
            int i = 0;
            for(Task tsk : listOfTask){
                tasks[i] = tsk;
                i++;
            }
            return  tasks;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
    
    public Task[] getTasksLike(String q,String token)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("key", q);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_tasks_like", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Task> listOfTask = new ArrayList<Task>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Task tsk = new Task();
                    tsk.setNama_task((String)js_obj.get("nama_task"));
                    tsk.setDeadline(Date.valueOf(js_obj.get("deadline").toString()));
                    tsk.setId_kategori(Integer.parseInt(js_obj.get("id_kategori").toString()));
                    tsk.setId_task(Integer.parseInt(js_obj.get("id_task").toString()));
                    tsk.setId_user(Integer.parseInt(js_obj.get("id_user").toString()));
                    tsk.setStatus(Boolean.parseBoolean(js_obj.get("status").toString()));
                    listOfTask.add(tsk);
            }
            
             Task [] tasks = new Task[listOfTask.size()];
            int i = 0;
            for(Task tsk : listOfTask){
                tasks[i] = tsk;
                i++;
            }
            return  tasks;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
    
    public Category[] getCategoriesLike(String q,String token)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("key", q);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/get_categories_like", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Category> listOfCategory = new ArrayList<Category>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Category ctg = new Category();
                    ctg.setId_kategori(Integer.valueOf(js_obj.get("id_kategori").toString()));
                    ctg.setId_user(Integer.valueOf(js_obj.get("id_user").toString()));
                    ctg.setNama_kategori(String.valueOf(js_obj.get("nama_kategori")));
                    listOfCategory.add(ctg);
            }
            Category [] ctgs = new Category[listOfCategory.size()];
            int i = 0;
            for(Category ctg : listOfCategory){
                ctgs[i] = ctg;
                i++;
            }
            return  ctgs;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
    
    public User[] findAllLike(String q,String token)
    {
    	try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("key", q);
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/find_all_like", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<User> listOfUser = new ArrayList<User>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    
                    User usr = new User();
                    usr.setAvatar(String.valueOf(js_obj.get("avatar")));
                    usr.setBirthdate(Date.valueOf(js_obj.get("birthdate").toString()));
                    usr.setEmail(String.valueOf(js_obj.get("email")));
                    usr.setFullname(String.valueOf(js_obj.get("fullname")));
                    usr.setId_user(Integer.valueOf(js_obj.get("id_user").toString()));
                    usr.setPassword(String.valueOf(js_obj.get("password")));
                    usr.setUsername(String.valueOf(js_obj.get("username")));
                    
                    listOfUser.add(usr);
            }
            User [] usrs = new User[listOfUser.size()];
            int i = 0;
            for(User usr : listOfUser){
                usrs[i] = usr;
                i++;
            }
            return  usrs;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }
	
	public User findByUsername(String username,String token)
	{
            try {
                TreeMap<String, String> parameter = new TreeMap<String,String>();
                parameter.put("token", token);
                parameter.put("app_id", MainApp.appId);
                parameter.put("username", username);
                String response = MainApp.callRestfulWebService(MainApp.serviceURL+"user/find_by_username", parameter, "", 0);
                JSONObject resp_obj = (JSONObject) JSONValue.parse(response);

                User usr = new User();
                usr.setAvatar(String.valueOf(resp_obj.get("avatar")));
                usr.setBirthdate(Date.valueOf(resp_obj.get("birthdate").toString()));
                usr.setEmail(String.valueOf(resp_obj.get("email")));
                usr.setFullname(String.valueOf(resp_obj.get("fullname")));
                usr.setId_user(Integer.valueOf(resp_obj.get("id_user").toString()));
                usr.setPassword(String.valueOf(resp_obj.get("password")));
                usr.setUsername(String.valueOf(resp_obj.get("username")));

                return usr;
            }catch(Exception exc){
                exc.printStackTrace();
                return null;
            }
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
