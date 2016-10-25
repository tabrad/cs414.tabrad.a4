package cs414.tabrad.a4;

public class Driver 
{
	private String licensePlate;
	private Ticket myTicket;
	private Location location = new Location();
	private boolean isParked = false;
	
	public Driver(String licensePlate, int x, int y)
	{
		this.licensePlate = licensePlate;
		location.x = x;
		location.y = y;
	}
	
	private void pushTicketButton(Booth booth)
	{
		booth.ticketButtonPressed(this);
	}
	
	public void setTicket(Ticket ticket) 
	{
		myTicket = ticket;	
	}
	
	public void promptPayment(Booth booth, float amountDue) 
	{
		if(!booth.insertPayment(this, myTicket, amountDue))
				booth.requestAdmin(this, myTicket);
	}
	
	public void promtExit(Garage garage)
	{
		isParked = false;
		garage.removeVehicle(location);
	}
	
	public void enterGarage(Garage garage)
	{
		Booth booth = garage.getNearestBooth(location, false);
		move(garage, booth.getLocation());
		pushTicketButton(booth);
		
		Location stall = garage.getOpenStall();
		move(garage, stall);
		isParked = true;
	}
	
	public void exitGarage(Garage garage) 
	{
		Booth booth = garage.getNearestBooth(location, true);
		move(garage, booth.getLocation());
		booth.insertTicket(this, myTicket);
	}
	
	public void move(Garage garage, Location location)
	{
		move(garage, location.x, location.y);
	}
	
	public void move(Garage garage, int x, int y)
	{
		if(!garage.moveObject(this.toString(), location, x, y))
			return;
		
		location.x = x;
		location.y = y;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	@Override public String toString()
	{
		return licensePlate;
	}

	public float pay(float amountDue) 
	{
		return amountDue;
	}

	public boolean isParked() 
	{
		return isParked;
	}
	
	public Ticket getTicket()
	{
		return myTicket;
	}
}
