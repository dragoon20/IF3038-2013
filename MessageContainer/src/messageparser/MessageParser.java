/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package messageparser;

/**
 *
 * @author Adriel
 */
public class MessageParser {
    private static final int LOGIN_REQUEST = 0;
    private static final int LIST_REQUEST = 1;
    private static final int STATUS_REQUEST = 2;
    private static final int REQUEST_CODE_INDEX = 19;
    private static final int CONTENT_STARTING_INDEX = 20;
    
    private String username = ""; 
    private String password = "";
    private byte idtugas = -1;
    
    public void parse_message (String message)
    {
        String header = "MOA";
        if (check_same_message(message, header, 0, header.length()))
        {
            int request_code = get_request_code(message);
            if (request_code == LOGIN_REQUEST)
            {
                // kode untuk parse username dan password
                parse_login_request(message);
                
                // kode untuk mengecek login
                
            }
            else if (request_code == LIST_REQUEST)
            {
                // kode untuk parse username
                parse_list_request(message);
                
                // kode untuk menampilkan list
                
            }
            else if (request_code == STATUS_REQUEST)
            {
                // kode untuk parse ID tugas
                parse_status_request(message);
                
                // kode untuk mengupdate status
                
            }
        }
        else
        {
        }
    }
    
    public void parse_login_request (String message)
    {
        username = "";
        password = "";
        int uLength = (int)message.charAt(CONTENT_STARTING_INDEX);
        for (int i = CONTENT_STARTING_INDEX + 1; i < CONTENT_STARTING_INDEX + 1 + uLength ; i++)
        {
            username += message.charAt(i);
        }

        int PASSWORD_STARTING_INDEX = CONTENT_STARTING_INDEX + 1 + uLength;
        int pLength = (int)message.charAt(PASSWORD_STARTING_INDEX);
        for (int i = PASSWORD_STARTING_INDEX + 1; i < PASSWORD_STARTING_INDEX + 1 + pLength ; i++)
        {
            password += message.charAt(i);
        }
    }
    
    public void parse_list_request (String message)
    {
        username = "";
        int uLength = (int)message.charAt(CONTENT_STARTING_INDEX);
        for (int i = CONTENT_STARTING_INDEX + 1; i < CONTENT_STARTING_INDEX + 1 + uLength ; i++)
        {
            username += message.charAt(i);
        }
    }
    
    public void parse_status_request (String message)
    {
        username = "";
        int uLength = (int)message.charAt(CONTENT_STARTING_INDEX);
        for (int i = CONTENT_STARTING_INDEX + 1; i < CONTENT_STARTING_INDEX + 1 + uLength ; i++)
        {
            username += message.charAt(i);
        }

        int IDTUGAS_STARTING_INDEX = CONTENT_STARTING_INDEX + 1 + uLength;
        idtugas = (byte)message.charAt(IDTUGAS_STARTING_INDEX);
    }
    
    public int get_request_code (String message)
    {
        String header = "MOA";
        if (check_same_message(message, header, 0, header.length()))
        {
            return message.charAt(REQUEST_CODE_INDEX);
        }
        else
        {
            return -1;
        }
    }

    public boolean check_same_message (String list, String str, int beginIndex, int endIndex)
    {
        boolean found = true;
        for (int i = beginIndex; i < endIndex; i++)
        {
            if (list.charAt(i) != str.charAt(i))
            {
                found = false;
            }
        }
        return found;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String s)
    {
        username = s;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String s)
    {
        password = s;
    }
    
    public byte getIdtugas()
    {
        return idtugas;
    }
    
    public void setIdtugas(Byte b)
    {
        idtugas = b;
    }
}
