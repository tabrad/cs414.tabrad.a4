package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

import common.Booth;
import common.Location;
import common.Ticket;

public class BoothImpl extends Observable implements Booth
{
	private int boothId;
	private Location location;
	private boolean isExit;
	private boolean isOpen;
	private Rate rates = new Rate();
	private TicketTracker ticketTracker;
	private PaymentProcessor paymentProcessor = new PaymentProcessor();
	private Boolean adminMode = false;
	
	public BoothImpl(TicketTracker ticketTracker, int boothId, Location location, boolean isExit, Rate rates)
	{
		this.ticketTracker = ticketTracker;
		this.boothId = boothId;
		this.location = location;
		this.isExit = isExit;
		this.rates = rates;
		this.isOpen = false;
	}
	
	public int getId()
	{
		return boothId;
	}
	
	public Boolean isExit()
	{
		return isExit;
	}

	private Ticket getTicket(boolean isSimulation) throws RemoteException 
	{
		GarageImpl garage = GarageImpl.getInstance();
		if(isExit || garage.isFull())
			return null;

		Date date = new Date();
		if(isSimulation)
		{
			Calendar calendar = Calendar.getInstance();
			calendar.set(2016, (int)(Math.random() * 13), (int)(Math.random() * 28), (int)(Math.random() * 60), 0);
			date.setTime(calendar.getTimeInMillis());
		}
		
		Ticket t = new TicketImpl(date, boothId);
		Ticket ticket = (Ticket) UnicastRemoteObject.exportObject(t, 0); 	
		ticketTracker.addTicket(ticket);

		return ticket;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public Ticket ticketButtonPressed(boolean isSimulation) throws RemoteException
	{
		Ticket ticket = getTicket(isSimulation);
		if(ticket == null)
			return null;
		openGate();
		
		return ticket;
	}
	
	public float getAmountDue() 
	{
		return rates.maxCharge;
	}
	
	public float getAmountDue(String ticketId) throws RemoteException 
	{
		if(!ticketTracker.hasUnpaidTicket(ticketId))
			return rates.maxCharge;
		
		Ticket ticket = ticketTracker.findTicket(ticketId);
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
	
	public boolean insertPayment(String ticketId, float amount, boolean isCreditCard) throws RemoteException
	{
		if(!adminMode && amount < getAmountDue(ticketId))
			return false;
		
		if(!adminMode && !paymentProcessor.processPayment(isCreditCard))
			return false;
		
		Ticket ticket = ticketTracker.findTicket(ticketId);
		ticket.markPaid(boothId, amount);
		ticketTracker.markTicketPaid(ticket);
		openGate();
		
		return true;
	}
	
	public void openGate()
	{
		isOpen = true;
		setChanged();
		notifyObservers();
	}

	public void closeGate() 
	{
		isOpen = false;
		setChanged();
		notifyObservers();
	}

	public boolean login(Admin admin) 
	{
		if(!GarageImpl.isAdmin(admin))
			return false;
		
		adminMode = true;
		return true;
	}
	
	public void logout() 
	{
		adminMode = false;
	}
	
	public void requestAdmin(DriverImpl driver, Ticket ticket) 
	{
		Admin admin = GarageImpl.getAdmin();
		if(!admin.accessBooth(this))
			return;
	}

	public Ticket findTicket(String id) throws RemoteException 
	{
		return ticketTracker.findTicket(id);
	}

	public boolean gateIsOpen() 
	{
		return isOpen;
	}
}
