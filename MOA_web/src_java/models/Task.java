/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import controllers.MainApp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
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
public class Task extends DBSimpleRecord {
    /*private int id_task;
    private String nama_task;
    private boolean status;
    private Timestamp deadline;
    private int id_kategori;
    private int id_user;*/
    public static Task model;
    public static Task getModel() 
    {
        if(model==null)
        {
            model = new Task();
        }
        return model;
    }
    
    @Override
    protected  String GetClassName() 
    {
        return "models.Task";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "task";
    }
    
    public static String getTableName() 
    {
    	return "task";
    }
    
    public boolean save() 
    {
        // TIDAK MENGGUNAKAN USER ID, USER ID HARUS DITANGANI DI LUAR
        // check task name
    	Connection connection = DBConnection.getConnection();
        if (!this.data.containsKey("id_task"))
        {
            try {
                int affected_row=0;
                PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO `"+ Task.getModel().GetTableName()+"` (nama_task, deadline, id_kategori, id_user) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                // Parameters start with 1
                statement.setString(1, getNama_task());
                statement.setDate(2, getDeadline());
                statement.setInt(3, getId_kategori());
                statement.setInt(4, getId_user());
                
                affected_row = statement.executeUpdate();
                if (affected_row ==0) 
                {
                    throw new SQLException("Creating user failed, no rows affected");
                }
                
                ResultSet generatedkeys = statement.getGeneratedKeys();
                // get generated Id from last SQL Execution
                if (generatedkeys.next()) 
                {
                	setId_task(generatedkeys.getInt(1));
                	
                	List<User> assignees = (List<User>) data.get("assignee");
                	for (User u : assignees)
                	{
	                    statement = connection.prepareStatement
	                    		("INSERT INTO `assign` (id_user, id_task) VALUES (?,?)");
	                    statement.setInt(1, u.getId_user());
	                    statement.setInt(2, getId_task());
	                    
	                    statement.executeUpdate();
                	}
                	
                	List<Object> list = Arrays.asList((Object[])data.get("tag"));
                	String[] tags = list.toArray(new String[list.size()]);
                	for (String tag : tags)
                	{
                		Tag temptag = (Tag)Tag.getModel().find("tag_name = ?", new Object[]{tag}, new String[]{"string"}, null);
                		if (temptag.isEmpty())
                		{
                			temptag.setTag_name(tag);
                			temptag.save();
                		}
                		
                		statement = connection.prepareStatement
	                    		("INSERT INTO `have_tags` (id_task, id_tag) VALUES (?,?)");
	                    statement.setInt(1, getId_task());
	                    statement.setInt(2, temptag.getId_tag());
	                    
	                    statement.executeUpdate();
                	}
                	
                	List<Map<String, Object>> attachments = (List<Map<String, Object>>)data.get("attachments");
                	for (Map<String, Object> entry : attachments)
                	{
                		String name = (String)entry.get("attachment");
                		InputStream in = (InputStream)entry.get("temp");

                		Attachment attachment = new Attachment();
                		attachment.setId_task(getId_task());
                		attachment.setAttachment(name);
                		if (attachment.save())
                		{
                			FileOutputStream out = new FileOutputStream(new File((String)entry.get("location")));
    						
    						int read = 0;
    						byte[] bytes = new byte[1024];
    						while ((read = in.read(bytes)) != -1)
    						{
    							out.write(bytes, 0, read);
    						}
    						out.close();
                		}
                	}
                }
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
        }
        else
        {
        	try {
                int affected_row=0;
                PreparedStatement statement = connection.prepareStatement
                ("UPDATE `"+ Task.getModel().GetTableName()+"` SET nama_task = ?, deadline = ?, id_kategori = ?, id_user = ? WHERE id_task = ?");
                // Parameters start with 1
                statement.setString(1, getNama_task());
                statement.setDate(2, getDeadline());
                statement.setInt(3, getId_kategori());
                statement.setInt(4, getId_user());
                statement.setInt(5, getId_task());
                
                affected_row = statement.executeUpdate();
                if (affected_row ==0) 
                {
                    throw new SQLException("Creating user failed, no rows affected");
                }
            	
            	statement = connection.prepareStatement
                		("DELETE FROM `assign` WHERE id_task = ?");
            	statement.setInt(1, getId_task());
            	statement.executeUpdate();
            	List<User> assignees = (List<User>) data.get("assignee");
            	for (User u : assignees)
            	{
                    statement = connection.prepareStatement
                    		("INSERT INTO `assign` (id_user, id_task) VALUES (?,?)");
                    statement.setInt(1, u.getId_user());
                    statement.setInt(2, getId_task());
                    
                    statement.executeUpdate();
            	}
            	
            	statement = connection.prepareStatement
                		("DELETE FROM `have_tags` WHERE id_task = ?");
            	statement.setInt(1, getId_task());
            	statement.executeUpdate();
            	List<Object> list = Arrays.asList((Object[])data.get("tag"));
            	String[] tags = list.toArray(new String[list.size()]);
            	for (String tag : tags)
            	{
            		Tag temptag = (Tag)Tag.getModel().find("tag_name = ?", new Object[]{tag}, new String[]{"string"}, null);
            		if (temptag.isEmpty())
            		{
            			temptag.setTag_name(tag);
            			temptag.save();
            		}
            		
            		statement = connection.prepareStatement
                    		("INSERT INTO `have_tags` (id_task, id_tag) VALUES (?,?)");
                    statement.setInt(1, getId_task());
                    statement.setInt(2, temptag.getId_tag());
                    
                    statement.executeUpdate();
            	}
            	
            	List<Map<String, Object>> attachments = (List<Map<String, Object>>)data.get("attachments");
            	for (Map<String, Object> entry : attachments)
            	{
            		String name = (String)entry.get("attachment");
            		InputStream in = (InputStream)entry.get("temp");

            		Attachment attachment = new Attachment();
            		attachment.setId_task(getId_task());
            		attachment.setAttachment(name);
            		if (attachment.save())
            		{
            			FileOutputStream out = new FileOutputStream(new File((String)entry.get("location")));
						
						int read = 0;
						byte[] bytes = new byte[1024];
						while ((read = in.read(bytes)) != -1)
						{
							out.write(bytes, 0, read);
						}
						out.close();
            		}
                }
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            } catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
        }
    }
    
    public boolean checkValidity() 
    {
    	boolean status = false;
    	if (!getNama_task().matches("[a-zA-Z0-9 ]{1,25}"))
    	{
    		status = true;
    	}
		
    	String[] tempuser = ((String)data.get("assignee")).split(",");
    	List<User> assignees = new ArrayList<User>();
    	for (String user : tempuser)
    	{
    		User temp = (User)User.getModel().find("username = ?", new Object[]{user}, new String[]{"string"}, new String[]{"id_user"});
    		if (!temp.isEmpty())
    		{
    			User u = new User();
    			u.setId_user(temp.getId_user());
    			u.setUsername(user);
    			assignees.add(u);
    		}
    		else
    		{
    			status = true;
    		}
    	}
    	putData("assignee", assignees);
    	putData("tag", ((String)data.get("tag")).split(","));
        return status;
    }
    
    public Tag[] getTags(String token, int id_task) 
    {
        try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_tags", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Tag> listOfTag = new ArrayList<Tag>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Tag tag = new Tag();
                    tag.setTag_name(String.valueOf(js_obj.get("tag_name")));
                    listOfTag.add(tag);
            }
            Tag [] tags = new Tag[listOfTag.size()];
            int i = 0;
            for(Tag tag : listOfTag){
                tags[i] = tag;
                i++;
            }
            return  tags;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }

    public Category getCategory(String token,int id_task)
    {
        try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_category", parameter, "", 0);
            JSONObject resp_obj = (JSONObject)JSONValue.parse(response);
            
            Category ctg = new Category();
            ctg.setNama_kategori(String.valueOf(resp_obj.get("category")));
            
            return  ctg;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }

    public Attachment[] getAttachment(String token,int id_task)
    {
        try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_attachments", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Attachment> listOfAtt = new ArrayList<Attachment>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Attachment att = new Attachment();
                    att.setAttachment(js_obj.get("attachment_link").toString());
                    listOfAtt.add(att);
            }
            
            Attachment [] atts = new Attachment[listOfAtt.size()];
            int i = 0;
            for(Attachment att : listOfAtt){
                atts[i] = att;
                i++;
            }
            return  atts;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }

    public User[] getAssignee(String token, int id_task)
    {
         try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_assignees", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<User> listOfUser = new ArrayList<User>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    User usr = new User();
                    usr.setUsername(String.valueOf("assignee_name"));
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

    public Comment[] getComment(String token, int id_task)
    {
        try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_comments", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<Comment> listOfComment = new ArrayList<Comment>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Comment cmnt = new Comment();
                    cmnt.setKomentar(String.valueOf(js_obj.get("comment")));
                    cmnt.setTimestamp(Timestamp.valueOf(js_obj.get("timestamp").toString()));
                    
                    JSONObject js_user = (JSONObject) JSONValue.parse(js_obj.get("user").toString());
                    cmnt.setId_user(Integer.valueOf(js_user.get("id_user").toString()));
                    
                    listOfComment.add(cmnt);
            }
            
            Comment [] cmns = new Comment[listOfComment.size()];
            int i = 0;
            for(Comment cmn : listOfComment){
                cmns[i] = cmn;
                i++;
            }
            return  cmns;
        }catch(Exception exc){
            exc.printStackTrace();
            return null;
        }
    }

    public int getTotalComment(String token,int id_task)
    {
         try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_total_comments", parameter, "", 0);
            JSONObject resp_obj = (JSONObject)JSONValue.parse(response);
            
            return Integer.valueOf(resp_obj.get("total_comments").toString());
        }catch(Exception exc){
            exc.printStackTrace();
            return 0;
        }
    }

    public boolean getEditable(String token,int id_task)
    {
        try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_editable", parameter, "", 0);
            JSONObject resp_obj = (JSONObject)JSONValue.parse(response);
            
            return Boolean.valueOf(resp_obj.get("success").toString());
        }catch(Exception exc){
            exc.printStackTrace();
            return false;
        }
    }

    public boolean getDeletable(String token,int id_task)
    {
            try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService("http://localhost:8080/MOA_services/task/get_deleteable", parameter, "", 0);
            JSONObject resp_obj = (JSONObject)JSONValue.parse(response);
            
            return Boolean.valueOf(resp_obj.get("success").toString());
        }catch(Exception exc){
            exc.printStackTrace();
            return false;
        }
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
        data.put ("id_task",id_task);
    }

    /**
     * @return the nama_task
     */
    public String getNama_task() {
        return (String)data.get("nama_task");
    }

    /**
     * @param nama_task the nama_task to set
     */
    public void setNama_task(String nama_task) {
        data.put ("nama_task",nama_task);
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return (Boolean)data.get("status");
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        data.put("status",status);
    }

    /**
     * @return the deadline
     */
    public Date getDeadline() {
        return (Date)data.get("deadline");
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(Date deadline) {
        data.put("deadline",deadline);
    }

    /**
     * @return the id_kategori
     */
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
     * @return the id_user
     */
    public int getId_user() {
        return (Integer)data.get("id_user");
    }

    /**
     * @param id_user the id_user to set
     */
    public void setId_user(int id_user) {
        data.put("id_user",id_user);
    }
}
