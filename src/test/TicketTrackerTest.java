package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import common.Ticket;
import server.GarageImpl;
import server.TicketImpl;
import server.TicketTracker;

public class TicketTrackerTest {

	GarageImpl garage;
	TicketTracker ticketTracker;
	
	
	@Before public void inititialize() throws RemoteException
	{
		garage = GarageImpl.getInstance();
		ticketTracker = new TicketTracker();
	}
	
	@Test public void testAddingTickets() throws RemoteException 
	{
		for(int i = 0; i < 10; i++)
		{
			Ticket ticket = new TicketImpl(new Date(), i);
			ticketTracker.addTicket(ticket);
			assertTrue(ticketTracker.hasUnpaidTicket(ticket.getId()));
		}
	}

	@Test public void testPayingTicket() throws RemoteException
	{
		Ticket ticket = new TicketImpl(new Date(), 1);
		ticketTracker.addTicket(ticket);
		
		ticket.markPaid(1, 100f);
		ticketTracker.markTicketPaid(ticket);
		assertFalse(ticketTracker.hasUnpaidTicket(ticket.getId()));
		
		Set<Ticket> paidTickets = ticketTracker.getPaidTickets();
		assertTrue(paidTickets.contains(ticket));
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
