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
		Location location = new Location();
		location.y = booth.getLocation().y - 1; //driver needs to be next to booth, not on top of it
		location.x = booth.getLocation().x;
		move(location);
	}
	
	public void goToExit() 
	{
		Booth booth = garage.getNearestBooth(location, true);
		Location location = new Location();
		location.y = booth.getLocation().y - 1; //driver needs to be next to booth, not on top of it
		location.x = booth.getLocation().x;
		move(location);
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
		garage.removeVehicle(location, this);
	}
	
	public void move(Location location)
	{
		if(!garage.moveObject(this.toString(), this.location, location.x, location.y))
			return;
		
		this.location = location;
		
		setChanged();
		notifyObservers();
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

	public int getX() 
	{
		return location.x;
	}

	public int getY() 
	{
		return location.y;
	}
}
