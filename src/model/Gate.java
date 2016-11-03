package model;

public class Gate 
{
	private Boolean isOpen = false;
	
	public Boolean isOpen() 
	{
		return isOpen;
	}
	
	public void open() 
	{
		isOpen = true;
	}
	
	public void close()
	{
		isOpen = false;
	}
}
