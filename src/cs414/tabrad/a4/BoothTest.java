package cs414.tabrad.a4;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

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
		admin = new Admin("jfiwkls", "or023kf9");
		garage.addAdmin(admin);
		
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
	
	@Test public void testAdminTicketInserted()
	{
		driver.enterGarage(garage);
		boothExit.login(admin);
		Ticket ticket = driver.getTicket();
		boothExit.insertTicket(driver, ticket);
		assertTrue(ticket.isPaid());
		assertFalse(driver2.isParked());
	}
	
	@Test public void testTicketCharge()
	{
		Calendar calendar = Calendar.getInstance();
		Date currentDate = new Date();
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*5)); // five hours ago
		Ticket ticket = new Ticket(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket);
		float boothAmount = boothExit.getAmountDue(ticket);
		long seconds = System.currentTimeMillis() / 1000 - calendar.getTimeInMillis() / 1000;
		long hours = seconds / 60 / 60;
		float neededAmount = 5 * rates.hourlyRate;
		assertTrue(boothAmount == neededAmount);
		
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*1000)); // 1000 hours ago
		Ticket ticket2 = new Ticket(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket2);
		boothAmount = boothExit.getAmountDue(ticket2);
		seconds = System.currentTimeMillis() / 1000 - calendar.getTimeInMillis() / 1000;
		hours = seconds / 60 / 60;
		neededAmount = rates.maxCharge;
		assertTrue(boothAmount == neededAmount);
		
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*1/2)); // 30 minutes ago
		Ticket ticket3 = new Ticket(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket3);
		boothAmount = boothExit.getAmountDue(ticket3);
		System.out.println(boothAmount);
		seconds = System.currentTimeMillis() / 1000 - calendar.getTimeInMillis() / 1000;
		hours = seconds / 60 / 60;
		neededAmount = rates.minCharge;
		assertTrue(boothAmount == neededAmount);
	}

}
