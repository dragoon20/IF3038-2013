/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author Abraham Krisnanda
 */
import java.io.*;
import java.sql.*;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class DBConnection 
{
    private static Connection connection = null;
    private static boolean deployment = false;
    
    public static Connection getConnection() 
    {
        try {
        	if (connection != null) 
        	{
        		connection.close();
        	}
            Properties prop = new Properties();
            if (deployment)
            {
            	JSONObject credentials = (JSONObject)
						((JSONObject)
							((JSONArray)
								((JSONObject)
									JSONValue.parse(System.getenv("VCAP_SERVICES")))
								.get("mysql-5.1"))
							.get(0))
						.get("credentials");
            	prop.setProperty("driver", "com.mysql.jdbc.Driver");
            	prop.setProperty("url", "jdbc:mysql://"+credentials.get("host")+":"+credentials.get("port")+"/"+credentials.get("name"));
            	prop.setProperty("user", (String)credentials.get("user"));
            	prop.setProperty("password", (String)credentials.get("password"));
            }
            else
            {
                InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("/db.properties");
                prop.load(inputStream);
            }
            String driver = prop.getProperty("driver");
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
