package com.connection.message;

import java.util.Date;

public class MessageContainer 
{
    private static final String HEADER = "MOA";

    private static final int LOGIN_REQUEST = 0;
    private static final int LIST_TASK_REQUEST = 1;
    private static final int STATUS_REQUEST = 2;
    private static final int KEEP_ALIVE_REQUEST = 3;
    
    private static final int STATUS_SUCCESS = 5;
    private static final int STATUS_FAIL = 6;

    
    private static byte[] construct_message(int code, byte[] list)
    {
        byte[] ret = new byte[20+list.length];
        for (int i=0;i<HEADER.length();++i)
        {
            ret[i] = (byte)HEADER.charAt(i);
        }
        
        for (int i=0;i<19-HEADER.length();++i)
        {
            ret[HEADER.length()+i] = (byte)0;
        }
        
        ret[19] = (byte)code;
        for (int i=0;i<list.length;++i)
        {
        	ret[20+i] = list[i];
        }

        return ret;
    }

    public static byte[] construct_message_login(byte[] username, byte[] password)
    {
        byte[] list = new byte[username.length+password.length+2];
        list[0] = (byte) username.length;
        for (int i=0;i<username.length;++i)
        {
        	list[1+i] = username[i];
        }
        list[username.length+1] = (byte) password.length;
        for (int i=0;i<password.length;++i)
        {
        	list[username.length+2+i] = password[i];
        }
        
        return construct_message(LOGIN_REQUEST, list);
    }
    
    public static byte[] construct_message_list_task()
    { 
        return construct_message(LIST_TASK_REQUEST, new byte[0]);
    }
    
    public static byte[] construct_message_status(int idtugas, boolean status, Date timestamp)
    { 
    	String time = timestamp.toString();
    	byte[] list = new byte[3+time.length()];
        
    	list[0] = (byte) idtugas;
        if (status)
        	list[1] = (byte) '1';
        else
        	list[1] = (byte) '0';
        
        list[2] = (byte) time.length();
        for (int i=0;i<time.length();++i)
        {
        	list[3+i] = (byte)time.charAt(i);
        }
        
        return construct_message(STATUS_REQUEST, list);
    }
    
    public static byte[] construct_message_keep_alive()
    { 
        return construct_message(KEEP_ALIVE_REQUEST, new byte[0]);
    }
}