package cs414.tabrad.a4;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TicketTrackerTest {

	TicketTracker ticketTracker;
	
	@Before public void inititialize()
	{
		ticketTracker = new TicketTracker();
	}
	
	@Test public void testAddingTickets() 
	{
		for(int i = 0; i < 10; i++)
		{
			Ticket ticket = new Ticket((int)System.currentTimeMillis(), i);
			ticketTracker.addTicket(ticket);
			assertTrue(ticketTracker.hasUnpaidTicket(ticket));
		}
	}

	@Test public void testPayingTicket()
	{
		Ticket ticket = new Ticket((int)10000, 1);
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
			Ticket ticket = new Ticket((int)System.currentTimeMillis(), 1);
			ticketTracker.addTicket(ticket);
		}
		
		for(int i = 0; i < 10; i++)
		{
			Ticket ticket = new Ticket((int)System.currentTimeMillis(), 2);
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
}
