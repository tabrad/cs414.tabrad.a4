package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import common.Driver;
import common.Location;
import server.GarageImpl;
import server.TicketTracker;

public class GarageTest {

	GarageImpl garage;
	TicketTracker ticketTracker;
	Driver driver;
	
	@Before public void initialize() throws RemoteException
	{
		garage = GarageImpl.getInstance();
		ticketTracker = new TicketTracker();
		driver = garage.createDriver();

		try{
		garage.createBooth(1, new Location(5, 5), false);
		garage.createBooth(1, new Location(10, 15), true);
		}catch(Exception e){}
	}
	
	@Test public void testGrid() throws RemoteException
	{
		assertTrue(garage.isClear(new Location(10, 10)));
		
		garage.moveObject(driver.getLicense(), driver.getLocation(), 10, 10);
		assertFalse(garage.isClear(new Location(10, 10)));
		
		garage.moveObject(driver.getLicense(), new Location(10, 10), 11, 11);
		assertFalse(garage.isClear(new Location(11, 11)));
		assertTrue(garage.isClear(new Location(10, 10)));
	}
	
	@Test public void testRemoveVehicle() throws RemoteException
	{
		garage.moveObject(driver.getLicense(), new Location(0,0), 10, 10);
		garage.removeVehicle(new Location(10,10), driver.getLicense());
		assertTrue(garage.isClear(new Location(10, 10)));
	}

}
