package server;

import java.rmi.Naming;

import common.Garage;

public class GarageServer 
{
	private String url;
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
		new GarageServer(url);
	}
}
