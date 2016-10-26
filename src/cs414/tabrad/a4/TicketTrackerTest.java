package cs414.tabrad.a4;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TicketTrackerTest {

	Garage garage;
	TicketTracker ticketTracker;
	
	
	@Before public void inititialize()
	{
		garage = new Garage();
		ticketTracker = new TicketTracker(garage);
	}
	
	@Test public void testAddingTickets() 
	{
		for(int i = 0; i < 10; i++)
		{
			Ticket ticket = new Ticket(new Date(), i);
			ticketTracker.addTicket(ticket);
			assertTrue(ticketTracker.hasUnpaidTicket(ticket));
		}
	}

	@Test public void testPayingTicket()
	{
		Ticket ticket = new Ticket(new Date(), 1);
		ticketTracker.addTicket(ticket);
		
		ticket.markPaid(1, 100f);
		ticketTracker.markTicketPaid(ticket);
		assertFalse(ticketTracker.hasUnpaidTicket(ticket));
		
		Set<Ticket> paidTickets = ticketTracker.getPaidTickets();
		assertTrue(paidTickets.contains(ticket));
	}
	
	@Test public void testGettingTicketsByBooth()
	{
		for(int i = 0; i < 10; i++)
		{
			Ticket ticket = new Ticket(new Date(), 1);
			ticketTracker.addTicket(ticket);
		}
		
		for(int i = 0; i < 10; i++)
		{
			Ticket ticket = new Ticket(new Date(), 2);
			ticketTracker.addTicket(ticket);
		}
		
		Set<Ticket> booth1Tickets = ticketTracker.getTicketsProcessedByBooth(1, false, false);
		assertTrue(booth1Tickets.size() == 10);
		
		booth1Tickets.clear();
		booth1Tickets = ticketTracker.getTicketsProcessedByBooth(1, true, false);
		assertTrue(booth1Tickets.size() == 10);
		
		booth1Tickets.clear();
		booth1Tickets = ticketTracker.getTicketsProcessedByBooth(1, false, true);
		assertTrue(booth1Tickets.size() == 0);	
	}
	
	@Test public void testGettingTicketsByTime()
	{
		for(int i = 0; i < 20; i++)
		{
			Ticket ticket = new Ticket(new Date(System.currentTimeMillis() - (1000 * 20) + (1000 * i)), 1);
			ticketTracker.addTicket(ticket);
		}
		
		Set<Ticket> tickets = ticketTracker.getTicketsByTimePeriod(new Date(System.currentTimeMillis() - (1000 * 10)), new Date());
		assertTrue(tickets.size() == 10);
	} 
	
	@Test public void testGetMostPopularHour()
	{
		Calendar calendar = Calendar.getInstance();
		
		for(int i = 0; i < 20; i++)
		{
			calendar.set(2016, 10, 10, 15, 5);
			Ticket ticket = new Ticket(calendar.getTime(), 1);
			ticketTracker.addTicket(ticket);
		}
		
		calendar.set(2016, 1, 1);
		assertTrue(ticketTracker.getMostPopularHour(calendar.getTime(), new Date()) == 15);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
