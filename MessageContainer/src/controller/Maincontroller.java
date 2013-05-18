/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import messagecontainer.MessageContainer;
import messageparser.MessageParser;

/**
 *
 * @author Adriel
 */
public class Maincontroller {
    
    /**
     * @param args the command line arguments
     */
    public static void write_per_char(String s)
    {
        for (int i = 0; i < s.length(); i++)
        {
            System.out.print((int)s.charAt(i) + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args)
    {
        MessageContainer mc = new MessageContainer();
        //String message = mc.construct_message_login("adriel", "blablabla");
        //String message = mc.construct_message_list_task("adriel");
        String message = mc.construct_message_status("adriel", 20);
        write_per_char(message);
        
        MessageParser mp = new MessageParser();
        mp.parse_message(message);
        System.out.println("Username = " + mp.getUsername() + " Password = " + mp.getPassword() + " ID Tugas = " + mp.getIdtugas());
    }
}
