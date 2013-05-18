package com.model;

public class Tag
{
    private int id_tag;
    private String tag_name;

    public Tag(int id_tag, String tag_name)
    {
    	this.id_tag = id_tag;
    	this.tag_name = tag_name;
    }
    
    /**
     * @return the id_tag
     */
    public int getId_tag() 
    {
        return id_tag;
    }

    /**
     * @param id_tag the id_tag to set
     */
    public void setId_tag(int id_tag) 
    {
        this.id_tag = id_tag;
    }

    /**
     * @return the tag_name
     */
    public String getTag_name() 
    {
        return tag_name;
    }

    /**
     * @param tag_name the tag_name to set
     */
    public void setTag_name(String tag_name) 
    {
        this.tag_name = tag_name;
    }
}
