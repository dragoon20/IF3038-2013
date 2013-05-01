/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import controllers.MainApp;

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
    
    public Tag[] getTags(String token, int id_task) 
    {
        try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_tags", parameter, "", 0);
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

    public Category getCategory(String token, int id_task)
    {
        try {
            TreeMap<String, String> parameter = new TreeMap<String,String>();
            parameter.put("token", token);
            parameter.put("app_id", MainApp.appId);
            parameter.put("id_task", ""+id_task);
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_category", parameter, "", 0);
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
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_attachments", parameter, "", 0);
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
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_assignees", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            ArrayList<User> listOfUser = new ArrayList<User>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    User usr = new User();
                    usr.setUsername(String.valueOf(js_obj.get("assignee_name")));
                    usr.setId_user(Integer.parseInt(js_obj.get("assignee_id").toString()));
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
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_comments", parameter, "", 0);
            JSONArray resp_obj = (JSONArray)JSONValue.parse(response);
            
            ArrayList<Comment> listOfComment = new ArrayList<Comment>();
            for (Object obj : resp_obj)
            {
                    JSONObject js_obj = (JSONObject) obj;
                    Comment cmnt = new Comment();
                    cmnt.setKomentar(String.valueOf(js_obj.get("comment")));
                    cmnt.setTimestamp(Timestamp.valueOf(js_obj.get("timestamp").toString()));
                    cmnt.setId_komentar(Integer.valueOf(js_obj.get("id_comment").toString()));
                    
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
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_total_comments", parameter, "", 0);
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
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_editable", parameter, "", 0);
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
            
            String response = MainApp.callRestfulWebService(MainApp.serviceURL+"task/get_deleteable", parameter, "", 0);
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
