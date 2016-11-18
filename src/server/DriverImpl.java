package server;

import java.rmi.RemoteException;

import common.Booth;
import common.Driver;
import common.Location;
import common.Ticket;

public class DriverImpl implements Driver
{
	private GarageImpl garage;
	private String license;
	private Ticket myTicket = null;
	private Location location = new Location();
	private boolean isParked = false;
	private boolean hasExited = false;
	
	public DriverImpl(String licensePlate, int x, int y) throws RemoteException
	{
		super();
		this.license = licensePlate;
		location.x = x;
		location.y = y;
		garage = GarageImpl.getInstance();
	}
	
	public void pushTicketButton(int boothId) throws RemoteException 
	{
		Ticket ticket = null;
		for(Booth booth : garage.getBooths())
		{
			if(booth.getId() == boothId)
			{
				ticket = booth.ticketButtonPressed(false);
				break;
			}
		}
		
		myTicket = ticket;
	}
	
	public void goToEntrance() throws RemoteException
	{
		Booth booth = garage.getBooth(false);
		Location location = new Location();
		location.y = booth.getLocation().y - 1; //driver needs to be next to booth, not on top of it
		location.x = booth.getLocation().x;
		move(location);
	}
	
	public void goToExit() throws RemoteException 
	{
		Booth booth = garage.getBooth(true);
		Location location = new Location();
		location.y = booth.getLocation().y - 1; //driver needs to be next to booth, not on top of it
		location.x = booth.getLocation().x;
		move(location);
	}
	
	public void parkCar() throws RemoteException
	{
		Location stall = garage.getOpenStall();
		move(stall);
		isParked = true;
		garage.closeEntranceGate();
	}
	
	public void exitGarage() throws RemoteException 
	{
		garage.removeVehicle(location, license);
		hasExited = true;
	}
	
	public void move(Location location)
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
		return license;
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
		return license;
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

	public String getTicketId() throws RemoteException 
	{
		return myTicket.getId();
	}

	public boolean hasExited() throws RemoteException 
	{
		return hasExited;
	}
}
