package com.connection;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.connection.message.MessageContainer;

public class SocketClient 
{
    private Socket myClient = null;
    private InetAddress address = null;
    private OutputStream os = null;
    private OutputStreamWriter osw = null;
    private BufferedWriter bw = null;
    private static final long DELAY = 20000L;
    private int port = 0;
    
    public SocketClient()
    {
    }
    
    public SocketClient(String machinename, int port)
    {
        try{
            address = InetAddress.getByName(machinename);
            this.port = port;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void timedOut() 
    { 
        synchronized(this){
            try {
                notifyAll();
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    public void establishConnection()
    {
        try {
            while(true){
                try{
                    myClient = new Socket(address, port);
                }catch(ConnectException ignore)
                {
                    //ignore connect exception first then check connection
                }
                if(myClient==null)
                {
                    //try connecting again ever 5s
                    System.out.println("Trying to connect server...");
                    new Timer(true).schedule(new TimerTask() {  
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
                    }  
                }else{
                    //connection has been established !
                    os = myClient.getOutputStream();
                    osw = new OutputStreamWriter(os);
                    bw = new BufferedWriter(osw);
                    
                    break;
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void closeClient(){
        try{
            myClient.close();
        }catch(IOException e){
        }
    }
    
    public void doLogin(String username, String password)
    {
        try
        {
            String sendMessage = MessageContainer.construct_message_login(username, password);
            bw.write(sendMessage);
            bw.flush();
            System.out.println("Message sent to the server : "+ sendMessage);
        }
        catch (Exception exception) 
        {
            exception.printStackTrace();
        }
        finally
        {
            //Closing the socket
            try
            {
                myClient.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
