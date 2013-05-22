/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package socketserver;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import messageparser.MessageParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Vincentius Martin
 */
public class ClientThread extends Thread{
    
    //Input
    
    private Socket ClientSocket = null;
    private int ID = -1;
    public MessageParser parser = new MessageParser();
    public static final String APP_ID = "38822580b406c4af9d52390efc237db0";
    private String token = "";
    
    public ClientThread(Socket ClientSocket){
        this.ClientSocket = ClientSocket;
        ID = ClientSocket.getPort();
        System.out.println("Created new client connection with ID : "+ID);
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
                Scanner in = new Scanner(ClientSocket.getInputStream());
                
                String input;
                if((in!=null) && (in.hasNextLine())){
                    
                    input = input = in.nextLine();
                    System.out.println("Message received from client is : "+ input);
                    
                    if(input.equalsIgnoreCase("papa I'm alive")){
                        PrintWriter out = new PrintWriter(ClientSocket.getOutputStream());
                        out.println("okay son");
                        out.flush();
                    }else{      
                        switch((int)input.charAt(19))
                        {
                            case 0 :
                                parser.parse_login_request(input);
                                String loginresult = callDatabaseLogin(parser.getUsername(), parser.getPassword());
                                System.out.println("result "+loginresult);
                                JSONObject ret = (JSONObject)JSONValue.parse(loginresult);
                                System.out.println("ret "+ret);
                                if((boolean)ret.get("status")){
                                    token = (String)ret.get("token");
                                    PrintWriter out = new PrintWriter(ClientSocket.getOutputStream());
                                    out.println("Server : loginsuccess");
                                    out.flush();
                                }else{
                                    PrintWriter out = new PrintWriter(ClientSocket.getOutputStream());
                                    out.println("Server : loginfail");
                                    out.flush();
                                }
                            break;
                        }
                    }
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
    
    public String callDatabaseLogin(String username, String password){
        String out = "";
        try {
            HashMap<String, String> param = new HashMap<String, String>();
            param.put("app_id", APP_ID);
            param.put("username",username);
            param.put("password",password);
            out = callRestfulWebService("http://localhost:8080/MOAServices//token/login", param, "", 0);
        } catch (Exception ex) {
           ex.printStackTrace();
        }
        return out;
    }
}
