package server;

import java.rmi.Naming;

import common.Garage;

public class GarageServer 
{
	private String url;
	private static int maxOccupancy = 0;
	
	public GarageServer(String url) {
		this.url = url;
		try {
			
			Garage garage = GarageImpl.getInstance();
			Naming.rebind(url, garage);
			System.out.println("Garage running on " + this.url);
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}

	public static void main(String args[]) {
		String url = new String("rmi://" + args[0] + ":" + args[1] + "/Garage");
		
		if(args.length > 2)
			maxOccupancy = Integer.parseInt(args[2]);
		new GarageServer(url);
	}
	
	public static int getMaxOccupancy()
	{
		return maxOccupancy;
	}
}
