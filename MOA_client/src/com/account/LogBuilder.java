package com.account;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogBuilder 
{
	private List<Log> logs = new ArrayList<>();
	private static final String log_path = "logs.txt";
	
    public Log get(int i)
    {
    	return logs.get(i);
    }
    
    public int size()
    {
    	return logs.size();
    }
    
    public LogBuilder()
    {
        try {
            parse_logs();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LogBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LogBuilder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(LogBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void add_log(byte id, String nama, boolean status)
    {
        Log newlog = new Log(id, nama, status);
        System.out.println("Added log with id="+id+" name="+nama+" status="+status);
        logs.add(newlog);
    }
    
    public void add_log(byte id, String nama, boolean status, Date date)
    {
        Log newlog = new Log(id, nama, status, date);
        logs.add(newlog);
    }
    
    public void delete_log(int i)
    {
        logs.remove(i);
    }
    
    public void delete_all_log()
    {
    	logs.removeAll(logs);
    }
    
    public void delete_last_log()
    {
        logs.remove(logs.size()-1);
    }
    
    public String construct_log_message(Log log)
    {
        String s = "";
        s += (int)log.getIdtugas() + "-\t\t" + log.getNamatugas() + "-\t\t" + log.getStatustugas() + "-\t\t" + log.getWaktuperubahan().toString();
        return s;
    }
    
    public void write_logs()
    {
        for (int i = 0; i < logs.size(); i++)
        {
            System.out.print(construct_log_message(logs.get(i)) + "\n");
        }
    }
    
    public void save_logs() throws IOException
    {
        OutputStreamWriter osw = new FileWriter("logs.txt");
        for (int i = 0; i < logs.size(); i++)
        {
            osw.write(construct_log_message(logs.get(i)) + "\n");
        }
        osw.flush();
        osw.close();
    }
    
    public void parse_logs() throws FileNotFoundException, IOException, ParseException
    {
    	delete_all_log();
        BufferedReader br = new BufferedReader(new FileReader(log_path));
        String line;
        while ((line = br.readLine()) != null)
        {
            // parse id tugas
            int i = 0;
            String temp = "";
            boolean flag = false;
            while (i < line.length() && flag == false)
            {
                temp += line.charAt(i);
                i++;
                if (line.charAt(i) == '-' && line.charAt(i+1) == '\t' && line.charAt(i+2) == '\t')
                {
                    flag = true;
                }
            }
            i += 3;
            byte idtugas = (byte) Integer.parseInt(temp);
            
            // parse id tugas
            temp = "";
            flag = false;
            while (i < line.length() && flag == false)
            {
                temp += line.charAt(i);
                i++;
                if (line.charAt(i) == '-' && line.charAt(i+1) == '\t' && line.charAt(i+2) == '\t')
                {
                    flag = true;
                }
            }
            i += 3;
            String namatugas = temp;
            
            // parse id tugas
            temp = "";
            flag = false;
            while (i < line.length() && flag == false)
            {
                temp += line.charAt(i);
                i++;
                if (line.charAt(i) == '-' && line.charAt(i+1) == '\t' && line.charAt(i+2) == '\t')
                {
                    flag = true;
                }
            }
            i += 3;
            boolean statustugas = Boolean.parseBoolean(temp);
            
            // parse id tugas
            temp = "";
            flag = false;
            while (i < line.length() && flag == false)
            {
                temp += line.charAt(i);
                i++;
                if (i < line.length() && line.charAt(i) == '-' && line.charAt(i+1) == '\t' && line.charAt(i+2) == '\t')
                {
                    flag = true;
                }
            }
            i += 3;
            DateFormat format;
            format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            Date waktuperubahan = format.parse(temp);
            
            add_log(idtugas, namatugas, statustugas, waktuperubahan);
        }
        br.close();
    }
}
