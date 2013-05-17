/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messagecontainer;

/**
 *
 * @author User
 */
public class Task {
    public int taskid;
    public String taskname;
    
    public Task(int taskid, String taskname)
    {
        this.taskid = taskid;
        this.taskname = taskname;
    }
    
    public void settaskid(int taskid)
    {
        this.taskid = taskid;
    }
    
    public void settaskname(String taskname)
    {
        this.taskname = taskname;
    }
    
    public int gettaskid()
    {
        return taskid;
    }
    
    public String gettaskname()
    {
        return taskname;
    }
}
