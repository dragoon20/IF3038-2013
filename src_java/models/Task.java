/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        int affected_row=0;
        ResultSet generatedkeys = null; 
        if (!this.data.containsKey(this))
        {
            try {
                PreparedStatement statement = connection.prepareStatement
                ("INSERT INTO `"+ Task.getModel().GetTableName()+"` (nama_task, deadline, id_kategori) VALUES (?, ?, ?)");
                // Parameters start with 1
                statement.setString(1, getNama_task());
                statement.setDate(2, getDeadline());
                statement.setInt(3, getId_kategori());
                affected_row = statement.executeUpdate();
                if (affected_row ==0) {
                    throw new SQLException("Creating user failed, no rows affected");
                }
                
                generatedkeys = statement.getGeneratedKeys();
                // get generated Id from last SQL Execution
                if (generatedkeys.next()) {
                    PreparedStatement statement0 = connection.prepareStatement
                    ("INSERT INTO `assign` (id_user, id_task) VALUES (?,?)");
                    statement0.setInt(1, affected_row);
                    statement0.setInt(2, (int) generatedkeys.getLong(1));
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(Task.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return true;
        }
        return false;
    }
    
    public boolean checkValidity() {
        return true;
    }
    
    public Tag[] getTags() 
	{
    	List<DBSimpleRecord> list = Arrays.asList(Tag.getModel().findAll("id_tag IN (SELECT id_tag FROM have_tags WHERE id_task = ?)", new Object[]{getId_task()}, new String[]{"integer"}, null));
    	return list.toArray(new Tag[list.size()]);
	}
	
	public Category getCategory()
	{
		return (Category)Category.getModel().find("id_kategori = ?", new Object[]{getId_kategori()}, new String[]{"integer"}, null);
	}
	
	public Attachment[] getAttachment()
	{
		List<DBSimpleRecord> list = Arrays.asList(Attachment.getModel().findAll("id_task = ?", new Object[]{getId_task()}, new String[]{"integer"}, null));
		return list.toArray(new Attachment[list.size()]);
	}
	
	public User[] getAssignee()
	{
		List<DBSimpleRecord> list = Arrays.asList(User.getModel().findAll("id_user IN (SELECT id_user FROM assign WHERE id_task = ?)", new Object[]{getId_task()}, new String[]{"integer"}, new String[]{"id_user", "username"}));
		return list.toArray(new User[list.size()]);
	}
	
	public Comment[] getComment()
	{
		List<DBSimpleRecord> list = Arrays.asList(Comment.getModel().findAll("id_task = ? ORDER BY timestamp DESC LIMIT 10", new Object[]{getId_task()}, new String[]{"integer"}, null));
		Collections.reverse(list);
		return list.toArray(new Comment[list.size()]);
	}
	
	public int getTotalComment()
	{
		return Comment.getModel().findAll("id_task = ?", new Object[]{getId_task()}, new String[]{"integer"}, null).length;
	}

	public boolean getEditable(int id_user)
	{
		return !User.getModel().find("id_user IN (SELECT id_user FROM assign WHERE id_task=? AND id_user=?)", new Object[]{getId_task(), id_user}, new String[]{"integer", "integer"}, null).isEmpty();
	}
	
	public boolean getDeletable(int id_user)
	{
		return (id_user == getId_user());
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
