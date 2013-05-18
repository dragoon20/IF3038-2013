/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Vincentius Martin
 */
public class ClientThread extends Thread{
    private InputStream is = null;
    private InputStreamReader isr = null;
    private BufferedReader br = null;
    private Socket ClientSocket = null;
    private int ID = -1;
    
    public ClientThread(Socket ClientSocket){
        this.ClientSocket = ClientSocket;
        ID = ClientSocket.getPort();
        System.out.println("Created new client connection with ID : "+ID);
    }
    
    public void open() throws IOException{
        is = ClientSocket.getInputStream();
        isr = new InputStreamReader(is);
        br = new BufferedReader(isr);
    }
    
    public void close() throws IOException{
        if(ClientSocket!=null){
            ClientSocket.close();
        }
    }
    
    private void doReceive(){
        try
        {
            while(true) 
            {
                //Reading the message from the client
                String input = br.readLine();
                if(input!=null){
                    System.out.println("Message received from client is : "+ input);
                }
                
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Transaction Finished !!");
        }
    }
    
    @Override
    public void run(){
        doReceive();
    }
}
