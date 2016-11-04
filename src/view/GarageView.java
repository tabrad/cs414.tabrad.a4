package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Driver;
import model.Garage;

public class GarageView extends JPanel implements Observer
{
	private static GarageView instance = null;
	//private int rows = 30;
	//private int columns = 30;
	//private JPanel[][] iconHolder = new JPanel[rows][columns];
	
	public static GarageView getInstance()
	{
		if(instance == null)
			instance = new GarageView();
		
		return instance;
	}
	
	private GarageView()
	{
		//setup();
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		System.out.println("GarageView Notified");
		Driver driver = (Driver)o;
		int[] move = (int[])arg;
		repaint(move[2], move[3], 10, 10);
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		Garage garage = Garage.getInstance();
		Set<Driver> drivers = garage.getDrivers();
		
		for(Driver driver : drivers)
		{
			g.setColor(Color.BLACK);
			g.fillRect(driver.getLocation().x, driver.getLocation().y, 5, 5);
		}
	}
	
//	public void setup()
//	{
//		frame = new JFrame();
//		frame.setSize(700, 700);
//		frame.setLayout(new GridLayout(rows, columns));
//		
//		//setup a JPanel array with empty panels that can hold our icons
//		for(int i = 0; i < rows; i++)
//		{
//			for(int j = 0; j < columns; j++)
//			{
//				iconHolder[i][j] = new JPanel();
//				frame.add(iconHolder[i][j]);
//			}
//		}
//	}
	
//	public void addLabelToGrid(int x, int y, JLabel label)
//	{
//		JPanel panel = new JPanel();
//		panel.add(label);
//		iconHolder[x][y] = panel;
//		frame.pack();
//	}

//	public void updateIcon(int[] move) 
//	{
//		//move the item to new location on the grid
//		//JPanel panelStart = iconHolder[move[0]][move[1]];
//		JPanel panelEnd = iconHolder[move[2]][move[3]];
//		
//		Graphics2D g = (Graphics2D)panelEnd.getGraphics();
//		g.setPaint(Color.blue);
//
//        g.drawLine(move[0], move[1], move[2], move[3]);
//		//Component icon = panelStart.getComponent(0);
//		//icon.update(null);
//		//panelEnd.add(icon);
//		//panelStart.removeAll();
//		frame.pack();
//	}

//	public void addDriverIcon(String license, int x, int y) 
//	{
//		DriverIcon icon = new DriverIcon(license, x, y);
//		addLabelToGrid(x, y, icon);	
//	}
	public void addDriverIcon(String license, int x, int y)
	{
		
	}

}
