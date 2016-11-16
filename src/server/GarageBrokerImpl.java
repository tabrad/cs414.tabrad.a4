package server;

import java.util.HashSet;
import java.util.Set;

import common.GarageBroker;
import model.Driver;
import model.Location;

public class GarageBrokerImpl extends java.rmi.server.UnicastRemoteObject implements GarageBroker
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GarageImpl garage;
	
	public GarageBrokerImpl() throws java.rmi.RemoteException 
	{
		super();
		garage = GarageImpl.getInstance();
		initialize();
	}
	
	private void initialize()
	{
		//garage.createBooth(1, new Location(2, 2), false);
		//garage.createBooth(1, new Location(18, 2), true);
		
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
		garage.setRoad(roads);
		
		//setup parking stalls
		HashSet<Location> parkingStalls = new HashSet<Location>();
		mapLocationToColumn(3, 18, 6, parkingStalls);
		mapLocationToColumn(3, 18, 7, parkingStalls);
		mapLocationToColumn(3, 18, 10, parkingStalls);
		mapLocationToColumn(3, 18, 11, parkingStalls);
		mapLocationToColumn(3, 18, 14, parkingStalls);
		mapLocationToColumn(3, 18, 15, parkingStalls);
		garage.setParkingStalls(parkingStalls);
	}
	
	public void createBooth() throws java.rmi.RemoteException
	{
		
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

	public void simulate() 
	{
		garage.simulate();	
	}

	public Driver createDriver() 
	{
		String license = "" + System.currentTimeMillis();
		return garage.createDriver(license);
	}

	public Object[][] getTableData(int granularity, boolean isFinancialReport)
	{
		return garage.getTicketTracker().getTableData(granularity, isFinancialReport);
	}

	public Set<Location> getParkingStalls() 
	{
		return garage.getParkingStalls();
	}

	public Set<Location> getRoad() 
	{
		return garage.getRoad();
	}

	public Set<BoothImpl> getBooths() 
	{
		return garage.getBooths();
	}

	public Driver findDriver(int x, int y) 
	{
		return garage.findDriver(x, y);
	}

	public Set<Driver> getDrivers() 
	{
		return garage.getDrivers();
	}

	public boolean isFull() 
	{
		return garage.isFull();
	}
	
	public int getMaxOccupancy()
	{
		return garage.getMaxOccupancy();
	}
	
	public int getOccupancy()
	{
		return garage.getOccupancy();
	}
	
	public boolean isEntranceOpen()
	{
		return garage.isEntranceOpen();
	}
	
	public boolean isExitOpen()
	{
		return garage.isExitOpen();
	}
	
}
