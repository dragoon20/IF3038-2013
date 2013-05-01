/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;


/**
 *
 * @author Abraham Krisnanda
 */
public class Attachment extends DBSimpleRecord {
    /*private int id_task;
    private String attachment;*/
    private static Attachment model;
    public static Attachment getModel() 
    {
        if (model==null)
        {
            model = new Attachment();
        }
        return model;
    }
    
    @Override
    protected String GetClassName() 
    {
        return "models.Attachment";
    }
    
    @Override
    protected String GetTableName() 
    {
    	return "task_attachment";
    }
    
    public static String getTableName()
    {
    	return "task_attachment";
    }
    
    /**
     * @return the id_task
     */
    public int getId_task() {
        return (Integer)data.get("id_task");
    }

    /**
     * @param id_task the id_task to set
     */
    public void setId_task(int id_task) {
        data.put("id_task", id_task);
    }

    /**
     * @return the attachment
     */
    public String getAttachment() {
        return ((String)data.get("attachment"));
    }

    /**
     * @param attachment the attachment to set
     */
    public void setAttachment(String attachment) {
        data.put("attachment", attachment);
    }
}
