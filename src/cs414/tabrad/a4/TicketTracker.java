package cs414.tabrad.a4;

import java.util.HashSet;
import java.util.Set;

public class TicketTracker 
{
	private HashSet<Ticket> unPaidTickets = new HashSet<Ticket>();
	private HashSet<Ticket> paidTickets = new HashSet<Ticket>();
	
	public void addTicket(Ticket ticket)
	{
		unPaidTickets.add(ticket);
	}
	
	public boolean hasUnpaidTicket(Ticket ticket)
	{
		if(unPaidTickets.contains(ticket))
			return true;
		
		return false;
	}
	
	public void markTicketPaid(Ticket ticket)
	{
		paidTickets.add(ticket);
		unPaidTickets.remove(ticket);
	}
	
	public Set<Ticket> getPaidTickets()
	{
		return paidTickets;
	}

	public Set<Ticket> getTicketsProcessedByBooth(int boothId, boolean asEntrance, boolean asExit) 
	{
		HashSet<Ticket> tickets = new HashSet<Ticket>();
		
		for(Ticket ticket : unPaidTickets)
		{
			if(ticket.processedBy(boothId, asEntrance, asExit))
				tickets.add(ticket);
		}
		
		for(Ticket ticket : paidTickets)
		{
			if(ticket.processedBy(boothId, asEntrance, asExit))
				tickets.add(ticket);
		}
		
		return tickets;
	}
	
	public int getOccupancy()
	{
		return unPaidTickets.size();
	}
}
