package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Garage;
import model.Location;
import model.Rate;
import model.TicketTracker;
import model.Driver;

import view.DriverDialog;
import view.OverviewDialog;
import view.ReportDialog;

// GarageController is singleton
public class GarageController implements ActionListener
{
	private static GarageController instance = null;
	
	//view
	private static OverviewDialog overviewDialog;
	
	//model
	private static Garage garage;
	private static TicketTracker ticketTracker;
	private static Rate rates;
	
	public static void main(String[] args)
    {
        overviewDialog = new OverviewDialog();
        overviewDialog.showDialog();
    }
	
	private GarageController()
	{
		garage = new Garage();
		rates = new Rate(3, 3, 20);
	}
	
	public static GarageController getInstance()
	{
		if(instance == null)
		{
			instance = new GarageController();
			initialize();
		}
		
		return instance;
	}
	
	private static void initialize()
	{
		ticketTracker = new TicketTracker(garage);
		garage.createBooth(ticketTracker, 1, new Location(5, 5), false, rates);
		garage.createBooth(ticketTracker, 1, new Location(10, 15), true, rates);
	}

	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		
		if(command.equals("Reports"))  
        {
			reportsClicked();
        }
        else if (command.equals("Simulate")) 
        {
           simulate();
        } 
        else if (command.equals("New Car"))
        {
        	createDriver();
        }
		
		Location location = new Location(0,0);
		overviewDialog.update(ticketTracker.getOccupancy(), 
				garage.getMaxOccupancy(), 
				garage.getNearestBooth(location, false).getGate().isOpen(), 
				garage.getNearestBooth(location, true).getGate().isOpen());
	}
	
	public void simulate() 
	{
		garage.simulate();
	}

	public void createDriver() 
	{
		String license = "" + System.currentTimeMillis();
    	Driver driver = garage.createDriver(license);
    	DriverDialog driverDialog = new DriverDialog(garage, driver);
    	driverDialog.showDialog();
	}

	public void reportsClicked() 
	{
		ReportDialog reportDialog = new ReportDialog(ticketTracker);
        reportDialog.showDialog();
	}
}
