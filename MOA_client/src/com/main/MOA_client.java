package com.main;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.connection.SocketClient;

public class MOA_client 
{

	public static final SocketClient sc = new SocketClient("localhost", 25000);
	
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
		
		MainFrame.setDefaultLookAndFeelDecorated(true);
		new MainFrame();
	}

}
