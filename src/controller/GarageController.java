package controller;
import java.util.Observer;
import java.util.Observable;

import model.Booth;
import model.Garage;
import model.Location;
import model.Rate;
import model.TicketTracker;
import model.Driver;
import view.DriverDialog;
import view.ReportDialog;

// GarageController is singleton
public class GarageController implements Observer
{
	private static GarageController instance = null;
	private static Garage garage;
	private static Booth entranceBooth;
	private static Booth exitBooth;
	private static TicketTracker ticketTracker;
	private static Rate rates;
	
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
		ticketTracker.addObserver(instance);
		
		entranceBooth = garage.createBooth(ticketTracker, 1, new Location(5, 5), false, rates);
		entranceBooth.addObserver(instance);
		exitBooth = garage.createBooth(ticketTracker, 1, new Location(10, 15), true, rates);
		exitBooth.addObserver(instance);
	}
	
	@Override
	public void update(Observable observable, Object arg)
	{
	
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
