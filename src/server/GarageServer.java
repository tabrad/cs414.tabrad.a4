package server;

import java.rmi.Naming;

import common.Garage;

public class GarageServer 
{
	private String url;
	public GarageServer(String url) {
		this.url = url;
		try {
			Garage garage = new GarageImpl();
			Naming.rebind(url, garage);
			System.out.println("Garage running...");
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}

	// run the program using 
	// java CalculatorServer <host> <port>

	public static void main(String args[]) {
		String url = new String("rmi://localhost:2500/Garage");
		new GarageServer(url);
	}
	
	
}
