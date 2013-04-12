/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        try {
            PreparedStatement statement = connection.prepareStatement
            ("INSERT INTO `"+ User.getModel().GetTableName()+"` (id_komentar, komentar, id_user, id_task) VALUES (?, ?, ?)");
            // Parameters start with 1
            statement.setInt(1, getId_komentar());
            statement.setString(2, getKomentar());
            statement.setInt(3, getId_user());
            statement.setInt(4, getId_task());
            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Comment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public boolean checkValidity() 
    {
        return true;
    }
    
    public User getUser()
	{
    	return (User)User.getModel().find("id_user = '?'", new Object[]{getId_user()}, new String[]{"integer"}, new String[]{"id_user", "username", "fullname", "avatar"});
	}
	
	public Task getTask()
	{
		return (Task)Task.getModel().find("id_task = '?'", new Object[]{getId_task()}, new String[]{"integer"}, null);
	}
    
    public Comment[] getLatest(int id_task, String timestamp)
    {
		try 
		{
	    	Connection conn = DBConnection.getConnection();
	    	PreparedStatement prep = conn.prepareStatement("SELECT id_komentar, timestamp, komentar, c.id_user, username, fullname, avatar"+
										" FROM "+Comment.getTableName()+" as c INNER JOIN "+User.getTableName()+" as u "+
										" ON c.id_user=u.id_user WHERE id_task = '?' AND timestamp > '?' ORDER BY timestamp");
			prep.setInt(1, id_task);
	    	prep.setString(2, timestamp);
	    	
	    	List<Comment> result = new ArrayList<Comment>();
	    	ResultSet rs = prep.executeQuery();
	    	if (rs.next())
	    	{
	    		ResultSetMetaData metadata = rs.getMetaData();
	    		int column_count = metadata.getColumnCount();
	    		
	    		do
	    		{
	    			Comment c = new Comment();
	    			for (int i=1;i<=column_count;++i)
	    			{
	    				String label = metadata.getColumnLabel(i);
	    				c.putData(label, rs.getObject(i));
	    			}
	        		result.add(c);
	    		}while (rs.next());
	    	}
	    	return (Comment[])result.toArray();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
    
    public Comment[] getOlder(int id_task, String timestamp)
    {
    	try 
		{
	    	Connection conn = DBConnection.getConnection();
	    	PreparedStatement prep = conn.prepareStatement("SELECT id_komentar, timestamp, komentar, c.id_user, username, fullname, avatar"+
	    							" FROM "+Comment.getTableName()+" as c INNER JOIN "+User.getTableName()+" as u "+
	    							" ON c.id_user=u.id_user WHERE id_task = '?' AND timestamp < '?' ORDER BY timestamp DESC LIMIT 10");
	    	prep.setInt(1, id_task);
	    	prep.setString(2, timestamp);
	    	
	    	List<Comment> result = new ArrayList<Comment>();
	    	ResultSet rs = prep.executeQuery();
	    	if (rs.next())
	    	{
	    		ResultSetMetaData metadata = rs.getMetaData();
	    		int column_count = metadata.getColumnCount();
	    		
	    		do
	    		{
	    			Comment c = new Comment();
	    			for (int i=1;i<=column_count;++i)
	    			{
	    				String label = metadata.getColumnLabel(i);
	    				c.putData(label, rs.getObject(i));
	    			}
	        		result.add(c);
	    		}while (rs.next());
	    	}
	    	return (Comment[])result.toArray();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
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
    public String getTimestamp() {
        return ((String)data.get("timestamp"));
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(String timestamp) {
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
