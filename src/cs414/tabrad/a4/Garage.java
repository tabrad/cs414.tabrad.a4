package cs414.tabrad.a4;

import java.util.HashSet;

public class Garage
{
	private int xSize = 100;
	private int ySize = 100;
	private Location parkingStart = new Location(15, 20);
	private Location parkingEnd = new Location(80, 80);
	private int maxOccupancy = 3;
	private String[][] grid = new String[xSize][ySize];
	private HashSet<Booth> activeBooths = new HashSet<Booth>();
	private HashSet<Admin> admins = new HashSet<Admin>();
	private HashSet<Driver> drivers = new HashSet<Driver>();

	public boolean moveObject(String s, Location fromLocation, int toX, int toY)
	{
		if(grid[toX][toY] != null)
			return false;
		
		grid[toX][toY] = s;
		grid[fromLocation.x][fromLocation.y] = null;
		
		return true;
	}
	
	public void removeVehicle(Location location)
	{
		grid[location.x][location.y] = null;
		Booth booth = getNearestBooth(location, true);
		booth.closeGate();
	}
	
	public Boolean isClear(Location location)
	{
		return grid[location.x][location.y] == null;
	}
	
	public Driver createDriver(String license)
	{
		Driver driver = new Driver(license, 0, 0);
		drivers.add(driver);
		
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
	
	public Booth createBooth(TicketTracker ticketTracker, int boothId, Location location, Boolean isExit, Rate rates)
	{
		Booth booth = new Booth(ticketTracker, boothId, location, isExit, rates);
		activeBooths.add(booth);
		
		return booth;
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
		for(int x = parkingStart.x; x < parkingEnd.x; x++)
		{	
			for(int y = parkingStart.y; y < parkingEnd.y; y++)
			{
				if(!isClear(new Location(x, y)))
					continue;
				
				return new Location(x, y);
			}
		}
		
		return null;
	}

	public void addAdmin(Admin admin)
	{
		admins.add(admin);
	}
	
	public boolean isAdmin(Admin admin)
	{
		return admins.contains(admin);
	}

	public Admin getAdmin() 
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
		return maxOccupancy;
	}

	public void simulate() 
	{
		for(int i = 0; i < 100; i++)
        {
        	Driver driver = createDriver("" + i);
        	driver.goToEntrance(this);
        	driver.pushTicketButton(getNearestBooth(driver.getLocation(), false), true);
        	driver.parkCar(this);
			getNearestBooth(driver.getLocation(), false).closeGate();
			
			driver.goToExit(this);
			Booth booth = getNearestBooth(driver.getLocation(), true);
			Float amountDue = booth.getAmountDue(driver.getTicket());
			booth.insertPayment(driver, driver.getTicket(), amountDue, false);
			driver.exitGarage(this);
        }
		
	}
}
