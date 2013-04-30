/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.services;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.helper.GeneralHelper;
import com.models.Category;
import com.models.DBSimpleRecord;
import com.models.User;
import com.template.BasicServlet;

/**
 *
 * @author TOSHIBA
 */
public class CategoryService extends BasicServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public void edit_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
                            Category kategori;
                            if (("POST".equals(request.getMethod())) && (request.getParameter("id_kategori")!=null) && (request.getParameter("nama_kategori")!=null) && 
                                    ((kategori = (Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("id_kategori"))}, new String[]{"integer"}, null)).getEditable(id_user)))
                            {
                                    kategori.setNama_kategori(request.getParameter("nama_kategori"));
                                    kategori.setId_user(id_user);
                                    if ((!kategori.checkValidity()) && (kategori.save()))
                                    {
                                            Map<String, Boolean> map = new HashMap<String, Boolean>();
                                            map.put("success", true);
                                            JSONObject ret = new JSONObject(map);

                                            PrintWriter pw = response.getWriter();
                                            pw.print(ret.toJSONString());
                                            pw.close();
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
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(ret.toJSONString());
			pw.close();
		}
	}
        
    public void delete_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (("POST".equals(request.getMethod())) && 
					(request.getParameter("id_kategori")!=null) &&  
					(((Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("id_kategori"))}, new String[]{"integer"}, null)).getDeletable(id_user)))
				{
					if (Category.getModel().delete("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("id_kategori"))}, new String[]{"integer"})==1)
					{
						Map<String, Boolean> map = new HashMap<String, Boolean>();
						map.put("success", true);
						JSONObject ret = new JSONObject(map);
						PrintWriter pw = response.getWriter();
						pw.print(ret.toJSONString());
						pw.close();
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
			Map<String, Boolean> map = new HashMap<String, Boolean>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			
			PrintWriter pw = response.getWriter();
			pw.print(ret.toJSONString());
			pw.close();
		}
	}
        
	public void get_editable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_kategori")!=null)
				{
					Category kategori = (Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("id_kategori"))}, new String[]{"integer"}, null);
					
                                        Map<String, Object> map = new HashMap<String, Object>();
					map.put("success", kategori.getEditable(id_user));
					JSONObject ret = new JSONObject(map);
					PrintWriter pw = response.getWriter();
					pw.print(ret.toJSONString());
                                        pw.close();
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", "");
			JSONObject ret = new JSONObject(map);
			PrintWriter pw = response.getWriter();
			pw.print(ret.toJSONString());
                        pw.close();
		}
	}
     
    public void get_deletable(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int id_user;
			if ((request.getParameter("token")!=null) && (request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_kategori")!=null)
				{
					Category kategori = (Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("id_kategori"))}, new String[]{"integer"}, null);
					
                    Map<String, Object> map = new HashMap<String, Object>();
                    boolean success = kategori.getDeletable(id_user);
					map.put("success", success);
					JSONObject ret = new JSONObject(map);
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
					pw.close();
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
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("success", false);
			JSONObject ret = new JSONObject(map);
			PrintWriter pw = response.getWriter();
			pw.print(JSONValue.toJSONString(ret));
			pw.close();
		}
	}
    
    public void retrieve_categories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
    	try
		{
			int id_user;
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((id_user = GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				Category[] raw = ((User)User.getModel().find("id_user = ?", new Object[]{id_user}, new String[]{"integer"}, null)).getCategories();
				
				List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
				for (Category cat : raw)
				{
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("nama_kategori", cat.getNama_kategori());
					map.put("id", cat.getId_kategori());
					map.put("canDeleteCategory", cat.getDeletable(id_user));
					map.put("canEditCategory", cat.getEditable(id_user));
					
					result.add(map);
				}
				
				PrintWriter pw = response.getWriter();
				pw.println(JSONValue.toJSONString(result));
				pw.close();
			}
			else
			{
				throw new Exception();
			}
		} catch(Exception e)
		{
			e.printStackTrace();
			
			PrintWriter pw = response.getWriter();
			pw.print("[]");
			pw.close();
		}
    }
    
    public void get_category(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
    	try
		{
			if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if (request.getParameter("id_kategori")!=null)
				{
					Category currentCat = (Category)Category.getModel().find("id_kategori = ?", new Object[]{Integer.parseInt(request.getParameter("id_kategori"))}, new String[]{"integer"}, null);
					
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("nama_kategori", currentCat.getNama_kategori());
					PrintWriter pw = response.getWriter();
					pw.println(new JSONObject(map).toJSONString());
					pw.close();
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
			
			PrintWriter pw = response.getWriter();
			pw.print("{}");
			pw.close();
		}
	}
    
    public void search_categories(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
    	try
		{
    		if ((request.getParameter("token")!=null) &&(request.getParameter("app_id")!=null) && ((GeneralHelper.isLogin(request.getParameter("token"), request.getParameter("app_id")))!=-1))
			{
				if ((request.getParameter("terms")!=null) && (request.getParameter("start")!=null))
				{
					List<DBSimpleRecord> list = Arrays.asList(Category.getModel().findAllLimit("nama_kategori LIKE ?", new Object[]{request.getParameter("terms")}, new String[]{"string"}, null, Integer.parseInt(request.getParameter("start")), 10));

					List<Map<String, Object>> ret = new ArrayList<Map<String,Object>>();
					Category[] categories = list.toArray(new Category[list.size()]);
					for (Category category : categories)
					{
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id_category", category.getId_kategori());
						map.put("category_name", category.getNama_kategori());
						
						ret.add(map);
					}
					
					PrintWriter pw = response.getWriter();
					pw.print(JSONValue.toJSONString(ret));
					pw.close();
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
			
			PrintWriter pw = response.getWriter();
			pw.print("[]");
			pw.close();
		}
	}
}
