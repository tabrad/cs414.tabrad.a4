package controller;

import java.rmi.RemoteException;

import common.Driver;
import common.Garage;
import model.Location;

public class DriverController 
{	
	private Driver driver;
	private GarageController garageController;
	
	public DriverController(Driver driver, Garage garage)
	{
		this.driver = driver;
		garageController = GarageController.getInstance();
	}
	
	public void moveDriverToEntrance() throws RemoteException  
	{
		driver.goToEntrance();
	}

	public void pushTicketButton(int boothId) throws RemoteException 
	{
		driver.pushTicketButton(boothId);
	}

	public void driverPrematureExit() throws RemoteException 
	{
		Location location = driver.getLocation();
		String license = driver.getLicense();
		garageController.removeVehicle(location, license);
	}

	public void parkCar() throws RemoteException 
	{
		driver.parkCar();
	}

	public void moveDriverToExit() throws RemoteException 
	{
		driver.goToExit();
	}
	
	public void driverExit() throws RemoteException 
	{
		driver.exitGarage();
		GarageController.updateOverview();
	}
}
