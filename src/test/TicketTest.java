package test;

import static org.junit.Assert.*;

import java.rmi.RemoteException;
import java.util.Date;
import org.junit.Test;

import common.Ticket;
import server.TicketImpl;

public class TicketTest {

	@Test public void testEquals() throws RemoteException 
	{
		Date date = new Date();
		Ticket ticket = new TicketImpl(date, 1);
		Ticket ticket2 = new TicketImpl(date, 1);
		assertTrue(ticket.equals(ticket2));
		
		ticket.markPaid(2, 20f);
		assertTrue(ticket.equals(ticket2));
		
		Ticket ticket3 = new TicketImpl(new Date(), 0);
		assertFalse(ticket.equals(ticket3));
	}
	
	@Test public void testProcessedBy() throws RemoteException
	{
		Ticket ticket = new TicketImpl(new Date(), 1);
		assertTrue(ticket.processedBy(1, false, false));
		assertTrue(ticket.processedBy(1, true, false));
		assertFalse(ticket.processedBy(2, false, false));
	}

}
