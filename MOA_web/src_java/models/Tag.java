/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;


/**
 *
 * @author Abraham Krisnanda
 */
public class Tag extends DBSimpleRecord{
    /*private int id_tag;
    private String tag_name;*/
    private static Tag model;
    public static Tag getModel() 
    {
        if (model==null)
        {
            model = new Tag();
        }
        return model;
    }
    
    @Override
    protected  String GetClassName() 
    {
        return "models.Tag";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "tag";
    }
    
    public static String getTableName()
    {
    	return "tag";
    }
    
    /**
     * @return the id_tag
     */
    public int getId_tag() {
        return (Integer)data.get("id_tag");
    }

    /**
     * @param id_tag the id_tag to set
     */
    public void setId_tag(int id_tag) {
        data.put("id_tag",id_tag);
    }

    /**
     * @return the tag_name
     */
    public String getTag_name() {
        return ((String)data.get("tag_name"));
    }

    /**
     * @param tag_name the tag_name to set
     */
    public void setTag_name(String tag_name) {
        data.put("tag_name", tag_name);
    }
}
