package com.custom;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class CategoryButton extends JButton
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean hovered;
	private boolean active;
	private int id;
	
	public CategoryButton(String text, int id)
	{
		super(text);
		this.id = id;
		hovered = false;
		active = false;
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				hovered = false;
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				hovered = true;
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setActive(boolean b)
	{
		active = b;
	}
	
	@Override
	protected void paintComponent(Graphics g) 
	{
		super.paintComponent(g);
		
		Dimension originalSize = super.getPreferredSize();

		
		if ((hovered) || (active))
		{
			g.setColor(new Color(54, 54, 54));
		}
		else
		{
			g.setColor(new Color(164, 164, 164));
		}
        g.fillRoundRect(0, 0, originalSize.width-3, originalSize.height-3, 10, 10);
        
        if ((hovered) || (active))
		{
        	g.setColor(new Color(19, 96, 243));
		}
        else
        {
        	g.setColor(new Color(30, 144, 255));
        }
        g.fillRoundRect(3, 3, originalSize.width-9, originalSize.height-9, 10, 10);
        
        g.setColor(Color.WHITE);
        g.setFont(getFont());
        g.drawString(getText(), 10, (int)(originalSize.height * 0.55));
	}

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}
}
