package view;

import java.awt.GridLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GarageView extends Dialog
{
	private int rows = 10;
	private int columns = 10;
	private JPanel[][] iconHolder = new JPanel[rows][columns];
	
	public static void main(String[] args) 
	{
		GarageView view = new GarageView();
		view.showDialog();
		
		URL imageURL = GarageView.class.getResource("images/sports-car.png");
		if(imageURL == null)
			System.out.println("null image");
		ImageIcon icon = new ImageIcon(imageURL);
		
		view.addIconToGrid(5, 5, icon);
	}
	
	public GarageView()
	{
		setup();
	}
	
	public void setup()
	{
		frame = new JFrame();
		frame.setSize(500, 500);
		frame.setLayout(new GridLayout(rows, columns));
		
		//setup a JPanel array with empty panels that can hold our icons
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < columns; j++)
			{
				iconHolder[i][j] = new JPanel();
				frame.add(iconHolder[i][j]);
			}
		}
	}
	
	public void addIconToGrid(int x, int y, ImageIcon icon)
	{
		JPanel panel = iconHolder[x][y];
		panel.add(new JLabel(icon));
		frame.pack();
	}

}
