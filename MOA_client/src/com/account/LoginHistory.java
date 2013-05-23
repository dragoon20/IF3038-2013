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
    private byte[] username;
    private byte[] password;
    private List<Category> categories;
    
    public LoginHistory()
    {
        username = new byte[0];
        password = new byte[0];
        categories = new ArrayList<Category>();
    }
    
    public LoginHistory(byte[] user, byte[] pass)
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
	            String[] split = ((String)main.get("username")).split(",");
	            username = new byte[split.length];
	            for (int i=0;i<split.length;++i)
	            {
	            	username[i] = Byte.parseByte(split[i]);
	            }
	            
	            split = ((String)main.get("password")).split(",");
	            password = new byte[split.length];
	            for (int i=0;i<split.length;++i)
	            {
	            	password[i] = Byte.parseByte(split[i]);
	            }
	            
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
            username = new byte[0];
            password = new byte[0];
            categories = new ArrayList<Category>();
        }        
        
        br.close();
        return ret;
    }

    public void produce_xml() throws IOException
    {
        OutputStreamWriter osw = new FileWriter("loginhistory.txt");
        
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<username.length;++i)
        {
        	sb.append(username[i]);
        	sb.append(",");
        }
        sb.delete(sb.length()-1, sb.length());
        map.put("username", sb.toString());

        sb = new StringBuilder();
        for (int i=0;i<password.length;++i)
        {
        	sb.append(password[i]);
        	sb.append(",");
        }
        sb.delete(sb.length()-1, sb.length());
        map.put("password", sb.toString());
        
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
    public byte[] getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(byte[] username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(byte[] password) {
        this.password = password;
    }

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
