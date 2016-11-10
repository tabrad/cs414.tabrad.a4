package model;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;


//garage is singleton
public class Garage extends Observable
{
	private static Garage instance = null;
	private static TicketTracker ticketTracker;
	Rate rates = new Rate(3, 3, 20);
	private int xSize = 20;
	private int ySize = 20;
	private HashSet<Location> parkingStalls;
	private HashSet<Location> road;
	private String[][] grid = new String[xSize][ySize];
	private HashSet<Booth> activeBooths = new HashSet<Booth>();
	private static HashSet<Admin> admins = new HashSet<Admin>();
	private HashSet<Driver> drivers = new HashSet<Driver>();

	private Garage(){}
	
	public static Garage getInstance()
	{
		if(instance == null)
		{
			instance = new Garage();
			ticketTracker = new TicketTracker();
		}
		
		return instance;
	}
	
	public void setParkingStalls(HashSet<Location> stalls)
	{
		parkingStalls = stalls;
	}
	
	public Set<Location> getParkingStalls()
	{
		return parkingStalls;
	}
	
	public void setRoad(HashSet<Location> roads)
	{
		road = roads;
	}
	
	public Set<Location> getRoad()
	{
		return road;
	}
	
	public boolean moveObject(String s, Location fromLocation, int toX, int toY)
	{
		if(grid[toX][toY] != null)
			return false;
		
		grid[toX][toY] = s;
		grid[fromLocation.x][fromLocation.y] = null;
		
		return true;
	}
	
	public void removeVehicle(Location location, Driver driver)
	{
		grid[location.x][location.y] = null;
		Booth booth = getNearestBooth(location, true);
		booth.closeGate();
		drivers.remove(driver);
		
		setChanged();
		notifyObservers();
	}
	
	public Boolean isClear(Location location)
	{
		return grid[location.x][location.y] == null;
	}
	
	public Driver createDriver(String license)
	{
		Driver driver = new Driver(license, 0, 0);
		drivers.add(driver);
		setChanged();
		notifyObservers();
		
		return driver;
	}
	
	public Driver getDriver(String license) 
	{
		for(Driver d : drivers)
		{
			if(d.getLicense() == license)
				return d;
		}
		
		return null;
	}
	
	public Booth createBooth(int boothId, Location location, Boolean isExit)
	{
		Booth booth = new Booth(ticketTracker, boothId, location, isExit, rates);
		activeBooths.add(booth);
		
		return booth;
	}
	
	public Set<Booth> getBooths()
	{
		return activeBooths;
	}
	
	public Booth getNearestBooth(Location location, boolean isExit)
	{
		Booth booth = null;
		int closestDistance = 0;
		
		for(Booth b : activeBooths)
		{	
			if(b.isExit() != isExit)
				continue;
			
			int boothDistance = Math.abs(b.getLocation().x - location.x) + Math.abs(b.getLocation().y - location.y);
			
			//if we have not selected a booth yet, use this booth
			if(closestDistance == 0)
			{
				booth = b;
				closestDistance = boothDistance;
				continue;
			}
			
			if(boothDistance < closestDistance)
				booth = b;
		}
		
		return booth;
	}

	public Location getOpenStall() 
	{	
		for(Location stall : parkingStalls)
		{	
			if(!isClear(stall))
				continue;
				
			return stall;
		}
		
		return null;
	}

	public void addAdmin(Admin admin)
	{
		admins.add(admin);
	}
	
	public static boolean isAdmin(Admin admin)
	{
		return admins.contains(admin);
	}

	public static Admin getAdmin() 
	{
		if(!admins.isEmpty())
		{
			for(Admin admin : admins)
			{
				//return the first available admin for now.
				return admin; 
			}
		}
		
		return null;
	}
	
	public int getMaxOccupancy() 
	{
		return parkingStalls.size();
	}

	public void simulate() 
	{
		for(int i = 0; i < 100; i++)
        {
        	Driver driver = createDriver("" + i);
        	driver.goToEntrance();
        	driver.pushTicketButton(getNearestBooth(driver.getLocation(), false), true);
        	driver.parkCar();
			getNearestBooth(driver.getLocation(), false).closeGate();
			
			driver.goToExit();
			Booth booth = getNearestBooth(driver.getLocation(), true);
			Float amountDue = booth.getAmountDue(driver.getTicket());
			booth.insertPayment(driver, driver.getTicket(), amountDue, false);
			driver.exitGarage();
        }
	}

	public TicketTracker getTicketTracker() 
	{
		return ticketTracker;
	}
	
	public int getOccupancy()
	{
		return ticketTracker.getOccupancy();
	}

	public boolean isFull() 
	{
		return ticketTracker.getOccupancy() >= getMaxOccupancy();
	}

	public Set<Driver> getDrivers() 
	{
		return drivers;
	}

	public boolean isEntranceOpen() 
	{
		Booth boothEntrance = getNearestBooth(new Location(0, 0), false);
		
		return boothEntrance.gateIsOpen();
	}
	
	public boolean isExitOpen() 
	{
		Booth boothExit = getNearestBooth(new Location(0, 0), true);
		
		return boothExit.gateIsOpen();
	}
}
