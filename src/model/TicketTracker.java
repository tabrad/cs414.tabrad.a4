package model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

import common.Ticket;

public class TicketTracker extends Observable
{
	private HashSet<Ticket> unPaidTickets = new HashSet<Ticket>();
	private HashSet<Ticket> paidTickets = new HashSet<Ticket>();
	
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
	
	public int getOccupancy()
	{
		return unPaidTickets.size();
	}
	
	public Set<Ticket> getPaidTickets()
	{
		return paidTickets;
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

	public Object[][] getTableData(int granularity, boolean isFinancialReport) 
	{
		Object[][] data = null;
		Calendar calendar = Calendar.getInstance();
		Set<Ticket> tickets = getAllTickets();

		if(granularity == 0) //day
		{
			float days[] = {0f,0f,0f,0f,0f,0f,0f};
			for(Ticket ticket : tickets)
        	{
				calendar.setTime(ticket.getTimeEntered());
				int day = calendar.get(Calendar.DAY_OF_WEEK);
				days[day - 1] = (isFinancialReport ? ticket.getPaymentAmount() : 1) + days[day - 1];
        	}
			
			//populate the data object that we put into the JTable
			data = new Object[][] {{0f,0f,0f,0f,0f,0f,0f}};
			for(int i = 0; i < days.length; i++)
				data[0][i] = days[i];
		}
		else if(granularity == 1) // week
		{
			float weeks[] = {0f, 0f, 0f, 0f, 0f, 0f};
			for(Ticket ticket : tickets)
			{
				calendar.setTime(ticket.getTimeEntered());
				int week = calendar.get(Calendar.WEEK_OF_MONTH);
				
				// the week of month setting can return 0
				if(week < 1)
					week = 1;
				
				weeks[week - 1] = (isFinancialReport ? ticket.getPaymentAmount() : 1) + weeks[week -1];
			}
			
			//populate the data object that we put into the JTable
			data = new Object[][] {{0f, 0f, 0f, 0f, 0f, 0f}};
			for(int i = 0; i < weeks.length; i++)
				data[0][i] = weeks[i];
		}
		else // month
		{
			float months[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
			for(Ticket ticket : tickets)
			{
				calendar.setTime(ticket.getTimeEntered());
				int month = calendar.get(Calendar.MONTH); //note month starts at 0, where the days and weeks start at 1
				months[month] = (isFinancialReport ? ticket.getPaymentAmount() : 1) + months[month]; 
			}
			
			//populate the data object that we put into the JTable
			data = new Object[][] {{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f}};
			for(int i = 0; i < months.length; i++)
				data[0][i] = months[i];
		}
		
		return data;
	}
	
	

}
