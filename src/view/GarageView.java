package view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JPanel;

import model.Driver;
import model.Garage;

public class GarageView extends JPanel implements Observer
{
	private static final long serialVersionUID = 1L;
	private static GarageView instance = null;
	
	public static GarageView getInstance()
	{
		if(instance == null)
			instance = new GarageView();
		
		return instance;
	}
	
	private GarageView(){}

	@Override
	public void update(Observable o, Object arg) 
	{
		repaint();
	}
	
	//paintComponent is called in repaint()
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
}
