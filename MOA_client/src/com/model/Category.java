package com.model;

import java.util.ArrayList;
import java.util.List;

public class Category
{
    private int id_kategori;
    private String nama_kategori;
    private List<Task> list_task;
    
    public Category(int id_kategori, String nama_kategori)
    {
    	this.id_kategori = id_kategori;
    	this.nama_kategori = nama_kategori;
    	this.list_task = new ArrayList<Task>();
    }
    
    public void add(Task task)
    {
    	list_task.add(task);
    }
    
    public Task remove(int index)
    {
    	return list_task.remove(index);
    }
    
    /**
     * @return the id_kategori
     */
    public int getId_kategori() 
    {
        return id_kategori;
    }

    /**
     * @param id_kategori the id_kategori to set
     */
    public void setId_kategori(int id_kategori) 
    {
        this.id_kategori = id_kategori;
    }

    /**
     * @return the nama_kategori
     */
    public String getNama_kategori() 
    {
        return nama_kategori;
    }

    /**
     * @param nama_kategori the nama_kategori to set
     */
    public void setNama_kategori(String nama_kategori) 
    {
        this.nama_kategori = nama_kategori;
    }

	public List<Task> getList_task() 
	{
		return list_task;
	}

	public void setList_task(List<Task> list_task) 
	{
		this.list_task = list_task;
	}
}
