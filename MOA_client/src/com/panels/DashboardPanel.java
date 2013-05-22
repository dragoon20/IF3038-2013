package com.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.account.LoginHistory;
import com.connection.SocketClient;
import com.custom.CategoryButton;
import com.main.MOA_client;
import com.main.MainFrame;
import com.model.Category;
import com.model.Task;

public class DashboardPanel extends JPanel implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Category> list_category;
	private List<CategoryButton> list_category_buttons;
	private List<Task> active_list_task;
	private List<JButton> list_mark_buttons;
	private int active_category_id;
	
	private JPanel panel_category;
	private JPanel panel_task;
	
	private JButton button_refresh;
	private JButton button_logout;
	
	private LoginHistory history;
	
	private boolean first;
	
	public DashboardPanel(LoginHistory history)
	{
		super(new BorderLayout());
		this.history = history;
		
		panel_category = new JPanel(new FlowLayout());
		panel_category.setPreferredSize(new Dimension(220, 600));
		panel_category.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredSoftBevelBorder(), 
								"Kategori", TitledBorder.CENTER, TitledBorder.TOP, new Font("Serif", Font.BOLD, 16)));  
		
		panel_task = new JPanel(new FlowLayout());
		panel_task.setPreferredSize(new Dimension(570, 600));
		panel_task.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredSoftBevelBorder(), 
				"Tugas", TitledBorder.CENTER, TitledBorder.TOP, new Font("Serif", Font.BOLD, 16)));  
		
		JLabel label_app_name = new JLabel("Multiuser Online Agenda");
		label_app_name.setFont(new Font("Serif", Font.BOLD, 22));
		JLabel label_hello = new JLabel("Halo, ");
		label_hello.setFont(new Font("Serif", Font.PLAIN, 14));
		JLabel label_username = new JLabel(history.getUsername());
		label_username.setFont(new Font("Serif", Font.BOLD, 14));

		ImageIcon refresh_icon = new ImageIcon(ClassLoader.getSystemResource("images/refresh.png"));
		button_refresh = new JButton(refresh_icon);
		button_refresh.setPreferredSize(new Dimension(30, 30));
		button_refresh.addActionListener(this);
		
		button_logout = new JButton("Keluar");
		button_logout.addActionListener(this);
		
		JPanel panel_top = new JPanel(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.CENTER;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		c.gridheight = 1;
		c.weightx = 1;
		c.insets = new Insets(10, 0, 10, 130);
		panel_top.add(label_app_name, c);
		c.weightx = 0;
		
		c.fill = GridBagConstraints.EAST;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 4;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0, 0, 0, 0);
		panel_top.add(label_hello, c);
		
		c.fill = GridBagConstraints.EAST;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 5;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(0, 0, 0, 25);
		panel_top.add(label_username, c);
		
		c.fill = GridBagConstraints.EAST;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 4;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 0, 0, 5);
		panel_top.add(button_refresh, c);
		
		c.fill = GridBagConstraints.EAST;
		c.anchor = GridBagConstraints.NORTHEAST;
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 15, 0, 0);
		panel_top.add(button_logout, c);
		
		add(panel_top, BorderLayout.PAGE_START);
		
		add(panel_category, BorderLayout.WEST);
		add(panel_task, BorderLayout.CENTER);
		
		list_category = history.getCategories();
		list_category_buttons = new ArrayList<CategoryButton>();
		active_list_task = new ArrayList<Task>();
		list_mark_buttons = new ArrayList<JButton>();
		active_category_id = 0;

		refresh();
	}
	
	public void refresh()
	{
		if (!MOA_client.sc.logged_in)
		{
			MOA_client.sc.doLogin(history.getUsername(), history.getPassword());
		}
		List<Category> temp = MOA_client.sc.doList();
		if (temp!=null)
		{
			list_category = temp;
		}
		revalidate();
	}
	
	@Override
	public void revalidate() 
	{
		if (panel_category!=null)
		{
			panel_category.removeAll();
			list_category_buttons.removeAll(list_category_buttons);
			
			CategoryButton button_all = new CategoryButton("Semua", 0);
			button_all.setPreferredSize(new Dimension(200, 40));
			button_all.setFont(new Font("Serif", Font.BOLD, 14));
			button_all.addActionListener(this);
			if (first)
			{
				button_all.setActive(true);
				first = false;
			}
			list_category_buttons.add(button_all);
			panel_category.add(button_all);
			
			for (int i=0;i<list_category.size();++i)
			{
				CategoryButton button = new CategoryButton(list_category.get(i).getNama_kategori(), list_category.get(i).getId_kategori());
				button.setPreferredSize(new Dimension(200, 40));
				button.setFont(new Font("Serif", Font.BOLD, 14));
				button.addActionListener(this);
				list_category_buttons.add(button);
				panel_category.add(button);
			}
			panel_category.repaint();
			
			int i = 0;
			boolean check = true;
			while ((check) && (i<list_category_buttons.size()))
			{
				if (list_category_buttons.get(i).getId()==active_category_id)
				{
					check = false;
					list_category_buttons.get(i).setActive(true);
					list_category_buttons.get(i).repaint();
				}
				++i;
			}
			
			if (active_category_id==0)
			{
				active_list_task = new ArrayList<Task>(); 
				for (i=0;i<list_category.size();++i)
				{
					active_list_task.addAll(list_category.get(i).getList_task());
				}
			}
			else
			{
				i = 0;
				check = true;
				while ((check) && (i<list_category.size()))
				{
					if (list_category.get(i).getId_kategori()==active_category_id)
					{
						check = false;
						active_list_task = list_category.get(i).getList_task();
						System.out.println(active_list_task.size());
					}
					++i;
				}
			}
			
			panel_task.removeAll();
			list_mark_buttons.removeAll(list_mark_buttons);
			
			for (int j=0;j<active_list_task.size();++j)
			{
				JPanel panel = new JPanel(new BorderLayout());
				panel.setBorder(BorderFactory.createTitledBorder(active_list_task.get(j).getNama_task()));
				panel.setPreferredSize(new Dimension(550, 80));
				
				StringBuilder sb = new StringBuilder();
				/*sb.append("{");
				List<Tag> tags = active_list_task.get(j).getList_tag();
				for (Tag t : tags)
				{
					sb.append(t.getTag_name() + ",");
				}
				sb.delete(sb.length()-1, sb.length());
				sb.append("}");*/
				
				JLabel asignee = new JLabel(sb.toString());
				asignee.setFont(new Font("Serif", Font.PLAIN, 16));
				panel.add(asignee, BorderLayout.WEST);
				
				JButton status = new JButton((active_list_task.get(j).getStatus())? "selesai" : "belum selesai");
				status.setPreferredSize(new Dimension(150, 20));
				if (active_list_task.get(j).getStatus())
				{
					status.setForeground(Color.GREEN);
				}
				else
				{
					status.setForeground(Color.RED);
				}
				status.setFont(new Font("Serif", Font.BOLD, 13));
				status.addActionListener(this);
				list_mark_buttons.add(status);
				panel.add(status, BorderLayout.EAST);
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
				JLabel deadline_task = new JLabel(sdf.format(active_list_task.get(j).getDeadline()));
				deadline_task.setFont(new Font("Serif", Font.PLAIN, 16));
				deadline_task.setHorizontalAlignment(SwingConstants.CENTER);
				panel.add(deadline_task, BorderLayout.CENTER);
				
				panel_task.add(panel);
			}
			panel_task.repaint();

			history.setCategories(list_category);
			try {
				history.produce_xml();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		super.revalidate();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) 
	{
		if (button_refresh.equals(arg0.getSource()))
		{
			refresh();
		}
		else if (button_logout.equals(arg0.getSource()))
		{
			Container parent = this.getParent();
			parent.removeAll();
			parent.add(new LoginPanel());
			parent.revalidate();
			MOA_client.logged_in = false;
			
			try 
			{
				BufferedWriter br = new BufferedWriter(new FileWriter("loginhistory.txt"));
				br.write(0);
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			if (list_category_buttons.get(0).equals(arg0.getSource()))
			{
				active_category_id = list_category_buttons.get(0).getId();
				list_category_buttons.get(0).setActive(true);
				list_category_buttons.get(0).repaint();
				for (int i=1;i<list_category_buttons.size();++i)
				{
					list_category_buttons.get(i).setActive(false);
					list_category_buttons.get(i).repaint();
				}
				revalidate();
			}
			else
			{
				boolean check = true;
				
				list_category_buttons.get(0).setActive(false);
				list_category_buttons.get(0).repaint();
				for (int i=1;i<list_category_buttons.size();++i)
				{
					if (list_category_buttons.get(i).equals(arg0.getSource()))
					{
						check = false;
						list_category_buttons.get(i).setActive(true);
						active_category_id = list_category_buttons.get(i).getId();
					}
					else
					{
						list_category_buttons.get(i).setActive(false);
					}
					list_category_buttons.get(i).repaint();
				}
				
				int i = 0;
				boolean check2 = true;
				while ((check2) && (i<list_category_buttons.size()))
				{
					if (list_category_buttons.get(i).getId()==active_category_id)
					{
						check2 = false;
						list_category_buttons.get(i).setActive(true);
						list_category_buttons.get(i).repaint();
					}
					++i;
				}
				
				if (check)
				{
					i = 0;
					check = true;
					while ((check) && (i<list_mark_buttons.size()))
					{
						if (list_mark_buttons.get(i).equals(arg0.getSource()))
						{
							check = false;
							try
							{
								SocketClient.LB.parse_logs();
							}
							catch (Exception e)
							{
								e.printStackTrace();
							}
							SocketClient.LB.add_log((byte)active_list_task.get(i).getId_task(), active_list_task.get(i).getNama_task(), !active_list_task.get(i).getStatus());
							try 
							{
								SocketClient.LB.save_logs();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						++i;
					}
					--i;
					if (!check)
					{
						active_list_task.get(i).setStatus(!active_list_task.get(i).getStatus());
						list_mark_buttons.get(i).setText((active_list_task.get(i).getStatus())? "selesai" : "belum selesai");
						if (active_list_task.get(i).getStatus())
						{
							list_mark_buttons.get(i).setForeground(Color.GREEN);
						}
						else
						{
							list_mark_buttons.get(i).setForeground(Color.RED);
						}
						list_mark_buttons.get(i).revalidate();
						revalidate();
					}
				}
				else
				{
					revalidate();
				}
			}
		}
	}
}
