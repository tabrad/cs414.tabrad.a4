package model;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

public class Booth extends Observable 
{
	private int boothId;
	private Location location = new Location();
	private Boolean isExit;
	private Gate gate = new Gate();
	private Rate rates = new Rate();
	private TicketTracker ticketTracker;
	private PaymentProcessor paymentProcessor = new PaymentProcessor();
	private Boolean adminMode = false;
	
	public Booth(TicketTracker ticketTracker, int boothId, Location location, Boolean isExit, Rate rates)
	{
		this.ticketTracker = ticketTracker;
		this.boothId = boothId;
		this.location = location;
		this.isExit = isExit;
		this.rates = rates;
	}
	
	public int getId()
	{
		return boothId;
	}
	
	public Boolean isExit()
	{
		return isExit;
	}

	private Ticket getTicket(boolean isSimulation) 
	{
		if(isExit || Garage.isFull())
			return null;

		Date date = new Date();
		if(isSimulation)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set(2016, (int)(Math.random() * 13), (int)(Math.random() * 28), (int)(Math.random() * 60), 0);
			date.setTime(calendar.getTimeInMillis());
		}
		
		Ticket ticket = new Ticket(date, boothId);
		ticketTracker.addTicket(ticket);
		
		return ticket;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void ticketButtonPressed(Driver driver, boolean isSimulation)
	{
		Ticket ticket = getTicket(isSimulation);
		if(ticket == null)
			return;
		driver.setTicket(ticket);
		openGate();
	}
	
	public float getAmountDue() 
	{
		return rates.maxCharge;
	}
	
	public float getAmountDue(Ticket ticket) 
	{
		if(!ticketTracker.hasUnpaidTicket(ticket))
			return rates.maxCharge;
		
		long currentTime = new Date().getTime() / 1000;
		long ticketTime = ticket.getTimeEntered().getTime() / 1000;
		long hoursParked = (currentTime - ticketTime) / 60 / 60;
		
		return calculateAmountDue(hoursParked);
	}
	
	private float calculateAmountDue(long hours)
	{
		float amountDue = hours * rates.hourlyRate;
		if(amountDue < 0)
			return rates.maxCharge; // wrapped the data structure boundary
		
		if(amountDue > rates.maxCharge)
			amountDue = rates.maxCharge;
		else if(amountDue < rates.minCharge)
			amountDue = rates.minCharge;
		
		return amountDue;
	}
	
	public boolean insertPayment(Driver driver, Ticket ticket, float amount, boolean isCreditCard)
	{
		if(!adminMode && amount != getAmountDue(ticket))
			return false;
		
		if(!adminMode && !paymentProcessor.processPayment(isCreditCard))
			return false;
		
		ticket.markPaid(boothId, amount);
		ticketTracker.markTicketPaid(ticket);
		openGate();
		
		return true;
	}
	
	public Gate getGate() 
	{
		return gate;
	}
	
	public void openGate()
	{
		gate.open();
		setChanged();
		notifyObservers();
	}

	public void closeGate() 
	{
		gate.close();
		setChanged();
		notifyObservers();
	}

	public boolean login(Admin admin) 
	{
		if(!Garage.isAdmin(admin))
			return false;
		
		adminMode = true;
		return true;
	}
	
	public void logout() 
	{
		adminMode = false;
	}
	
	public void requestAdmin(Driver driver, Ticket ticket) 
	{
		Admin admin = Garage.getAdmin();
		if(!admin.accessBooth(this))
			return;
	}

	public Ticket findTicket(String id) 
	{
		return ticketTracker.findTicket(id);
	}
}
