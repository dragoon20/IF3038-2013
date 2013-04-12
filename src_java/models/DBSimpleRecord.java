/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
/**
 *
 * @author Abraham Krisnanda
 */
public abstract class DBSimpleRecord 
{
    private Connection connection;
    protected HashMap<String, Object> data;
    
    public DBSimpleRecord() 
    {
        connection = DBConnection.getConnection();
        data = new HashMap<String, Object>();
    }
    
    protected abstract String GetClassName();
    protected abstract String GetTableName();
    
    public void putData(String key, Object value)
    {
    	data.put(key, value);
    }
    
    public DBSimpleRecord find(String query, String[] selection)
    {
    	try
    	{
			Class<?> c = Class.forName(GetClassName());
			DBSimpleRecord result = (DBSimpleRecord)c.newInstance();

			if (query != "")
	        {
	            query = " WHERE "+query;
	        }
	        StringBuilder cmd = new StringBuilder();
	        if ((selection!=null) && (selection.length!=0))
	        {
	            for (int i=1;i<=selection.length;i++)
	            {
	            	cmd.append(selection[i]);
	            	cmd.append(", ");
	            }
	            cmd.substring(0, cmd.length()-2);
	        }
	        else {
	            cmd.append("*");
	        }
	        
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT " + cmd.toString() + " FROM " + this.GetTableName() + query) ;

            ResultSetMetaData meta_data = rs.getMetaData();
            int column_count = meta_data.getColumnCount();
            if (rs.next())
            {
            	for (int i=1;i<=column_count;++i)
            	{
            		String label = meta_data.getColumnLabel(i);
            		result.putData(label, rs.getObject(i));
            	}
            }
            return result;
    	} catch (SQLException e) {
            e.printStackTrace();
    	} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
}