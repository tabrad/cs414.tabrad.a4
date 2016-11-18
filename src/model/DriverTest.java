package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import server.BoothImpl;
import server.DriverImpl;
import server.GarageImpl;

public class DriverTest {

	GarageImpl garage;
	TicketTracker ticketTracker;
	DriverImpl driver;
	
	@Before public void initialize()
	{
		garage = GarageImpl.getInstance();
		ticketTracker = new TicketTracker();
		try{
		garage.createBooth(1, new Location(5, 5), false);
		garage.createBooth(1, new Location(10, 15), true);
		}catch(Exception e){}
		driver = new DriverImpl("XYZ-TTR", 0, 0);
	}
	
	@Test public void testMove() 
	{
		driver.move(new Location(20, 15));
		assertTrue(driver.getLocation().x == 20);
		assertTrue(driver.getLocation().y == 15);
		assertFalse(garage.isClear(new Location(20, 15)));
	}
	
	@Test public void testEnterGarage()
	{
		driver.enterGarage();
		assertTrue(driver.isParked());
		assertTrue(driver.getTicket() != null);
	}
	
	@Test public void testExitGarage()
	{
		driver.enterGarage();
		Location location = driver.getLocation();
		driver.goToExit();
		BoothImpl booth = garage.getNearestBooth(driver.getLocation(), true);
		Float amountDue = booth.getAmountDue(driver.getTicket());
		booth.insertPayment(driver, driver.getTicket(), amountDue, false);
		driver.exitGarage();
		assertTrue(driver.getTicket().isPaid());
		assertTrue(garage.isClear(location));
	}

}
