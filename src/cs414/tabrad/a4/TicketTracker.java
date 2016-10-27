package cs414.tabrad.a4;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public class TicketTracker extends Observable
{
	private Garage garage;
	private HashSet<Ticket> unPaidTickets = new HashSet<Ticket>();
	private HashSet<Ticket> paidTickets = new HashSet<Ticket>();
	
	public TicketTracker(Garage garage)
	{
		this.garage = garage;
	}
	
	public void addTicket(Ticket ticket)
	{
		unPaidTickets.add(ticket);
		setChanged();
		notifyObservers();
	}
	
	public Ticket findTicket(String id)
	{
		for(Ticket ticket : unPaidTickets)
		{
			if(ticket.getId().equals(id))
				return ticket;
		}
		
		return null;
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
		setChanged();
		notifyObservers();
	}
	
	public Set<Ticket> getPaidTickets()
	{
		return paidTickets;
	}
	
	public int getOccupancy()
	{
		return unPaidTickets.size();
	}

	public Garage getGarage() 
	{
		return garage;
	}
	
	public Set<Ticket> getAllTickets()
	{
		HashSet<Ticket> tickets = unPaidTickets;
		tickets.addAll(paidTickets);
		
		return tickets;
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
	
	public Set<Ticket> getTicketsByTimePeriod(Date begin, Date end)
	{
		HashSet<Ticket> tickets = new HashSet<Ticket>();
		for(Ticket ticket : unPaidTickets)
		{
			if(ticket.getTimeEntered().before(begin) || ticket.getTimeEntered().after(end))
				continue;
			
			tickets.add(ticket);
		}
		
		for(Ticket ticket : paidTickets)
		{
			if(ticket.getTimeEntered().before(begin) || ticket.getTimeEntered().after(end))
				continue;
			
			tickets.add(ticket);
		}

		return tickets;
	}
	
	private int getMax(int[] list)
	{
		int max = 0;
		
		for(int i = 0; i < list.length; i++)
		{
			if(list[i] > max)
				max = i;
		}
		
		return max;
	}
	
	public int getMostPopularHour(Date begin, Date end)
	{
		Set<Ticket> tickets = getAllTickets();
		int[] ticketHour = new int[24];
		
		Calendar calendar = Calendar.getInstance();
		for(Ticket ticket : tickets)
		{
			calendar.setTime(ticket.getTimeEntered());
			ticketHour[calendar.get(Calendar.HOUR_OF_DAY)]++;
		}

		return getMax(ticketHour);
	}
	
	public boolean isFull()
	{
		return getOccupancy() == garage.getMaxOccupancy();
	}

	public Date getEarliestDate() 
	{
		Set<Ticket> tickets = getAllTickets();
		Date earliestDate = new Date();
		for(Ticket ticket : tickets)
		{
			if(ticket.getTimeEntered().before(earliestDate))
				earliestDate = ticket.getTimeEntered();
		}
		
		return earliestDate;
	}
	
	public Date getLatestDate()
	{
		Set<Ticket> tickets = getAllTickets();
		Date latestDate = new Date();
		latestDate.setTime(0);
		
		for(Ticket ticket : tickets)
		{
			if(ticket.getTimeEntered().after(latestDate))
				latestDate = ticket.getTimeEntered();
		}
		
		return latestDate;
	}
}
