package controller;

import java.rmi.RemoteException;

import common.Driver;
import common.Garage;

public class DriverController 
{	
	private Driver driver;
	private Garage garage;
	
	public DriverController(Driver driver, Garage garage)
	{
		this.driver = driver;
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
		garage.removeVehicle(driver.getLocation());
	}

	public void parkCar() throws RemoteException 
	{
		driver.parkCar();
		
		//closing the gate right here. Not the most logical place to put this code. should be more of a signal than a control of the booth. Rename?
		garage.getBooth(false).closeGate();
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
