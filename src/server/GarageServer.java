package server;

import java.rmi.Naming;

import common.GarageBroker;

public class GarageServer 
{
	private String url;
	public GarageServer(String url) {
		this.url = url;
		try {
			GarageBroker garageBroker = new GarageBrokerImpl();
			Naming.rebind(url, garageBroker);
			System.out.println("GarageServer running...");
		} catch (Exception e) {
			System.out.println("Trouble: " + e);
		}
	}

	// run the program using 
	// java CalculatorServer <host> <port>

	public static void main(String args[]) {
		String url = new String("rmi://localhost:2500/GarageServer");
		new GarageServer(url);
	}
	
	
}
