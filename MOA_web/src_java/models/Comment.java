/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import controllers.MainApp;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
    
    public static String getTableName()
    {
        return "comment";
    }
    
    public boolean save() 
    {
    	Connection connection = DBConnection.getConnection();
    	if (!this.data.containsKey("id_komentar"))
    	{
	        try 
	        {
	            PreparedStatement statement = connection.prepareStatement
	            ("INSERT INTO `"+ getTableName()+"` (komentar, id_user, id_task) VALUES (?, ?, ?)");
	            // Parameters start with 1
	            statement.setString(1, getKomentar());
	            statement.setInt(2, getId_user());
	            statement.setInt(3, getId_task());
	            statement.executeUpdate();
	        } catch (SQLException ex) {
	            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return true;
    	}
    	else
    	{
    		return false;
    	}
    }
    
    public boolean checkValidity() 
    {
        return false;
    }
    
    public User getUser(String token, String id_komentar)
	{
            User user = new User();
            try {
			TreeMap<String, String> parameter = new TreeMap<String,String>();
			parameter.put("token", token);
			parameter.put("app_id", MainApp.appId);
                        parameter.put("id_komentar",id_komentar);
			String response = MainApp.callRestfulWebService(MainApp.serviceURL+"comment/get_user", parameter, "", 0);
			Object obj = JSONValue.parse(response);
                        JSONObject js_obj = (JSONObject) obj;
                        user.setId_user(Integer.valueOf((String)js_obj.get("id_user")));
                        user.setUsername((String)js_obj.get("user_name"));
                        user.setFullname((String)js_obj.get("fullname"));
                        user.setAvatar((String)js_obj.get("avatar"));
            }catch(Exception exc){
                  exc.printStackTrace();
            }
            return user;
    	}
	
	public Task getTask(String token, String id_komentar)
	{
		//return (Task)Task.getModel().find("id_task = ?", new Object[]{getId_task()}, new String[]{"integer"}, null);
            Task task = new Task();
            try {
			TreeMap<String, String> parameter = new TreeMap<String,String>();
			parameter.put("token", token);
			parameter.put("app_id", MainApp.appId);
                        parameter.put("id_komentar",id_komentar);
			String response = MainApp.callRestfulWebService(MainApp.serviceURL+"comment/get_task", parameter, "", 0);
			Object obj = JSONValue.parse(response);
                        JSONObject js_obj = (JSONObject) obj;
                        
                        task.setId_task(Integer.valueOf((String)js_obj.get("id_task")));
                        task.setNama_task((String)js_obj.get("nama_task"));
                        task.setStatus(Boolean.valueOf((String)js_obj.get("task_status")));
                        task.setDeadline(Date.valueOf((String) js_obj.get("deadline")));
                        task.setId_kategori(Integer.valueOf((String)js_obj.get("id_kategori")));
                        task.setId_task(Integer.valueOf((String)js_obj.get("id_task")));
            }catch(Exception exc){
                  exc.printStackTrace();
            }
            return task;
	}
    
    public Comment[] getLatest(String id_task, String timestamp, String token)
    {
        List<Comment> commentlist = new ArrayList<Comment>();
        try {
			TreeMap<String, String> parameter = new TreeMap<String,String>();
			parameter.put("token", token);
			parameter.put("app_id", MainApp.appId);
                        parameter.put("id_task", id_task);
                        parameter.put("timestamp", timestamp);
			String response = MainApp.callRestfulWebService("http://localhost:8088/MOA_services/comment/get_latest", parameter, "", 0);
                        
			JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
			for (Object obj : resp_obj)
			{
				JSONObject js_obj = (JSONObject) obj;
				Comment komen = new Comment();
                                komen.setId_komentar(Integer.valueOf((String)js_obj.get("id_komentar")));
                                komen.setTimestamp(Timestamp.valueOf((String)js_obj.get("timestamp")));
                                komen.setKomentar((String)js_obj.get("komentar"));
                                komen.setId_user(Integer.valueOf((String)js_obj.get("id_user")));
                                commentlist.add(komen);
			}
        }catch(Exception exc){

        }
        return commentlist.toArray(new Comment[commentlist.size()]);
    }
    
    public Comment[] getOlder(String id_task, String timestamp, String token)
    {
    	List<Comment> commentlist = new ArrayList<Comment>();
        try {
			TreeMap<String, String> parameter = new TreeMap<String,String>();
			parameter.put("token", token);
			parameter.put("app_id", MainApp.appId);
                        parameter.put("id_task", id_task);
                        parameter.put("timestamp", timestamp);
			String response = MainApp.callRestfulWebService("http://localhost:8088/MOA_services/comment/get_older", parameter, "", 0);
                        
			JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
			for (Object obj : resp_obj)
			{
				JSONObject js_obj = (JSONObject) obj;
				Comment komen = new Comment();
                                komen.setId_komentar(Integer.valueOf((String)js_obj.get("id_komentar")));
                                komen.setTimestamp(Timestamp.valueOf((String)js_obj.get("timestamp")));
                                komen.setKomentar((String)js_obj.get("komentar"));
                                komen.setId_user(Integer.valueOf((String)js_obj.get("id_user")));
                                commentlist.add(komen);
			}
        }catch(Exception exc){

        }
        return commentlist.toArray(new Comment[commentlist.size()]);
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
    public Timestamp getTimestamp() 
    {
        return ((Timestamp)data.get("timestamp"));
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp) {
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
