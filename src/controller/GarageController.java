package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Garage;
import model.Location;
import model.Rate;
import model.Ticket;
import model.TicketTracker;
import model.Booth;
import model.Driver;
import view.Dialog;
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
		garage = Garage.getInstance();
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
		
		updateOverview();
	}
	
	public void updateOverview()
	{
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
    	DriverDialog driverDialog = new DriverDialog(license, driver.getLocation().x, driver.getLocation().y, driver.hasTicket(), driver.isParked());
    	driverDialog.showDialog();
	}

	public void reportsClicked() 
	{
		ReportDialog reportDialog = new ReportDialog(ticketTracker);
        reportDialog.showDialog();
	}
	
	private void updateDriverDialog(DriverDialog dialog, Driver driver)
	{
		dialog.update(driver.getLocation().x, driver.getLocation().y, driver.hasTicket(), driver.isParked());
		
		//each time the driver is updated, the is likely updates for the overview dialog too
		updateOverview();
	}
	
	public void moveDriverToEntrance(DriverDialog dialog, String license, int x, int y) 
	{
		Driver driver = garage.getDriver(license);
		driver.goToEntrance();
		updateDriverDialog(dialog, driver);
	}

	public void pushTicketButton(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.pushTicketButton(garage.getNearestBooth(driver.getLocation(), false), false);
		updateDriverDialog(dialog, driver);
	}

	public void driverPrematureExit(String license) 
	{
		Driver driver = garage.getDriver(license);
		garage.removeVehicle(driver.getLocation());
	}

	public void parkCar(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.parkCar();
		
		//closing the gate right here. Not the most logical place to put this code. Maybe refactor later
		garage.getNearestBooth(driver.getLocation(), false).closeGate();
		
		updateDriverDialog(dialog, driver);
	}

	public void moveDriverToExit(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.goToExit();
		updateDriverDialog(dialog, driver);
	}
	
	public void driverExit(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.exitGarage();
		updateDriverDialog(dialog, driver);
	}

	public float getAmountDue(String license, boolean lostTicket) 
	{
		Driver driver = garage.getDriver(license);
		Booth booth = garage.getNearestBooth(driver.getLocation(), true);
		float amountDue = 0;
		
		if(lostTicket)
			amountDue = booth.getAmountDue();
		else
			amountDue = booth.getAmountDue(driver.getTicket());
		
		return amountDue;
	}

	public float getAmountDueByTicketId(String license, String id) 
	{	
		Driver driver = garage.getDriver(license);
		Booth booth = garage.getNearestBooth(driver.getLocation(), true);
		Ticket ticket = ticketTracker.findTicket(id);
		
		return booth.getAmountDue(ticket);
	}

	public boolean findTicket(String id) 
	{
		return ticketTracker.findTicket(id) != null;
	}

	public boolean insertPayment(DriverDialog dialog, String license, float amountDue, boolean isCreditCard) 
	{
		Driver driver = garage.getDriver(license);
		Booth booth = garage.getNearestBooth(driver.getLocation(), true);
		
		boolean isPaid = booth.insertPayment(driver, driver.getTicket(), amountDue, isCreditCard);
		updateDriverDialog(dialog, driver);
		
		return isPaid;
	}
}
