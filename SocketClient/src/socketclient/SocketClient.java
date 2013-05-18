/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class SocketClient {

    private Socket myClient = null;
    private InetAddress address = null;
    private OutputStream os = null;
    private OutputStreamWriter osw = null;
    private BufferedWriter bw = null;
    private static final long DELAY = 20000L;
    private int port = 0;
    
    public SocketClient(){
    }
    
    public SocketClient(String machinename, int port){
        try{
            address = InetAddress.getByName(machinename);
            this.port = port;
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void timedOut() { 
        synchronized(this){
            try {
                notifyAll();
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }  
    
    public void establishConnection(){
        try {
            while(true){
                try{
                    myClient = new Socket(address, port);
                }catch(ConnectException ignore){
                    //ignore connect exception first then check connection
                }
                if(myClient==null){
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
            /*System.out.println("aaa");
            myClient.connect(new InetSocketAddress(address, port));
            System.out.println("bbb");
            //Send the message to the server
            while(myClient.isConnected()==false){
                System.out.println("Waiting for a connection...");
            }
            os = myClient.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            */
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
    
    public void doLogin(String username, String password){
        try
        {
            List<Byte> msgloginlist = messagecontainer.MessageContainer.construct_message_login(username, password);
            byte[] msglogin = new byte[msgloginlist.size()];
            for(int i = 0; i<msgloginlist.size(); i++)
            {
                msglogin[i] = msgloginlist.get(i);
            }
 
            String sendMessage = new String(msglogin);
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
        }/*
        try {
            
            //DoOutput.write(msglogin);
            //DoOutput.wr
            //DoOutput.writeBytes("HELO\n");
            
            Bw.write(new String(msglogin));
            Bw.flush();
            
            System.out.println(new String(msglogin));
            
//            String responseLine;
//            while((responseLine = AccInput.readLine()) != null){
//                System.out.println("Server: " + responseLine);
//                if (responseLine.indexOf("Ok") != -1) {
//                 break;
//                }
//            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }*/
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        SocketClient sc = new SocketClient("localhost", 25000);
        sc.establishConnection();
        sc.doLogin("martin", "akupadamu");
    }
}
