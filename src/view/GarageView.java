package view;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import controller.GarageController;
import model.Driver;
import model.Garage;
import model.Location;

public class GarageView extends JPanel implements Observer
{
	private static final long serialVersionUID = 1L;
	private static GarageView instance = null;
	GarageController garageController;
	private static int iconWidth = 32;
	private static int iconHeight = 32;
	
	public static GarageView getInstance()
	{
		if(instance == null)
			instance = new GarageView();
		
		return instance;
	}
	
	private GarageView()
	{
		garageController = GarageController.getInstance();
		
		addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				int x = e.getX();
				x = (x/32) + 1;
				int y = e.getY();
				y = (y/32) + 1;
				System.out.println("mouse clicked x:" + x + " y:" + y);
				
				Garage garage = Garage.getInstance();
				Set<Driver> drivers = garage.getDrivers();
				for(Driver driver : drivers)
				{		
					if(x != driver.getLocation().x)
						continue;
					
					if(y != driver.getLocation().y)
						continue;
					
					DriverDialog dialog = new DriverDialog(driver.getLicense(), driver.getLocation().x, driver.getLocation().y, driver.hasTicket(), driver.isParked());
					dialog.showDialog();
					return;
					
				}
				
				
			}
		});
	}

	@Override
	public void update(Observable o, Object arg) 
	{
		repaint();
	}
	
	//paintComponent is called in repaint()
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		//draw background grid
		for(int x = 0; x < 639; x += iconWidth)
		{
			g.drawLine(2, x, 639, x);
		}
		
		for(int y = 0; y < 639; y += iconHeight)
		{
			g.drawLine(y, 2, y, 639);
		}
		
		//paint parking stalls certain color
		Garage garage = Garage.getInstance();
		Set<Location> stalls = garage.getParkingStalls();
		for(Location stall : stalls)
		{
			BufferedImage img = null;
			try 
			{
			    img = ImageIO.read(GarageView.class.getResource("images/stall.png"));
			} catch (IOException e) {
			}

			g.drawImage(img, stall.x * iconWidth - iconWidth, stall.y * iconHeight - iconHeight, null);
		}
		
		//draw drivers
		Set<Driver> drivers = garage.getDrivers();
		for(Driver driver : drivers)
		{
			BufferedImage img = null;
			try 
			{
			    img = ImageIO.read(GarageView.class.getResource("images/sports-car.png"));
			} catch (IOException e) {
			}

			g.drawImage(img, driver.getLocation().x * iconWidth - iconWidth, driver.getLocation().y * iconHeight - iconHeight, null);
		}
		
		
	}
}
