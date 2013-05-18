/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messagecontainer;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class MessageContainer {
    
    private static final String HEADER = "MOA";

    private static final int LOGIN_REQUEST = 0;
    private static final int LIST_TASK_REQUEST = 1;
    private static final int STATUS_REQUEST = 2;
    
    private static final int STATUS_SUCCESS = 5;
    private static final int STATUS_FAIL = 6;

    
    private static List<Byte> construct_message(int code,List<Byte> list)
    {
        List<Byte> ret = new ArrayList<>();
        for (int i=0;i<HEADER.length();++i)
        {
            ret.add((byte)HEADER.charAt(i));
        }
        
        for (int i=0;i<19-HEADER.length();++i)
        {
            ret.add((byte)0);
        }
        
        ret.add((byte)LOGIN_REQUEST);
        ret.addAll(list);

        return ret;
    }

    public static List<Byte> construct_message_login (String username, String password)
    {
        List<Byte> data = new ArrayList<Byte>();
        
        data.add((byte)username.length());
        System.out.println("asd " + (byte)username.length());
        
        for(int i = 0; i<username.length(); i++)
        {
            data.add((byte)username.charAt(i));
        }
        
        data.add((byte)password.length());
        
        for(int i = 0; i<password.length(); i++)
        {
            data.add((byte)password.charAt(i));
        }
        
        return construct_message(LOGIN_REQUEST, data);
    }
    
    public static List<Byte> construct_message_task (List<Task> ltask)
    { 
        List<Byte> data = new ArrayList<Byte>();
        
        for(int i = 0; i<ltask.size(); i++){
            data.add((byte)ltask.get(i).gettaskid());
        }
        
        return construct_message(LOGIN_REQUEST, data);
    }
    
    public static List<Byte> construct_message_status (List<Task> ltask)
    { 
        List<Byte> data = new ArrayList<Byte>();
        
        for(int i = 0; i<ltask.size(); i++){
            data.add((byte)ltask.get(i).gettaskid());
        }
        
        return construct_message(LOGIN_REQUEST, data);
    }
            
}
