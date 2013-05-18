package com.model;

import java.util.Date;
import java.util.List;

public class Task 
{
    private int id_task;
    private String nama_task;
    private boolean status;
    private Date deadline;
    private List<Tag> list_tag;
    private List<Attachment> list_attachment;
    
    public Task(int id_task, String nama_task, boolean status, Date deadline)
    {
    	this.id_task = id_task;
    	this.nama_task = nama_task;
    	this.status = status;
    	this.deadline = deadline;
    }
    
    public void add_tag(Tag tag)
    {
    	list_tag.add(tag);
    }
    
    public Tag remove_tag(int index)
    {
    	return list_tag.remove(index);
    }
    
    public void add_attachment(Attachment attachment)
    {
    	list_attachment.add(attachment);
    }
    
    public Attachment remove_attachment(int index)
    {
    	return list_attachment.remove(index);
    }
    
    /**
     * @return the id_task
     */
    public int getId_task() 
    {
        return id_task;
    }

    /**
     * @param id_task the id_task to set
     */
    public void setId_task(int id_task) 
    {
        this.id_task = id_task;
    }

    /**
     * @return the nama_task
     */
    public String getNama_task() 
    {
        return nama_task;
    }

    /**
     * @param nama_task the nama_task to set
     */
    public void setNama_task(String nama_task) 
    {
        this.nama_task = nama_task;
    }

    /**
     * @return the status
     */
    public boolean getStatus() 
    {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) 
    {
        this.status = status;
    }

    /**
     * @return the deadline
     */
    public Date getDeadline() 
    {
        return deadline;
    }

    /**
     * @param deadline the deadline to set
     */
    public void setDeadline(Date deadline) 
    {
        this.deadline = deadline;
    }

	public List<Tag> getList_tag() 
	{
		return list_tag;
	}

	public void setList_tag(List<Tag> list_tag) 
	{
		this.list_tag = list_tag;
	}

	public List<Attachment> getList_attachment() 
	{
		return list_attachment;
	}

	public void setList_attachment(List<Attachment> list_attachment) 
	{
		this.list_attachment = list_attachment;
	}
}
