package cs414.tabrad.a4;

import java.util.HashSet;

public class Garage 
{
	private int xSize = 100;
	private int ySize = 100;
	private Location parkingStart = new Location(15, 20);
	private Location parkingEnd = new Location(80, 80);
	private String[][] grid = new String[xSize][ySize];
	private HashSet<Booth> activeBooths = new HashSet<Booth>();
	
	public Boolean moveObject(String s, Location fromLocation, int toX, int toY)
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
		getBooth(location).closeGate();
	}
	
	private Booth getBooth(Location location)
	{
		for(Booth booth : activeBooths)
		{
			if(booth.getLocation().equals(location))
				return booth;
		}
		
		return null;
	}
	
	public Boolean isClear(Location location)
	{
		return grid[location.x][location.y] == null;
	}
	
	public Boolean isClear(int x, int y)
	{
		return grid[x][y] == null;
	}
	
	public Booth createBooth(TicketTracker ticketTracker, int boothId, Location location, Boolean isExit, Rate rates)
	{
		Booth booth = new Booth(ticketTracker, boothId, location, isExit, rates);
		activeBooths.add(booth);
		
		return booth;
	}
	
	public Booth getNearestBooth(Location location)
	{
		Booth booth = null;
		int distance = 0;
		
		for(Booth b : activeBooths)
		{	
			if(distance == 0)
			{
				booth = b;
				continue;
			}
			
			if(Math.abs(b.getLocation().x - location.x) + Math.abs(b.getLocation().y - location.y) < distance)
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
				if(!isClear(x, y))
					continue;
				
				return new Location(x, y);
			}
		}
		
		return null;
	}
}
