package cs414.tabrad.a4;

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

	private Ticket getTicket() 
	{
		if(isExit || ticketTracker.isFull())
			return null;
		
		Ticket ticket = new Ticket(new Date(), boothId);
		ticketTracker.addTicket(ticket);
		
		return ticket;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void ticketButtonPressed(Driver driver)
	{
		Ticket ticket = getTicket();
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
		float amountDue = hoursParked * rates.hourlyRate;
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
		if(!ticketTracker.getGarage().isAdmin(admin))
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
		Admin admin = ticketTracker.getGarage().getAdmin();
		if(!admin.accessBooth(this))
			return;
		
		admin.settlePayment(this, driver, ticket);
	}

	public Ticket findTicket(String id) 
	{
		return ticketTracker.findTicket(id);
	}
}
