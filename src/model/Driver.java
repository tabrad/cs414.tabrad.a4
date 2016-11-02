package model;

public class Driver 
{
	private String licensePlate;
	private Ticket myTicket = null;
	private Location location = new Location();
	private boolean isParked = false;
	
	public Driver(String licensePlate, int x, int y)
	{
		this.licensePlate = licensePlate;
		location.x = x;
		location.y = y;
	}
	
	public void pushTicketButton(Booth booth, boolean isSimulation)
	{
		booth.ticketButtonPressed(this, isSimulation);
	}
	
	public void setTicket(Ticket ticket) 
	{
		myTicket = ticket;	
	}
	
	public void goToEntrance(Garage garage)
	{
		Booth booth = garage.getNearestBooth(location, false);
		move(garage, booth.getLocation());
	}
	
	public void goToExit(Garage garage) 
	{
		Booth booth = garage.getNearestBooth(location, true);
		move(garage, booth.getLocation());
	}
	
	public void parkCar(Garage garage)
	{
		Location stall = garage.getOpenStall();
		move(garage, stall);
		isParked = true;
	}
	
	public void enterGarage(Garage garage)
	{
		goToEntrance(garage);
		pushTicketButton(garage.getNearestBooth(location, false), false);
		parkCar(garage);
	}
	
	public void exitGarage(Garage garage) 
	{
		garage.removeVehicle(location);
	}
	
	public void move(Garage garage, Location location)
	{
		if(!garage.moveObject(this.toString(), this.location, location.x, location.y))
			return;
		
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
