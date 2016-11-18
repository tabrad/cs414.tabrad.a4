package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Set;

import common.Booth;
import common.Driver;
import common.Garage;
import common.Location;


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
	private HashSet<Booth> activeBooths = new HashSet<Booth>();
	private static HashSet<Admin> admins = new HashSet<Admin>();
	private HashSet<Driver> drivers = new HashSet<Driver>();

	private GarageImpl() throws RemoteException 
	{
		super();
		initialize();
	}
	
	private void initialize()
	{
		ticketTracker = new TicketTracker();
		try{
			createBooth(1, new Location(2, 2), false);
			createBooth(2, new Location(18, 2), true);
		}catch(Exception e){System.out.println("failed to create booths");}
		
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
		road = roads;
		
		//setup parking stalls
		HashSet<Location> stalls = new HashSet<Location>();
		mapLocationToColumn(3, 18, 6, stalls);
		mapLocationToColumn(3, 18, 7, stalls);
		mapLocationToColumn(3, 18, 10, stalls);
		mapLocationToColumn(3, 18, 11, stalls);
		mapLocationToColumn(3, 18, 14, stalls);
		mapLocationToColumn(3, 18, 15, stalls);
		this.parkingStalls = stalls;
		
		int max = GarageServer.getMaxOccupancy();
		//if set to 0 we will give full stalls
		if(max == 0)
			return;
		
		//go through stall set and only keep the number of stalls requested
		HashSet<Location> parkingStallsNew = new HashSet<Location>();
		for(Location stall : parkingStalls)
		{
			if(parkingStallsNew.size() > (max - 1))
				break;
			
			parkingStallsNew.add(stall);
		}
		parkingStalls = parkingStallsNew;
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
	
	public static GarageImpl getInstance() throws RemoteException
	{	
		if(instance == null)
			instance = new GarageImpl();
		
		return instance;
	}
	
	public Set<Location> getParkingStalls()
	{
		return parkingStalls;
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
	
	public void removeVehicle(Location location, String license) throws RemoteException
	{
		grid[location.x][location.y] = null;
		Booth booth = getBooth(true);
		booth.closeGate();
		
		//remove driver
		Driver temp = null;
		for(Driver d : drivers)
		{
			if(d.getLicense().equals(license))
				temp = d;
		}
		drivers.remove(temp);
	}
	
	public Boolean isClear(Location location)
	{
		return grid[location.x][location.y] == null;
	}
	
	public Driver createDriver() throws RemoteException
	{
		Driver d = new DriverImpl("" + System.currentTimeMillis(), 0, 0);
		Driver driver = (Driver)UnicastRemoteObject.exportObject(d, 0);
		drivers.add(driver);	
		if(driver == null)
			System.out.println("driver is null");
		return driver;
	}
	
	public Driver getDriver(String license) throws RemoteException 
	{
		for(Driver d : drivers)
		{
			if(d.getLicense() == license)
				return d;
		}
		
		return null;
	}
	
	public Driver findDriver(int x, int y) throws RemoteException 
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

	public void createBooth(int boothId, Location location, Boolean isExit) throws RemoteException
	{
		Booth b = new BoothImpl(ticketTracker, boothId, location, isExit, rates);
		Booth booth = (Booth) UnicastRemoteObject.exportObject(b, 0); 		
		activeBooths.add(booth);
	}
	
	public Booth getBooth(boolean isExit) throws RemoteException
	{
		for(Booth booth : activeBooths)
		{
			if(booth.isExit() == isExit)
			{
				return booth;
			}		
		}
		
		System.out.println("failed to find booth");
		return null;
	}
	
	public Set<Booth> getBooths()
	{
		return activeBooths;
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

	public void simulate() throws RemoteException 
	{
//		for(int i = 0; i < 100; i++)
//        {
//        	Driver driver = createDriver("" + i);
//        	driver.goToEntrance();
//        	driver.pushTicketButton(getNearestBooth(driver.getLocation(), false), true);
//        	driver.parkCar();
//			getNearestBooth(driver.getLocation(), false).closeGate();
//			
//			driver.goToExit();
//			BoothImpl booth = getNearestBooth(driver.getLocation(), true);
//			Float amountDue = booth.getAmountDue(driver.getTicket());
//			booth.insertPayment(driver, driver.getTicket(), amountDue, false);
//			driver.exitGarage();
//        }
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

	public void closeEntranceGate() throws RemoteException 
	{
		Booth booth = getBooth(false);
		booth.closeGate();
	}
	
	public boolean isEntranceOpen() throws RemoteException 
	{
		Booth boothEntrance = getBooth(false);
		return boothEntrance.gateIsOpen();
	}
	
	public boolean isExitOpen() throws RemoteException 
	{
		Booth boothExit = getBooth(true);
		return boothExit.gateIsOpen();
	}

	public Object[][] getTableData(int granularity, boolean isFinancialReport) throws RemoteException 
	{
		return getTicketTracker().getTableData(granularity, isFinancialReport);
	}

	public Set<Driver> getDrivers() throws RemoteException 
	{
		Set<Driver> s = new HashSet<Driver>();
		for(Driver d : drivers)
		{
			s.add(d);
		}
		
		return s;
	}

	public Set<Location> getDriversLocations() throws RemoteException 
	{
		Set<Location> locations = new HashSet<Location>();
		for(Driver d : drivers)
		{
			locations.add(d.getLocation());
		}
		
		return locations;
	}

	
}
