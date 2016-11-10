package controller;

import model.Booth;
import model.Driver;
import model.Garage;
import model.Ticket;
import view.DriverDialog;

public class BoothController 
{
	private static Garage garage = Garage.getInstance();
	
	public static float getAmountDue(String license, boolean lostTicket) 
	{
		Driver driver = garage.getDriver(license);
		Booth booth = garage.getNearestBooth(driver.getLocation(), true);
		float amountDue = 0;
		
		if(lostTicket)
			amountDue = booth.getAmountDue();
		else
			amountDue = booth.getAmountDue(driver.getTicket());
		
		return amountDue;
	}

	public static float getAmountDueByTicketId(String license, String id) 
	{	
		Driver driver = garage.getDriver(license);
		Booth booth = garage.getNearestBooth(driver.getLocation(), true);
		Ticket ticket = garage.getTicketTracker().findTicket(id);
		
		return booth.getAmountDue(ticket);
	}

	public static boolean findTicket(String id) 
	{
		return garage.getTicketTracker().findTicket(id) != null;
	}

	public static boolean insertPayment(DriverDialog dialog, String license, float amountDue, boolean isCreditCard) 
	{
		Driver driver = garage.getDriver(license);
		Booth booth = garage.getNearestBooth(driver.getLocation(), true);
		
		return booth.insertPayment(driver, driver.getTicket(), amountDue, isCreditCard);
	}
}
