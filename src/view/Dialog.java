package view;

import javax.swing.JFrame;

public abstract class Dialog 
{
	protected JFrame frame;
	
	public void showDialog()
	{
		frame.setVisible(true);
	}
}
