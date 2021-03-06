package server;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import common.Ticket;

public class TicketTracker
{
	private HashSet<Ticket> unPaidTickets = new HashSet<Ticket>();
	private HashSet<Ticket> paidTickets = new HashSet<Ticket>();
	
	public void addTicket(Ticket ticket)
	{
		unPaidTickets.add(ticket);
	}
	
	public Ticket findTicket(String id) throws RemoteException
	{
		for(Ticket ticket : unPaidTickets)
		{
			if(ticket.getId().equals(id))
				return ticket;
		}
		
		return null;
	}
	
	public boolean hasUnpaidTicket(String ticketId) throws RemoteException
	{
		for(Ticket ticket : unPaidTickets)
		{
			if(ticket.getId().equals(ticketId))
				return true;
		}
		
		return false;
	}
	
	public void markTicketPaid(Ticket ticket)
	{
		paidTickets.add(ticket);
		unPaidTickets.remove(ticket);
	}
	
	public int getOccupancy()
	{
		return unPaidTickets.size();
	}
	
	public Set<Ticket> getPaidTickets()
	{
		return paidTickets;
	}
	
	public Object[][] getTableData(int granularity, boolean isFinancialReport) throws RemoteException 
	{
		Object[][] data = null;
		Calendar calendar = Calendar.getInstance();
		Set<Ticket> tickets = paidTickets;

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
