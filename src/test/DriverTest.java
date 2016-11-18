package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import org.junit.Before;
import org.junit.Test;

import common.Booth;
import common.Location;
import server.DriverImpl;
import server.GarageImpl;
import server.TicketTracker;

public class DriverTest {

	GarageImpl garage;
	TicketTracker ticketTracker;
	DriverImpl driver;
	
	@Before public void initialize() throws RemoteException
	{
		garage = GarageImpl.getInstance();
		ticketTracker = new TicketTracker();
		try{
		garage.createBooth(1, new Location(5, 5), false);
		garage.createBooth(2, new Location(10, 15), true);
		}catch(Exception e){}
		driver = new DriverImpl("XYZ-TTR", 0, 0);
	}
	
	@Test public void testMove() 
	{
		driver.move(new Location(10, 5));
		assertTrue(driver.getLocation().x == 10);
		assertTrue(driver.getLocation().y == 5);
		assertFalse(garage.isClear(new Location(10, 5)));
	}
	
	@Test public void testEnterGarage() throws RemoteException
	{
		driver.goToEntrance();
		driver.pushTicketButton(1, false);
		driver.parkCar();
		assertTrue(driver.isParked());
		assertTrue(driver.getTicket() != null);
	}
	
	@Test public void testExitGarage() throws RemoteException
	{
		driver.goToEntrance();
		driver.pushTicketButton(1, false);
		driver.parkCar();
		Location location = driver.getLocation();
		driver.goToExit();
		Booth booth = garage.getBooth(true);
		Float amountDue = booth.getAmountDue(driver.getTicket().getId());
		booth.insertPayment(driver.getTicket().getId(), amountDue, false);
		driver.exitGarage();
		assertTrue(driver.getTicket().isPaid());
		assertTrue(garage.isClear(location));
	}

}
