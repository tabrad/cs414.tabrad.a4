package controller;

import java.util.Observable;
import java.util.Observer;

import model.Driver;
import model.Garage;
import view.DriverDialog;

public class DriverController implements Observer
{
	private static Garage garage = Garage.getInstance();
	
	public static void updateDriverDialog(DriverDialog dialog, Driver driver)
	{
		dialog.update(driver.getLocation().x, driver.getLocation().y, driver.hasTicket(), driver.isParked());
		
		//each time the driver is updated, the is likely updates for the overview dialog too
		GarageController.updateOverview();
	}
	
	public static void moveDriverToEntrance(DriverDialog dialog, String license, int x, int y) 
	{
		Driver driver = garage.getDriver(license);
		driver.goToEntrance();
		updateDriverDialog(dialog, driver);
	}

	public static void pushTicketButton(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.pushTicketButton(garage.getNearestBooth(driver.getLocation(), false), false);
		updateDriverDialog(dialog, driver);
	}

	public static void driverPrematureExit(String license) 
	{
		Driver driver = garage.getDriver(license);
		garage.removeVehicle(driver.getLocation());
	}

	public static void parkCar(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.parkCar();
		
		//closing the gate right here. Not the most logical place to put this code. Maybe refactor later
		garage.getNearestBooth(driver.getLocation(), false).closeGate();
		
		updateDriverDialog(dialog, driver);
	}

	public static void moveDriverToExit(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.goToExit();
		updateDriverDialog(dialog, driver);
	}
	
	public static void driverExit(DriverDialog dialog, String license) 
	{
		Driver driver = garage.getDriver(license);
		driver.exitGarage();
		updateDriverDialog(dialog, driver);
	}

	@Override
	public void update(Observable obj, Object arg) 
	{
		System.out.println("notified");
		GarageController garageController = GarageController.getInstance();
		garageController.updateIcon(arg);
	}
}
