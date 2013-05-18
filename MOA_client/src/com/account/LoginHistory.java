package com.account;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class LoginHistory 
{
    private String username;
    private String password;
    
    public LoginHistory()
    {
        username = "";
        password = "";
    }
    
    public LoginHistory(String user, String pass)
    {
        username = user;
        password = pass;
    }
    
    public boolean parse_xml() throws FileNotFoundException, IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("loginhistory.txt"));
        
        String line;
        boolean ret = false;
        try
        {  
            line = br.readLine();
            if (line != "\0")
            {
	            username = line.substring(line.indexOf("<username>") + 10, line.indexOf("</username>"));
	            
	            line = br.readLine();
	            password = line.substring(line.indexOf("<password>") + 10, line.indexOf("</password>"));
	            ret = true;
            }
        }
        catch (Exception e)
        {
            System.out.println("error : " + e);
            username = "";
            password = "";
        }        
        
        br.close();
        return ret;
    }

    public void produce_xml() throws IOException
    {
        OutputStreamWriter osw = new FileWriter("loginhistory.txt");
        
        String s = "";
        s += "<username>" + username + "</username>\n";
        s += "<password>" + password + "</password>";
        osw.write(s);
        
        osw.close();
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
