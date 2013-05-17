/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import java.io.*;
import java.net.*;
import java.util.logging.Mapt java.util.logging.Level;
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
        }cSystem.out.println("L "+Line        }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */----Function----*/
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

        return response;ine arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        SocketServer server = new SocketServer(60000);
        server.doReceive();
    }
}
