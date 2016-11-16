package server;

import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import common.Booth;
import common.Garage;
import model.Admin;
import model.Driver;
import model.Location;
import model.Rate;
import model.TicketTracker;


//garage is singleton
public class GarageImpl extends UnicastRemoteObject implements Garage
{
	private static final long serialVersionUID = 1L;
	private static GarageImpl instance = null;
	private static TicketTracker ticketTracker;
	Rate rates = new Rate(3, 3, 20);
	private int xSize = 20;
	private int ySize = 20;
	private HashSet<Location> parkingStalls;
	private HashSet<Location> road;
	private String[][] grid = new String[xSize][ySize];
	private HashSet<BoothImpl> activeBooths = new HashSet<BoothImpl>();
	private static HashSet<Admin> admins = new HashSet<Admin>();
	private HashSet<Driver> drivers = new HashSet<Driver>();

	public GarageImpl() throws java.rmi.RemoteException 
	{
		super();
		//garage = GarageImpl.getInstance();
		initialize();
	}
	
	private void initialize()
	{
		try{
		createBooth(1, new Location(2, 2), false);
		createBooth(1, new Location(18, 2), true);
		}catch(Exception e){}
		
		//setup road locations
		HashSet<Location> roads = new HashSet<Location>();
		mapLocationToColumn(1, 20, 4, roads);
		mapLocationToColumn(1, 20, 5, roads);
		mapLocationToColumn(1, 20, 8, roads);
		mapLocationToColumn(1, 20, 9, roads);
		mapLocationToColumn(1, 20, 12, roads);
		mapLocationToColumn(1, 20, 13, roads);
		mapLocationToColumn(1, 20, 16, roads);
		mapLocationToColumn(1, 20, 17, roads);
		mapLocationToRow(1, 20, 1, roads);
		mapLocationToRow(4, 17, 2, roads);
		mapLocationToRow(4, 17, 19, roads);
		mapLocationToRow(4, 17, 20, roads);
		setRoad(roads);
		
		//setup parking stalls
		HashSet<Location> parkingStalls = new HashSet<Location>();
		mapLocationToColumn(3, 18, 6, parkingStalls);
		mapLocationToColumn(3, 18, 7, parkingStalls);
		mapLocationToColumn(3, 18, 10, parkingStalls);
		mapLocationToColumn(3, 18, 11, parkingStalls);
		mapLocationToColumn(3, 18, 14, parkingStalls);
		mapLocationToColumn(3, 18, 15, parkingStalls);
		setParkingStalls(parkingStalls);
		
		ticketTracker = new TicketTracker();
	}
	
	private static void mapLocationToColumn(int yStart, int yEnd, int xColumn, HashSet<Location> locations)
	{
		for(int y = yStart; y < yEnd + 1; y++)
		{
			Location location = new Location(xColumn, y);
			locations.add(location);
		}
	}
	
	private static void mapLocationToRow(int xStart, int xEnd, int yRow, HashSet<Location> locations)
	{
		for(int x = xStart; x < xEnd + 1; x++)
		{
			Location location = new Location(x, yRow);
			locations.add(location);
		}
	}
	
	public static GarageImpl getInstance()
	{	
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
		BoothImpl booth = getNearestBooth(location, true);
		booth.closeGate();
		drivers.remove(driver);
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

	public void createBooth(int boothId, Location location, Boolean isExit) throws java.rmi.RemoteException
	{
		BoothImpl b = new BoothImpl(ticketTracker, boothId, location, isExit, rates);
		BoothImpl booth = (BoothImpl) UnicastRemoteObject.exportObject(b, 0); 		
		activeBooths.add(booth);
	}
	
	public Booth getBooth(boolean isExit) throws java.rmi.RemoteException
	{
		for(Booth booth : activeBooths)
		{
			if(booth.isExit() == isExit)
				return booth;
		}
		
		return null;
	}
	
	public Set<BoothImpl> getBooths()
	{
		return activeBooths;
	}
	
	public BoothImpl getNearestBooth(Location location, boolean isExit)
	{
		BoothImpl booth = null;
		int closestDistance = 0;
		
		for(BoothImpl b : activeBooths)
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
			BoothImpl booth = getNearestBooth(driver.getLocation(), true);
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
		BoothImpl boothEntrance = getNearestBooth(new Location(0, 0), false);
		
		return boothEntrance.gateIsOpen();
	}
	
	public boolean isExitOpen() 
	{
		BoothImpl boothExit = getNearestBooth(new Location(0, 0), true);
		
		return boothExit.gateIsOpen();
	}

	public Driver findDriver(int x, int y) 
	{
		for(Driver driver : drivers)
		{		
			if(x != driver.getX())
				continue;
			
			if(y != driver.getY())
				continue;
			
			return driver;
		}
		return null;
	}

	public Driver createDriver() 
	{
		return createDriver("" + System.currentTimeMillis());
	}

	public Object[][] getTableData(int granularity, boolean isFinancialReport) 
	{
		return getTicketTracker().getTableData(granularity, isFinancialReport);
	}
}
