package model;

public class Location 
{
	public int x;
	public int y;
	
	public Location()
	{
		x = 0;
		y = 0;
	}
	
	public Location(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	@Override public boolean equals(Object o)
	{
		return ((Location)o).x == x && ((Location)o).y == y;
	}
	
	@Override public String toString()
	{
		return "X Location: " + x + " Y Location: " + y;
	}
}
