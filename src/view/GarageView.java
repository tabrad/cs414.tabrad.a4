package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
			BufferedImage img = null;
			try 
			{
			    img = ImageIO.read(GarageView.class.getResource("images/sports-car.png"));
			} catch (IOException e) {
			}

			g.drawImage(img, driver.getLocation().x, driver.getLocation().y, null);
		}
	}
}
