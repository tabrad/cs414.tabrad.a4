package cs414.tabrad.a4;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DriverTest {

	Garage garage;
	TicketTracker ticketTracker;
	Rate rates;
	Driver driver;
	
	@Before public void initialize()
	{
		garage = new Garage();
		ticketTracker = new TicketTracker(garage);
		rates = new Rate(3, 3, 20);
		garage.createBooth(ticketTracker, 1, new Location(5, 5), false, rates);
		garage.createBooth(ticketTracker, 1, new Location(10, 15), true, rates);
		driver = new Driver("XYZ-TTR", 0, 0);
	}
	
	@Test public void testMove() 
	{
		driver.move(garage, new Location(20, 15));
		assertTrue(driver.getLocation().x == 20);
		assertTrue(driver.getLocation().y == 15);
		assertFalse(garage.isClear(new Location(20, 15)));
	}
	
	@Test public void testEnterGarage()
	{
		driver.enterGarage(garage);
		assertTrue(driver.isParked());
		assertTrue(driver.getTicket() != null);
	}
	
	@Test public void testExitGarage()
	{
		driver.enterGarage(garage);
		Location location = driver.getLocation();
		driver.exitGarage(garage);
		assertFalse(driver.isParked());
		assertTrue(driver.getTicket().isPaid());
		assertTrue(garage.isClear(location));
	}

}
