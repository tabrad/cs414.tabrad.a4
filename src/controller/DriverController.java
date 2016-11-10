package controller;

import model.Driver;
import model.Garage;

public class DriverController
{
	private static Garage garage = Garage.getInstance();
	
	public static void updateDriverDialog(Driver driver)
	{
		//each time the driver is updated, the is likely updates for the overview dialog too
		GarageController.updateOverview();
	}
	
	public static void moveDriverToEntrance(Driver driver) 
	{
		driver.goToEntrance();
	}

	public static void pushTicketButton(Driver driver) 
	{
		driver.pushTicketButton(garage.getNearestBooth(driver.getLocation(), false), false);
	}

	public static void driverPrematureExit(Driver driver) 
	{
		garage.removeVehicle(driver.getLocation());
	}

	public static void parkCar(Driver driver) 
	{
		driver.parkCar();
		
		//closing the gate right here. Not the most logical place to put this code. Maybe refactor later
		garage.getNearestBooth(driver.getLocation(), false).closeGate();
	}

	public static void moveDriverToExit(Driver driver) 
	{
		driver.goToExit();
	}
	
	public static void driverExit(Driver driver) 
	{
		driver.exitGarage();
	}
}
