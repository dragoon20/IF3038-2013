package com.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import com.models.DBConnection;

public class GeneralHelper 
{
	public static String app_id = "";
	
	public static Integer isLogin(String token, String app_id)
	{
		try 
		{
			Connection conn = DBConnection.getConnection();
			PreparedStatement prep;
			prep = conn.prepareStatement("SELECT * FROM `tokens` WHERE token = ? AND id_app IN " +
										"(SELECT * FROM `applications WHERE app_id = ? LIMIT 1`) LIMIT 1");
			
			prep.setString(1, token);
			prep.setString(2, app_id);
			
			ResultSet rs = prep.executeQuery();
			if ((rs.last()) && (rs.getRow()==1))
			{
				Timestamp timestamp = rs.getTimestamp(1);
				int id_user = rs.getInt(3);
				int id_app = rs.getInt(4);
				rs.close();
				if (new Date().getTime() - timestamp.getTime() <= (1000 * 60 * 60 * 24 * 30))
				{
					conn = DBConnection.getConnection();
					prep = conn.prepareStatement("UPDATE `tokens` SET timestamp = NOW() WHERE id_user = ? AND id_app = ?");
					prep.setInt(1, id_user);
					prep.setInt(2, id_app);
					int affected = prep.executeUpdate();
					if (affected == 1)
					{
						return id_user;
					}
				}
				else
				{
					conn = DBConnection.getConnection();
					prep = conn.prepareStatement("DELETE FROM `tokens` WHERE id_user = ? AND id_app = ?");
					prep.setInt(1, id_user);
					prep.setInt(2, id_app);
					prep.executeUpdate();
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
}
