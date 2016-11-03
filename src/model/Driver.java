package model;

import java.util.Observable;

public class Driver extends Observable
{
	private Garage garage;
	private String licensePlate;
	private Ticket myTicket = null;
	private Location location = new Location();
	private boolean isParked = false;
	
	public Driver(String licensePlate, int x, int y)
	{
		this.licensePlate = licensePlate;
		location.x = x;
		location.y = y;
		garage = Garage.getInstance();
	}
	
	public void pushTicketButton(Booth booth, boolean isSimulation)
	{
		booth.ticketButtonPressed(this, isSimulation);
	}
	
	public void setTicket(Ticket ticket) 
	{
		myTicket = ticket;	
	}
	
	public void goToEntrance()
	{
		Booth booth = garage.getNearestBooth(location, false);
		move(booth.getLocation());
	}
	
	public void goToExit() 
	{
		Booth booth = garage.getNearestBooth(location, true);
		move(booth.getLocation());
	}
	
	public void parkCar()
	{
		Location stall = garage.getOpenStall();
		move(stall);
		isParked = true;
	}
	
	public void enterGarage()
	{
		goToEntrance();
		pushTicketButton(garage.getNearestBooth(location, false), false);
		parkCar();
	}
	
	public void exitGarage() 
	{
		garage.removeVehicle(location);
	}
	
	public void move(Location location)
	{
		Location start = this.location;
		
		if(!garage.moveObject(this.toString(), this.location, location.x, location.y))
			return;
		
		int[] move = {start.x, start.y, location.x, location.y};
		setChanged();
		notifyObservers(move);
		System.out.println("notifying Observers");
		
		this.location = location;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	@Override public String toString()
	{
		return licensePlate;
	}

	public boolean isParked() 
	{
		return isParked;
	}
	
	public Ticket getTicket()
	{
		return myTicket;
	}

	public String getLicense() 
	{
		return licensePlate;
	}

	public boolean hasTicket() 
	{
		return myTicket != null;
	}
}
