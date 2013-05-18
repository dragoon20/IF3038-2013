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

    
    private static String construct_message(int code, String list)
    {
        String ret = "";
        for (int i=0;i<HEADER.length();++i)
        {
            ret += HEADER.charAt(i);
        }
        
        for (int i=0;i<19-HEADER.length();++i)
        {
            ret += (char)0;
        }
        
        ret += (char)code;
        ret += list;

        return ret;
    }

    public static String construct_message_login (String username, String password)
    {
        String data = "";
        
        data += (char)username.length();
        data += username;
        data += (char)password.length();
        data += password;
        
        return construct_message(LOGIN_REQUEST, data);
    }
    
    public static String construct_message_list_task (String username)
    { 
        String data = "";
        
        data += (char)username.length();
        data += username;
        
        return construct_message(LIST_TASK_REQUEST, data);
    }
    
    public static String construct_message_status (String username, int idtugas)
    { 
        String data = "";
        
        data += (char)username.length();
        data += username;
        data += (char)idtugas;
        
        return construct_message(STATUS_REQUEST, data);
    }
            
}
