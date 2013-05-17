/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class SocketServer {

    ServerSocket MyService;
    Socket ServiceSocket = null;
    DataInputStream AccInput;
    PrintStream DoOutput;
    
    public SocketServer(int port){
        try {
            MyService = new ServerSocket(port);
            ServiceSocket = MyService.accept();
            AccInput = new DataInputStream(ServiceSocket.getInputStream());
            DoOutput = new PrintStream(ServiceSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void closeServer(){
        try{
            MyService.close();
            ServiceSocket.close();
            AccInput.close();
            DoOutput.close();
        }catch(IOException e){
        }
    }
    
    private void doReceive(){
        String Line;
        try{
            while(true){
                Line = AccInput.readLine();
                DoOutput.println(Line);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SocketServer server = new SocketServer(60000);
        server.doReceive();
    }
}
