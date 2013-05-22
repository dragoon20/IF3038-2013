package com.account;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.model.Category;
import com.model.Task;

public class LoginHistory 
{
    private String username;
    private String password;
    private List<Category> categories;
    
    public LoginHistory()
    {
        username = "";
        password = "";
        categories = new ArrayList<Category>();
    }
    
    public LoginHistory(String user, String pass)
    {
        username = user;
        password = pass;
        categories = new ArrayList<Category>();
    }
    
    public boolean parse_xml() throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("loginhistory.txt"));
        
        String line;
        boolean ret = false;
        try
        {  
            line = br.readLine();
            if (line != "\0")
            {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            	JSONObject main = (JSONObject)JSONValue.parse(line);
	            username = (String)main.get("username");
	            password = (String)main.get("password");
	            categories = new ArrayList<Category>();
	            
		      	JSONArray temp_categories = (JSONArray)main.get("categories");
		      	for (Object temp : temp_categories)
		      	{
		      		JSONObject obj = (JSONObject)temp;
		      		Category c = new Category(Integer.parseInt(obj.get("id_kategori").toString()), (String)obj.get("nama_kategori"));
		      		JSONArray tasks = (JSONArray)obj.get("tasks");
		      		for (Object temp_obj : tasks)
		      		{
		      			JSONObject obj2 = (JSONObject)temp_obj;
		      			Task t = new Task(Integer.parseInt(obj2.get("id_task").toString()), (String)obj2.get("nama_task"), (Boolean)obj2.get("status"), sdf.parse((String)obj2.get("deadline")));
		      			c.add(t);
		      		}
		      		categories.add(c);
		      	}
	            
	            ret = true;
            }
        }
        catch (Exception e)
        {
            System.out.println("error : " + e);
            username = "";
            password = "";
            categories = new ArrayList<Category>();
        }        
        
        br.close();
        return ret;
    }

    public void produce_xml() throws IOException
    {
        OutputStreamWriter osw = new FileWriter("loginhistory.txt");
        
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", username);
        map.put("password", password);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (categories!=null)
        {
        	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        	for (Category c : categories)
        	{
        		Map<String, Object> map2 = new HashMap<String, Object>();
        		map2.put("id_kategori", c.getId_kategori());
        		map2.put("nama_kategori", c.getNama_kategori());
        		List<Map<String, Object>> list2 = new ArrayList<Map<String,Object>>();
        		for (Task t : c.getList_task())
        		{
        			Map<String, Object> map3 = new HashMap<String, Object>();
        			map3.put("id_task", t.getId_task());
            		map3.put("nama_task", t.getNama_task());
            		map3.put("status", t.getStatus());
            		map3.put("deadline", sdf.format(t.getDeadline()));
            		
            		list2.add(map3);
        		}
        		map2.put("tasks", list2);
        		
        		list.add(map2);
        	}
        	map.put("categories", list);
        }
        osw.write(JSONObject.toJSONString(map));
        osw.flush();
        osw.close();
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
