package com.main;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.connection.SocketClient;

public class MOA_client 
{

	public static final SocketClient sc = new SocketClient("localhost", 25000);
	public static final Thread conn_thread = new Thread(sc);
	public static boolean logged_in;
	public static final byte[] public_key_bytes = new byte[] {48, 92, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, 75, 0, 48, 72, 2, 65, 0, -71, 113, -5, 95, 96, 39, -72, -1, -55, 72, -19, 125, 42, 73, 60, 58, 28, -113, -64, 74, 41, -17, -87, 22, 114, -52, -44, 52, 37, -38, 81, -108, -74, -50, 123, -43, 90, 118, -36, 7, 35, -36, -23, 71, -114, 127, -105, -114, 116, 112, -45, 62, -74, -21, -55, -83, -5, -56, 19, 49, -74, 37, 12, 61, 2, 3, 1, 0, 1};
	public static PublicKey public_key;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) 
		    {
		        if ("Nimbus".equals(info.getName())) 
		        {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		
		try
    	{
	        X509EncodedKeySpec spec = new X509EncodedKeySpec(MOA_client.public_key_bytes);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        MOA_client.public_key = keyFactory.generatePublic(spec);
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
		
		MainFrame.setDefaultLookAndFeelDecorated(true);
		new MainFrame();
	}

}
