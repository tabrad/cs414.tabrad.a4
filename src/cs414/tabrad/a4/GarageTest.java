package cs414.tabrad.a4;

import static org.junit.Assert.*;

import org.junit.Test;

public class GarageTest {

	Garage garage = new Garage();
	TicketTracker ticketTracker = new TicketTracker();
	Driver driver = new Driver("XYZ-TTR", 0, 0);
	
	@Test public void testGrid()
	{
		assertTrue(garage.isClear(10, 10));
		
		driver.move(garage, 10, 10);
		assertFalse(garage.isClear(10, 10));
		
		driver.move(garage, 11, 11);
		assertTrue(garage.isClear(10, 10));
	}
	
	@Test
	public void testGeneral() 
	{
		//add some booths
		Rate rates = new Rate(3, 3, 20);
		garage.createBooth(ticketTracker, 1, new Location(5, 5), false, rates);
		garage.createBooth(ticketTracker, 1, new Location(10, 15), true, rates);
		
		//enter the garage
		driver.move(garage, 0, 0);
		Location location = driver.getLocation();
		assertTrue(location.x == 0);
		assertTrue(location.y == 0);
		
		//get a new ticket from the booth
		driver.enterGarage(garage);
		System.out.println(driver.getLocation().toString());
		
		Driver driver2 = new Driver("JJJ-983", 0, 0);
		driver2.move(garage, 0, 0);

		driver2.enterGarage(garage);
		System.out.println(driver2.getLocation().toString());
		
		driver2.exitGarage(garage);
		
	}

}
