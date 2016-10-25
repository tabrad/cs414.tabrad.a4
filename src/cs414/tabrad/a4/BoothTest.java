package cs414.tabrad.a4;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BoothTest {

	Garage garage;
	TicketTracker ticketTracker;
	Booth boothEntrance;
	Booth boothExit;
	Rate rates;
	Driver driver;
	Driver driver2;
	Admin admin;
	
	@Before public void initialize()
	{
		garage = new Garage();
		rates = new Rate(3, 3, 20);
		ticketTracker = new TicketTracker(garage);
		driver = new Driver("XYZ-TTR", 0, 0);
		driver2 = new Driver("YYZ-T45", 0, 0);
		
		garage.createBooth(ticketTracker, 1, new Location(5, 5), false, rates);
		boothEntrance = garage.getNearestBooth(new Location(0, 0), false);
		
		garage.createBooth(ticketTracker, 2, new Location(25, 30), true, rates);
		boothExit = garage.getNearestBooth(new Location(0, 0), true);
	}
	
	@Test public void testTicketButtonPressed()
	{
		assertFalse(boothEntrance.getGate().isOpen());
		boothEntrance.ticketButtonPressed(driver);
		assertTrue(driver.getTicket() != null);
		
		assertFalse(boothEntrance.getGate().isOpen());
		boothEntrance.ticketButtonPressed(driver2);
		assertTrue(driver2.getTicket() != null);
	}
	
	@Test public void testTicketButtonPressedNotEntrance()
	{
		boothExit.ticketButtonPressed(driver);
		assertFalse(boothExit.getGate().isOpen());
		assertFalse(driver.isParked());
	}
	
	@Test public void testTicketInserted()
	{
		driver.enterGarage(garage);
		driver2.enterGarage(garage);
		assertFalse(boothExit.getGate().isOpen());
		
		Ticket ticket = driver.getTicket();
		boothExit.insertTicket(driver, ticket);
		assertTrue(ticket.isPaid());
		assertFalse(driver.isParked());
		
		ticket = driver2.getTicket();
		boothExit.insertTicket(driver2, ticket);
		assertTrue(ticket.isPaid());
		assertFalse(driver2.isParked());
	}
	
}
