*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class SocketServer {

    private ServerSocket Server = null;
    private Socket ServiceSocket = null;
    private InputStream is = null;
    private InputStreamReader isr = null;
    private BufferedReader br = null;
    
    public SocketServer(int port){
        try {
             Server = new ServerSocket(port);
             ServiceSocket = Server.accept();
             is = ServiceSocket.getInputStream();
             isr = new InputStreamReader(is);
             br = new BufferedReader(isr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void closeServer(){
        try{
            ServiceSocket.close();
        }catch(IOException e){
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
                    System.out.println("Message received from client is : "+input);
                }
                
            }
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                ServiceSocket.close();
            }
            catch(Exception e){}
        }
    }
    
    /*----Function----*/
    private static String buildWebQuery(Map<String, String> parameters) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = URLEncoder.encode(entry.getKey(), "UTF-8");
            String value = URLEncoder.encode(entry.getValue(), "UTF-8");
            sb.append(key).append("=").append(value).append("&");
        }
        return sb.toString().substring(0, sb.length() - 1);
    }
    
    public static String callRestfulWebService(String address, Map<String, String> parameters, String proxy, int port) throws Exception 
    {
        Proxy proxyObject = null;
        if (!proxy.equals("") && port > 0) 
        {
            InetSocketAddress proxyAddress = new InetSocketAddress(proxy, port);
            proxyObject = new Proxy(Proxy.Type.HTTP, proxyAddress);
        }

        String response = "";
        String query = buildWebQuery(parameters);

        URL url = new URL(address);
        
        URLConnection urlc = null;
        if (proxyObject == null) {
            urlc = url.openConnection();
        } else {
            urlc = url.openConnection(proxyObject);
        }
        urlc.setDoOutput(true);
        urlc.setAllowUserInteraction(false);

        // Send message
        PrintStream ps = new PrintStream(urlc.getOutputStream());
        ps.print(query);
        ps.close();

        // retrieve result
        BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        response = sb.toString();

        return response;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SocketServer SR = new SocketServer(25000);
        SR.doReceive();
    }
}
=======
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

i