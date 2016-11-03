package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class ClickableLabel extends JLabel
{
	private static final long serialVersionUID = 1L;

	public ClickableLabel(ImageIcon icon)
	{
		super(icon);
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				onClick();
			}
		});
	}
	
	protected void onClick()
	{
		System.out.println("mouse clicked");
	}
}
