package view;

import javax.swing.JFrame;

public abstract class Dialog 
{
	protected JFrame frame;
	
	public void showDialog()
	{
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void shutDialog()
	{
		frame.dispose();
	}
	
	public void refresh()
	{
		frame.repaint();
	}
}
