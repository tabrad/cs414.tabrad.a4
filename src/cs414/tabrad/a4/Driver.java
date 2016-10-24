package cs414.tabrad.a4;

public class Driver 
{
	private String licensePlate;
	private Ticket myTicket;
	private Location location = new Location();
	
	public Driver(String licensePlate, int x, int y)
	{
		this.licensePlate = licensePlate;
		location.x = x;
		location.y = y;
	}
	
	private void pushTicketButton(Booth booth)
	{
		this.myTicket = booth.getTicket();
	}
	
	public void enterGarage(Garage garage)
	{
		Booth booth = garage.getNearestBooth(location, false);
		pushTicketButton(booth);
		
		Location stall = garage.getOpenStall();
		System.out.println("Nearest Stall: " + stall.toString());
		move(garage, stall);
	}
	
	public void exitGarage(Garage garage) 
	{
		Booth booth = garage.getNearestBooth(location, true);
		float paymentDue = booth.getAmountDue(myTicket);
		
		System.out.println("PAyment Due: " + paymentDue);
		
		if(booth.payTicket(myTicket, paymentDue))
		{
			garage.removeVehicle(location);
			return;
		}
		else //payment failed we should just request an admin to handle this
		{
			booth.requestAdmin(this, myTicket);
		}
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
}
