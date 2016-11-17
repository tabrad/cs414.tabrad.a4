package common;

import model.Location;

public interface Driver extends java.rmi.Remote
{
	public void goToEntrance() throws java.rmi.RemoteException;
	
	public void goToExit() throws java.rmi.RemoteException;
	
	public void parkCar() throws java.rmi.RemoteException;
	
	public void exitGarage() throws java.rmi.RemoteException;
	
	public boolean isParked() throws java.rmi.RemoteException;
	
	public int getX() throws java.rmi.RemoteException;

	public int getY() throws java.rmi.RemoteException;
	
	public String getLicense() throws java.rmi.RemoteException;
	
	public boolean hasTicket() throws java.rmi.RemoteException;

	public Location getLocation() throws java.rmi.RemoteException;
	
	public void pushTicketButton(int boothId) throws java.rmi.RemoteException;
}
