package socketserver;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Adriel
 */
public class Test {
    private static final int LOGIN_REQUEST = 0;
    private static final int LIST_REQUEST = 1;
    private static final int STATUS_REQUEST = 2;
    
    private String username; 
    private String password;
    
    public void parse_message (List<Byte> message)
    {
        String header = "MOA";
        if (check_same_message(message, header, 0, header.length()))
        {
            byte request_code = message.get(19);
            if (request_code == (byte)LOGIN_REQUEST)
            {
                // kode untuk parse username dan password
                int uLength = 0; // isi dengan panjang username
                for (int i = 20; i < 20 + uLength ; i++)
                {
                    username += message.get(i);
                }
                
                int pLength = 0; // isi dengan panjang password
                for (int i = 20 + uLength; i < 20 + uLength + pLength ; i++)
                {
                    password += message.get(i);
                }
                
                // kode untuk mengecek login
                
            }
            else if (request_code == (byte)LIST_REQUEST)
            {
                // kode untuk menampilkan list
                
            }
            else if (request_code == (byte)STATUS_REQUEST)
            {
                // kode untuk parse ID tugas
                
                
                // kode untuk mengupdate status
                
            }
        }
        else
        {
        }
    }
    
    public int check_message (List<Byte> message)
    {
        String header = "MOA";
        if (check_same_message(message, header, 0, header.length()))
        {
            return message.get(19);
        }
        else
        {
            return -1;
        }
    }

    public boolean check_same_message (List<Byte> list, String str, int beginIndex, int endIndex)
    {
        boolean found = true;
        for (int i = beginIndex; i < endIndex; i++)
        {
            if (list.get(i) != (byte)str.charAt(i))
            {
                found = false;
            }
        }
        return found;
    }
}
