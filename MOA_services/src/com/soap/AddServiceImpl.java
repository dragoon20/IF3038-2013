package com.soap;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.jws.WebService;

import com.helper.GeneralHelper;
import com.models.Category;
import com.models.Comment;
import com.models.DBConnection;
import com.models.DBSimpleRecord;
import com.models.Tag;
import com.models.Task;
import com.models.User;

@WebService(endpointInterface = "com.soap.AddService")
public class AddServiceImpl implements AddService
{
	@Override
	public boolean add_new_user(String username, String email, String password,
							String fullname, String avatar, String birthdate) 
	{
		boolean success = false;        
        
    	if ((username!=null) && (email!=null) && 
    		(fullname!=null) && (avatar!=null) &&
    		(password!=null) && (birthdate!=null))
		{
    		try
    		{
	    		User new_user = new User();
	    		new_user.setUsername(username);
	    		new_user.setEmail(email);
	    		new_user.setFullname(fullname);
	    		new_user.setAvatar(avatar);
	    		new_user.setPassword(password);
	    		new_user.setBirthdate(new Date(DBSimpleRecord.sdf.parse(birthdate).getTime()));
	                    
	            if (!new_user.checkValidity())
	            {
	            	new_user.setPassword(DBSimpleRecord.MD5(new_user.getPassword()));
	                success = new_user.save();
	            }
    		} catch (Exception e)
    		{
    			e.printStackTrace();
    		}
		}
        
		return success;
	}
	
	@Override
	public int add_new_task(String token, String app_id, String nama_task, 
							String deadline, Integer id_kategori)
	{
		int id_task = -1;
		try
		{
			int id_user;
			if ((token!=null) &&(app_id!=null) && ((id_user = GeneralHelper.isLogin(token, app_id))!=-1))
			{
				if ((nama_task!=null) && (deadline!=null) && (id_kategori!=null) && 
					(((Category)Category.getModel().find("id_kategori = ?", new Object[]{id_kategori}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					Task task = new Task();
					task.setNama_task(nama_task);
					task.setDeadline(new Date(DBSimpleRecord.sdf.parse(deadline).getTime()));
					task.setId_kategori(id_kategori);
					task.setId_user(id_user);
					
					if ((!task.checkValidity()) && (task.save()))
					{
						id_task = task.getId_task();
					}
					else
					{
						throw new Exception();
					}
				}
				else
				{
					throw new Exception();
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return id_task;
	}
	
	@Override
	public boolean add_assignee(String token, String app_id, Integer id_task, String username)
	{
		boolean status = false;
		try
		{
			int id_user;
			if ((token!=null) &&(app_id!=null) && ((id_user = GeneralHelper.isLogin(token, app_id))!=-1))
			{
				User assignee;
				if ((id_task!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{id_task}, new String[]{"integer"}, null)).getEditable(id_user)) && 
					(!(assignee = (User)User.getModel().find("username = ?", new Object[]{username}, new String[]{"string"}, null)).isEmpty()))
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("INSERT INTO `assign` (id_user, id_task) VALUES(?, ?)");
					prep.setInt(1, assignee.getId_user());
					prep.setInt(2, id_task);
					
					status = (prep.executeUpdate()==1);
				}
				else
				{
					throw new Exception();
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}
	
	@Override
	public boolean add_tag(String token, String app_id, Integer id_task, String tag_name)
	{
		boolean success = false;
		try
		{
			int id_user;
			if ((token!=null) &&(app_id!=null) && ((id_user = GeneralHelper.isLogin(token, app_id))!=-1))
			{
				Tag tag = null;
				if ((id_task!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{id_task}, new String[]{"integer"}, null)).getEditable(id_user)) && 
					((tag = (Tag)Tag.getModel().find("tag_name = ?", new Object[]{tag_name}, new String[]{"string"}, null))==null))
				{
					tag = new Tag();
					tag.setTag_name(tag_name);
					if (tag.save())
					{
						Connection conn = DBConnection.getConnection();
						PreparedStatement prep = conn.prepareStatement("INSERT INTO `have_tags` (id_task, id_tag) VALUES(?, ?)");
						prep.setInt(1, id_task);
						prep.setInt(2, tag.getId_tag());
					
						success = (prep.executeUpdate()==1);
					}
				}
				else if (tag!=null)
				{
					success = true;
				}
				else
				{
					throw new Exception();
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return success;
	}
	
	@Override
	public boolean add_new_attachment(String token, String app_id, Integer id_task, String attachment)
	{
		boolean success = false;
		try
		{
			int id_user;
			if ((token!=null) &&(app_id!=null) && ((id_user = GeneralHelper.isLogin(token, app_id))!=-1))
			{
				if ((id_task!=null) && (attachment!=null) && 
					(((Task)Task.getModel().find("id_task = ?", new Object[]{id_task}, new String[]{"integer"}, null)).getEditable(id_user)))
				{
					Connection conn = DBConnection.getConnection();
					PreparedStatement prep = conn.prepareStatement("INSERT INTO `task_attachment` (id_task, attachment) VALUES(?, ?)");
					prep.setInt(1, id_task);
					prep.setString(2, attachment);
					System.out.println(attachment);
					success = (prep.executeUpdate()==1);
				}
				else
				{
					throw new Exception();
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return success;
	}
	
	@Override
	public int add_new_category(String token, String app_id, String nama_kategori, String usernames_list)
	{
		int id_cat = -1;
		try
		{
			int id_user;
			if ((token!=null) &&(app_id!=null) && ((id_user = GeneralHelper.isLogin(token, app_id))!=-1))
			{
				if ((nama_kategori!=null) && (usernames_list!=null))
				{
					try 
					{						
						Category kategori = new Category();
	                    kategori.setNama_kategori(nama_kategori);
	                    kategori.setId_user(id_user);
	                                        
						if ((!kategori.checkValidity()) && (kategori.save()))
						{
							id_cat = kategori.getId_kategori();
							String[] usernames = usernames_list.split(";");
							String[] paramType = new String[usernames.length];
							for (int i=0;i<usernames.length;++i)
							{
								usernames[i] = usernames[i].trim();
							}
							if (usernames.length > 1)
							{
								StringBuilder query = new StringBuilder("username IN (");
								for (int i=0;i<usernames.length;++i)
								{
									query.append("?");
									if (i!=usernames.length-1)
									{
										query.append(",");
									}
									paramType[i] = "string";
								}
								query.append(")");
								
								User[] users = (User[])User.getModel().findAll(query.toString(), usernames, paramType, null);
								Connection conn = DBConnection.getConnection();
								for (User user : users)
								{
									PreparedStatement prep = conn.prepareStatement("INSERT INTO edit_kategori (id_user, id_katego) VALUES (?, ?)");
									prep.setInt(1, user.getId_user());
									prep.setInt(2, id_cat);
								}
							}
						}
						else
						{
							throw new Exception();
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					throw new Exception();
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		return id_cat;
	}
	
	@Override
	public int add_new_comment(String token, String app_id, Integer id_task, String komentar)
	{
		int id_comment = -1;
		try
		{
			int id_user;
			if ((token!=null) &&(app_id!=null) && ((id_user = GeneralHelper.isLogin(token, app_id))!=-1))
			{
				if ((id_task!=null) && (komentar!=null) && 
					(!((Task)Task.getModel().find("id_task = ?", new Object[]{id_task}, new String[]{"integer"}, null)).isEmpty()))
				{
					Comment comment = new Comment();
					comment.setId_user(id_user);
					comment.setId_task(id_task);
					comment.setKomentar(komentar);
					if ((!comment.checkValidity()) && (comment.save()))
					{
						id_comment = comment.getId_komentar();
					}
					else
					{
						throw new Exception();
					}
				}
				else
				{
					throw new Exception();
				}
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return id_comment;
	}
}
