/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketclient;

import java.io.*;
import java.net.*;
import java.util.List;
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
    
    private SocketClient(){
    }
    
    private SocketClient(String machinename, int port){
        try{
            address = InetAddress.getByName(machinename);
            myClient = new Socket(address, port);
            //Send the message to the server
            os = myClient.getOutputStream();
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    private void closeClient(){
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
            int usernamelength = username.length();
            int passwordlength = password.length();
 
            String sendMessage = new String(msglogin);
            bw.write(sendMessage);
            bw.flush();
            System.out.println("Message sent to the server : "+sendMessage);

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
        sc.doLogin("martin", "akupadamu");
    }
}
