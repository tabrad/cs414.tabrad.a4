package controller;

import java.rmi.RemoteException;

import common.Booth;

public class BoothController 
{
	private static GarageController garageController = GarageController.getInstance();
	
	public static float getAmountDue(String ticketId, boolean lostTicket) throws RemoteException 
	{
		Booth booth = garageController.getBooth(true);
		float amountDue = 0;
		
		if(lostTicket)
			amountDue = booth.getAmountDue();
		else
			amountDue = booth.getAmountDue(ticketId);
		
		return amountDue;
	}

	public static boolean findTicket(String id) throws RemoteException 
	{
		Booth booth = garageController.getBooth(true);
		return booth.findTicket(id) != null;
	}

	public static boolean insertPayment(String ticketId, float amountDue, boolean isCreditCard) throws RemoteException 
	{
		Booth booth = garageController.getBooth(true);
		return booth.insertPayment(ticketId, amountDue, isCreditCard);
	}
}
