package view;

import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GarageView extends Dialog
{
	private static GarageView instance = null;
	private int rows = 10;
	private int columns = 10;
	private JPanel[][] iconHolder = new JPanel[rows][columns];
	
	public static GarageView getInstance()
	{
		if(instance == null)
			instance = new GarageView();
		
		return instance;
	}
	
	private GarageView()
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
	
	public void addLabelToGrid(int x, int y, JLabel label)
	{
		JPanel panel = iconHolder[x][y];
		panel.add(label);
		frame.pack();
	}

	public void updateIcon(int[] move) 
	{
		JPanel panelStart = iconHolder[move[0]][move[1]];
		JPanel panelEnd = iconHolder[move[2]][move[3]];
		
		Component icon = panelStart.getComponent(0);
		panelEnd.add(icon);
		panelStart.removeAll();
		frame.pack();
	}

	public void addDriverIcon(String license, int x, int y) 
	{
		DriverIcon icon = new DriverIcon(license, x, y);
		addLabelToGrid(x, y, icon);	
	}

}
