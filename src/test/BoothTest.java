package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import common.Booth;
import common.Driver;
import common.Location;
import common.Ticket;
import server.Admin;
import server.BoothImpl;
import server.GarageImpl;
import server.Rate;
import server.TicketImpl;
import server.TicketTracker;


public class BoothTest {

	GarageImpl garage;
	TicketTracker ticketTracker;
	Booth boothEntrance;
	Booth boothExit;
	Rate rates;
	Driver driver;
	Driver driver2;
	Admin admin;
	
	@Before public void initialize() throws RemoteException
	{
		garage = GarageImpl.getInstance();
		rates = new Rate(3, 3, 20);
		ticketTracker = new TicketTracker();
		driver = garage.createDriver();
		driver2 = garage.createDriver();
		admin = new Admin("jfiwkls", "or023kf9");
		garage.addAdmin(admin);
		try{
			garage.createBooth(1, new Location(5, 5), false);
			boothEntrance = garage.getBooth(false);
			
			garage.createBooth(2, new Location(25, 30), true);
			boothExit = garage.getBooth(true);
		}catch(Exception e){};
	}
	
	@Test public void testTicketButtonPressed() throws RemoteException
	{
		assertFalse(boothEntrance.gateIsOpen());
		driver.goToEntrance();
    	driver.pushTicketButton(garage.getBooth(true).getId());
    	driver.parkCar();
		assertFalse(driver.hasTicket());
	}
	
	@Test public void testTicketButtonPressedNotEntrance() throws RemoteException
	{
		boothExit.ticketButtonPressed(false);
		assertFalse(boothExit.gateIsOpen());
		assertFalse(driver.isParked());
	}
	
	@Test public void testTicketCharge() throws RemoteException
	{
		Calendar calendar = Calendar.getInstance();
		Date currentDate = new Date();
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*5)); // five hours ago
		Ticket ticket = new TicketImpl(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket);
		
		BoothImpl boothTemp = new BoothImpl(ticketTracker, 4, new Location(0,0), true, rates);
		float boothAmount = boothTemp.getAmountDue(ticket.getId());
		float neededAmount = 5 * rates.hourlyRate;
		System.out.println("booth:"+boothAmount);
		assertTrue(boothAmount == neededAmount);
		
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*1000)); // 1000 hours ago
		Ticket ticket2 = new TicketImpl(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket2);
		boothAmount = boothTemp.getAmountDue(ticket2.getId());
		neededAmount = rates.maxCharge;
		assertTrue(boothAmount == neededAmount);
		
		calendar.setTimeInMillis(currentDate.getTime() - (1000*60*60*1/2)); // 30 minutes ago
		Ticket ticket3 = new TicketImpl(calendar.getTime(), 1);
		ticketTracker.addTicket(ticket3);
		boothAmount = boothTemp.getAmountDue(ticket3.getId());
		System.out.println(boothAmount);
		neededAmount = rates.minCharge;
		assertTrue(boothAmount == neededAmount);
	}

}
