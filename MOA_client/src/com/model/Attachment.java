package com.model;

public class Attachment
{
	private int id_task;
	private String attachment;

	public Attachment(int id_task, String attachment)
	{
		this.id_task = id_task;
		this.attachment = attachment;
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
     * @return the attachment
     */
    public String getAttachment() 
    {
        return attachment;
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(String attachment) 
    {
    	this.attachment = attachment;
    }
}
