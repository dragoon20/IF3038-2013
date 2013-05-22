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
	public static final byte[] public_key_bytes = new byte[] {48, 92, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, 75, 0, 48, 72, 2, 65, 0, -90, -119, 40, 41, 110, 114, 24, 87, 80, 123, 104, -88, -118, 40, -106, 53, 68, -113, -65, 27, -21, -109, -71, 101, 89, 25, 53, -73, 127, 7, 80, -24, 39, -54, 0, -40, 0, 45, -24, -27, 109, -128, 122, -2, -3, 32, 67, 33, 117, -25, 78, -94, 55, 30, 33, 108, 41, 72, -60, -44, 90, -122, -44, 109, 2, 3, 1, 0, 1};
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
