package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.GarageImpl;

public class GarageTest {

	GarageImpl garage;
	TicketTracker ticketTracker;
	Driver driver;
	
	@Before public void initialize()
	{
		garage = GarageImpl.getInstance();
		ticketTracker = new TicketTracker();
		driver = garage.createDriver("83nfjahf");

		garage.createBooth(1, new Location(5, 5), false);
		garage.createBooth(1, new Location(10, 15), true);
	}
	
	@Test public void testGrid()
	{
		assertTrue(garage.isClear(new Location(10, 10)));
		
		driver.move(new Location(10, 10));
		assertFalse(garage.isClear(new Location(10, 10)));
		
		driver.move(new Location(11, 11));
		assertTrue(garage.isClear(new Location(10, 10)));
	}
	
	@Test public void testRemoveVehicle()
	{
		driver.move(new Location(10, 10));
		garage.removeVehicle(driver.getLocation(), driver);
		assertTrue(garage.isClear(new Location(10, 10)));
	}
	
	@Test
	public void testGeneral() 
	{
		//enter the garage
		driver.move(new Location(0, 0));
		Location location = driver.getLocation();
		assertTrue(location.x == 0);
		assertTrue(location.y == 0);
		
		//get a new ticket from the booth
		driver.enterGarage();
		
		Driver driver2 = new Driver("JJJ-983", 0, 0);
		driver2.move(new Location(0, 0));
		driver2.enterGarage();
		driver2.exitGarage();
		
	}

}
