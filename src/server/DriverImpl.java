package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.Booth;
import common.Driver;
import model.Location;
import model.Ticket;

public class DriverImpl implements Driver
{
	private static final long serialVersionUID = 1L;
	private GarageImpl garage;
	private String licensePlate;
	private Ticket myTicket = null;
	private Location location = new Location();
	private boolean isParked = false;
	
	public DriverImpl(String licensePlate, int x, int y) throws java.rmi.RemoteException
	{
		super();
		this.licensePlate = licensePlate;
		location.x = x;
		location.y = y;
		garage = GarageImpl.getInstance();
	}
	
	public void pushTicketButton(BoothImpl booth, boolean isSimulation) throws RemoteException
	{
		booth.ticketButtonPressed(this, isSimulation);
	}
	
	public void pushTicketButton(int boothId) throws RemoteException 
	{
		for(Booth booth : garage.getBooths())
		{
			if(booth.getId() == boothId)
				pushTicketButton((BoothImpl)booth, false);
		}
	}
	
	public void setTicket(Ticket ticket) 
	{
		myTicket = ticket;	
	}
	
	public void goToEntrance()
	{
		BoothImpl booth = (BoothImpl)garage.getNearestBooth(location, false);
		Location location = new Location();
		location.y = booth.getLocation().y - 1; //driver needs to be next to booth, not on top of it
		location.x = booth.getLocation().x;
		move(location);
	}
	
	public void goToExit() 
	{
		BoothImpl booth = (BoothImpl)garage.getNearestBooth(location, true);
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
	
	public void enterGarage() throws RemoteException
	{
		goToEntrance();
		pushTicketButton((BoothImpl)garage.getNearestBooth(location, false), false);
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
