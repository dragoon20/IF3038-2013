package com.connection;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.account.LogBuilder;
import com.connection.message.MessageContainer;
import com.model.Category;
import com.model.Task;

public class SocketClient implements Runnable
{
    private Socket myClient = null;
    private InetAddress address = null;
    
    private static final long DELAY = 3000L;
    private int port = 0;

    private boolean connected;
    public boolean logged_in;
    public static final LogBuilder LB = new LogBuilder();

    private boolean LOCK;
    
    private String username;

    public SocketClient()
    {
    	connected = false;
    	LOCK = false;
    	logged_in = false;
    	username = "";
    }
    
    public SocketClient(String machinename, int port)
    {
        try
        {
        	connected = false;
        	LOCK = false;
        	logged_in = false;
        	username = "";
            address = InetAddress.getByName(machinename);
            this.port = port;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void timedOut() 
    { 
        synchronized(this)
        {
            try {
                notifyAll();
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    private void establishConnection()
    {
        try 
        {
            while(true)
            {
                try
                {
                    myClient = new Socket(address, port);
                }catch(ConnectException ignore){
                    //ignore connect exception first then check connection
                	ignore.printStackTrace();
                }
                
                if(myClient==null)
                {
                    //try connecting again ever 5s
                    System.out.println("Trying to connect server...");
                    new Timer(true).schedule(new TimerTask() {
                        @Override
                        public void run() {  
                            timedOut();  
                        }  
                    },  
                    DELAY);
                    try{
                        synchronized (this){
                            this.wait();
                        }
                    } catch(InterruptedException  ignore) {
                        //ignore again
                    	ignore.printStackTrace();
                    }  
                } else
                {
                    //connection has been established !
                    myClient.setSoTimeout(5000);
                    connected = true;
                    while(connected)
                    {
                    	while (LOCK) ;
                    	LOCK = true;
                        PrintWriter out = null;
                        out = new PrintWriter(myClient.getOutputStream());
                        out.println("papa I'm alive");
                        out.flush();

                        Scanner in = new Scanner(myClient.getInputStream());

                        if (in.hasNextLine())
                        {
                            String response = in.nextLine();
                            if (response.equalsIgnoreCase("okay son"))
                            {
//                            	System.out.println("Server still alive");
                            }
                        }
                        else
                        {
                        	connected=false;
                            myClient = null;
                            System.out.println("Connection closed!");   
                        }
                        LOCK = false;
                        
                        new Timer(true).schedule(new TimerTask() {  
                            @Override
                            public void run() {  
                                timedOut();  
                            }  
                        },  
                        DELAY);
                        
                        try{
                            synchronized (this){
                                this.wait();
                            }
                        } catch(InterruptedException  ignore) {
                            //ignore again
                        	ignore.printStackTrace();
                        }
                        
                        if (logged_in)
                        {
                        	try
	                        {
	                        	LB.parse_logs();
	                        }
	                        catch (Exception e)
	                        {
	                        	e.printStackTrace();
	                        }
                        	
	                        while ((LB.size() > 0) && (connected))
	                    	{
	                        	while (LOCK) System.out.println("Dead");
	                        	LOCK = true;
	                            
	                        	System.out.println("Syncing status with id="+LB.get(0).getIdtugas()+" status="+LB.get(0).getStatustugas());
		                        String message = MessageContainer.construct_message_status(LB.get(0).getIdtugas(), LB.get(0).getStatustugas(), LB.get(0).getWaktuperubahan());
			            		PrintWriter out2 = new PrintWriter(myClient.getOutputStream());
			    	            out2.println(message);
			    	            out2.flush();
			    	            System.out.println("Message sent to the server : "+ message);
			    	            
			    	            Scanner in2 = new Scanner(myClient.getInputStream());
			    	            if (in2.hasNextLine())
			                    {
			                        String response = in2.nextLine();
			                        System.out.println("Server : "+response);
			                        if (response.equals("success"))
			                        {
			                        	LB.delete_log(0);
			                        	LB.save_logs();
			                        }
			                    }
			    	            LOCK = false;
			    	            
			    	            new Timer(true).schedule(new TimerTask() {  
		                            @Override
		                            public void run() {  
		                                timedOut();  
		                            }  
		                        },  
		                        DELAY);
			    	            
		                        try{
		                            synchronized (this){
		                                this.wait();
		                            }
		                        } catch(InterruptedException  ignore) 
		                        {
		                            //ignore again
		                        	ignore.printStackTrace();
		                        }
		                        
		                        try
		                        {
		                        	LB.parse_logs();
		                        }
		                        catch (Exception e)
		                        {
		                        	e.printStackTrace();
		                        }
	                    	}
                        }
                    }
                    logged_in = false;
                }
            }
        } catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public boolean doLogin(String username, String password)
    {
        try
        {
        	if (logged_in)
        	{
        		return true;
        	}
        	else if (connected)
        	{
        		while (LOCK);
        		LOCK = true;
	            String sendMessage = MessageContainer.construct_message_login(username, password);
	            PrintWriter out = new PrintWriter(myClient.getOutputStream());
	            out.println(sendMessage);
	            out.flush();
	            System.out.println("Message sent to the server : "+ sendMessage);
	            
	            Scanner in = new Scanner(myClient.getInputStream());
	            if (in.hasNextLine())
                {
                    String response = in.nextLine();
                    System.out.println("Server : "+response);
                    if (response.equalsIgnoreCase("login success"))
                    {
                    	LOCK = false;
                    	logged_in = true;
                    	return true;
                    }
                    else
                    {
                    	LOCK = false;
                    	return false;
                    }
                }
	            else
	            {
	            	LOCK = false;
	            	return false;
	            }
        	}
        	else
        	{
        		return false;
        	}
        }
        catch (Exception exception) 
        {
        	LOCK = false;
            exception.printStackTrace();
            return false;
        }
    }
    
    public List<Category> doList()
    {
        try
        {
        	if (connected)
        	{
        		while (LOCK);
        		LOCK = true;
	            String sendMessage = MessageContainer.construct_message_list_task(username);
	            PrintWriter out = new PrintWriter(myClient.getOutputStream());
	            out.println(sendMessage);
	            out.flush();
	            System.out.println("Message sent to the server : "+ sendMessage);
	            
	            Scanner in = new Scanner(myClient.getInputStream());
	            if (in.hasNextLine())
                {
                    String response = in.nextLine();
                    System.out.println("Server : "+response);
                	LOCK = false;
                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                	List<Category> ret = new ArrayList<Category>();
                	JSONArray categories = (JSONArray)JSONValue.parse(response);
                	for (Object temp : categories)
                	{
                		JSONObject obj = (JSONObject)temp;
                		Category c = new Category(Integer.parseInt(obj.get("id_kategori").toString()), (String)obj.get("nama_kategori"));
                		JSONArray tasks = (JSONArray)obj.get("tasks");
                		for (Object temp_obj : tasks)
                		{
                			JSONObject obj2 = (JSONObject)temp_obj;
                			Task t = new Task(Integer.parseInt(obj2.get("id_task").toString()), (String)obj2.get("nama_task"), (Boolean)obj2.get("status"), sdf.parse((String)obj2.get("deadline")));
                			c.add(t);
                		}
                		ret.add(c);
                	}
                	return ret;
                }
	            else
	            {
	            	LOCK = false;
	            	return null;
	            }
        	}
        	else
        	{
        		return null;
        	}
        }
        catch (Exception exception) 
        {
        	LOCK = false;
            exception.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void run()
    {
        establishConnection();
    }
}
