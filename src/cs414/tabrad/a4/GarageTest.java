package cs414.tabrad.a4;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GarageTest {

	Garage garage;
	TicketTracker ticketTracker;
	Driver driver;
	
	@Before public void initialize()
	{
		garage = new Garage();
		ticketTracker = new TicketTracker(garage);
		driver = garage.createDriver("83nfjahf");

		Rate rates = new Rate(3, 3, 20);
		garage.createBooth(ticketTracker, 1, new Location(5, 5), false, rates);
		garage.createBooth(ticketTracker, 1, new Location(10, 15), true, rates);
	}
	
	@Test public void testGrid()
	{
		assertTrue(garage.isClear(new Location(10, 10)));
		
		driver.move(garage, new Location(10, 10));
		assertFalse(garage.isClear(new Location(10, 10)));
		
		driver.move(garage, new Location(11, 11));
		assertTrue(garage.isClear(new Location(10, 10)));
	}
	
	@Test public void testRemoveVehicle()
	{
		driver.move(garage, new Location(10, 10));
		garage.removeVehicle(driver.getLocation());
		assertTrue(garage.isClear(new Location(10, 10)));
	}
	
	@Test
	public void testGeneral() 
	{
		//enter the garage
		driver.move(garage, new Location(0, 0));
		Location location = driver.getLocation();
		assertTrue(location.x == 0);
		assertTrue(location.y == 0);
		
		//get a new ticket from the booth
		driver.enterGarage(garage);
		
		Driver driver2 = new Driver("JJJ-983", 0, 0);
		driver2.move(garage, new Location(0, 0));
		driver2.enterGarage(garage);
		driver2.exitGarage(garage);
		
	}

}
