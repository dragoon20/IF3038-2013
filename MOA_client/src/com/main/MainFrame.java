package com.main;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.account.LoginHistory;
import com.panels.DashboardPanel;
import com.panels.LoginPanel;

public class MainFrame extends JFrame implements WindowListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static boolean logged_in;
	
	public MainFrame()
	{
		URL url = ClassLoader.getSystemResource("images/icon.png");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.createImage(url);
		setIconImage(img);
		setTitle("MOA_client");
		
		setSize(800, 600);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(null);

		MOA_client.conn_thread.start();
		try
		{
			LoginHistory history = new LoginHistory();
			if (history.parse_xml())
			{
                add(new DashboardPanel(history));
			}
			else
			{
				add(new LoginPanel());
			}
		}
		catch (Exception exc)
		{
			exc.printStackTrace();
			add(new LoginPanel());
		}
		setVisible(true);
		revalidate();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) 
	{
		int n = JOptionPane.showConfirmDialog(this, "Keluar dari MOA_client?", "Konfirmasi keluar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (n==JOptionPane.YES_OPTION)
		{
			System.exit(0);
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
