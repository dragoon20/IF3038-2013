/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
import java.sql.*;
import java.lang.*;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Abraham Krisnanda
 */
public class DBSimpleRecord {
    private Connection connection;
    private static Class c;
    protected static Object model;
    protected static HashMap<String, String> tuples;
    
    public DBSimpleRecord() {
        connection = DBConnection.getConnection();
    }
    
    public static Object getModel()
    {
        try {
            if (model==null)
            {
                
                c = Class.forName(this.);
                model = c.newInstance();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBSimpleRecord.class.getName()).log(Level.SEVERE, null, ex);
        }

        return model;
    }
        
    public Object find(String query, String[] selection)
    {
        if (query != "")
        {
            query = " WHERE "+query;
        }
        String[] select = {}; // temp variable for selected attribute
        String cmd;
        if (selection.length!=0) {
            for (int i=0;i<selection.length;i++)
            {
                select[select.length] = (selection[i] +", ");
            }
            int maxlength = select.toString().length();
            cmd = (select.toString().substring(0,maxlength -2)) ; // menghapus ", " pada akhir query
        }
        else {
            cmd ="*";
        }
        try {
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT" + select.toString() + " FROM " + this.GetClassName()) ;
                while (rs.next()) {
                    try {
                        Class c = Class.forName(this.GetClassName());
                        model = c.newInstance();
                    }
                    catch (LinkageError e) {
                        e.printStackTrace();
                    }
                    catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException ex) {
                        Logger.getLogger(DBSimpleRecord.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(DBSimpleRecord.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        return model;
    }
}
