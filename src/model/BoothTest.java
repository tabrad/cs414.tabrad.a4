package model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import server.BoothImpl;
import server.DriverImpl;
import server.GarageImpl;

public class BoothTest {

	GarageImpl garage;
	TicketTracker ticketTracker;
	BoothImpl boothEntrance;
	BoothImpl boothExit;
	Rate rates;
	DriverImpl driver;
	DriverImpl driver2;
	Admin admin;
	
	@Before public void initialize()
	{
		garage = GarageImpl.getInstance();
		rates = new Rate(3, 3, 20);
		ticketTracker = new TicketTracker();
		driver = new DriverImpl("XYZ-TTR", 0, 0);
		driver2 = new DriverImpl("YYZ-T45", 0, 0);
		admin = new Admin("jfiwkls", "or023kf9");
		garage.addAdmin(admin);
		try{
		garage.createBooth(1, new Location(5, 5), false);
		boothEntrance = garage.getNearestBooth(new Location(0, 0), false);
		
		garage.createBooth(2, new Location(25, 30), true);
		boothExit = garage.getNearestBooth(new Location(0, 0), true);
		}catch(Exception e){};
	}
	
	@Test public void testTicketButtonPressed()
	{
		assertFalse(boothEntrance.gateIsOpen());
		driver.goToEntrance();
    	driver.pushTicketButton(garage.getNearestBooth(driver.getLocation(), false), true);
    	driver.parkCar();
		assertTrue(driver.getTicket() != null);
		boothEntrance.ticketButtonPressed(driver2, false);
		assertTrue(driver2.getTicket() != null);
	}
	
	@Test public void testTicketButtonPressedNotEntrance()
	{
		boothExit.ticketButtonPressed(driver, false);
		assertFalse(boothExit.gateIsOpen());
		assertFalse(driver.isParked());
	}
	
	@Test public void testAdminTicketInserted()
	{
		driver.enterGarage();
		driver.goToExit();
		boothExit.login(admin);	
		BoothImpl booth = garage.getNearestBooth(driver.getLocation(), true);
		Float amountDue = booth.getAmountDue(driver.getTicket());
		booth.insertPayment(driver, driver.getTicket(), amountDue, false);
		driver.exitGarage();
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
		float neededAmount = 5 * rates.hourlyRate;
		assertTrue(boothAmount == neededAmount);
		
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*1000)); // 1000 hours ago
		Ticket ticket2 = new Ticket(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket2);
		boothAmount = boothExit.getAmountDue(ticket2);
		neededAmount = rates.maxCharge;
		assertTrue(boothAmount == neededAmount);
		
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*1/2)); // 30 minutes ago
		Ticket ticket3 = new Ticket(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket3);
		boothAmount = boothExit.getAmountDue(ticket3);
		System.out.println(boothAmount);
		neededAmount = rates.minCharge;
		assertTrue(boothAmount == neededAmount);
	}

}
