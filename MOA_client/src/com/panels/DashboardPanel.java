package com.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.model.Category;
import com.model.Task;

public class DashboardPanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Category> list_category;
	private int active_category;
	private List<Task> active_list_task;
	
	private JButton button_refresh;
	
	public DashboardPanel(String username)
	{
		super(new GridBagLayout());
		
		JLabel label_app_name = new JLabel("Multiuser Online Agenda");
		label_app_name.setFont(new Font("Serif", Font.BOLD, 22));
		JLabel label_hello = new JLabel("Halo, ");
		label_hello.setFont(new Font("Serif", Font.PLAIN, 14));
		JLabel label_username = new JLabel(username);
		label_username.setFont(new Font("Serif", Font.BOLD, 14));
		
		button_refresh = new JButton("Refresh");
		button_refresh.setFont(new Font("Serif", Font.BOLD, 14));
		button_refresh.addActionListener(this);
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(10, 0, 10, 130);
		add(label_app_name, c);
		c.weightx = 0;
		
		c.fill = GridBagConstraints.EAST;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 0, 0, 0);
		add(label_hello, c);
		
		c.fill = GridBagConstraints.EAST;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 5;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 0, 0, 25);
		add(label_username, c);
		
		list_category = new ArrayList<Category>();
		active_category = 0;
		active_list_task = new ArrayList<Task>();
		
		refresh();
	}
	
	public void refresh()
	{
		revalidate();
	}
	
	@Override
	public void revalidate() 
	{
		super.revalidate();
	}
	
	@Override
	public void paintComponent(Graphics g) 
	{
		super.paintComponents(g);

		Graphics2D g2d = (Graphics2D) g;
		Color color1 = this.getBackground();
		Color color2 = color1.darker();
		
		int width = getWidth();
		int height = getHeight();
		GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
		g2d.setPaint(gp);
		g2d.fillRect(0, 0, width, height);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if (button_refresh.equals(arg0.getSource()))
		{
			refresh();
		}
	}
}
