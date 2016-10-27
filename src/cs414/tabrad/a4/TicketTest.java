package cs414.tabrad.a4;

import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Test;

public class TicketTest {

	@Test public void testEquals() 
	{
		Date date = new Date();
		Ticket ticket = new Ticket(date, 1);
		Ticket ticket2 = new Ticket(date, 1);
		assertTrue(ticket.equals(ticket2));
		
		ticket.markPaid(2, 20f);
		assertTrue(ticket.equals(ticket2));
		
		Ticket ticket3 = new Ticket(new Date(), 0);
		assertFalse(ticket.equals(ticket3));
	}
	
	@Test public void testProcessedBy()
	{
		Ticket ticket = new Ticket(new Date(), 1);
		assertTrue(ticket.processedBy(1, false, false));
		assertTrue(ticket.processedBy(1, true, false));
		assertFalse(ticket.processedBy(2, false, false));
	}

}
