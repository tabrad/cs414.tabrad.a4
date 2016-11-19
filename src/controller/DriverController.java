package controller;

import java.rmi.RemoteException;

import common.Driver;
import common.Garage;
import common.Location;

public class DriverController 
{	
	private Driver driver;
	private GarageController garageController;
	
	public DriverController(Driver driver, Garage garage)
	{
		this.driver = driver;
		garageController = GarageController.getInstance();
	}
	
	public void moveDriverToBooth(int boothId) throws RemoteException  
	{
		driver.goToBooth(boothId);
	}

	public void pushTicketButton(int boothId) throws RemoteException 
	{
		driver.pushTicketButton(boothId, false);
	}

	public void driverPrematureExit(int boothId) throws RemoteException 
	{
		Location location = driver.getLocation();
		String license = driver.getLicense();
		garageController.removeVehicle(location, license, boothId);
	}

	public void parkCar() throws RemoteException 
	{
		driver.parkCar();
	}
	
	public void driverExit(int boothId) throws RemoteException 
	{
		driver.exitGarage(boothId);
		GarageController.updateOverview();
	}
}
