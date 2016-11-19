package common;

import java.rmi.RemoteException;

public interface Driver extends java.rmi.Remote
{
	public void goToBooth(int boothId) throws RemoteException;
	public void parkCar() throws RemoteException;
	public void exitGarage(int boothId) throws RemoteException;
	public boolean isParked() throws RemoteException;
	public int getX() throws RemoteException;
	public int getY() throws RemoteException;
	public String getLicense() throws RemoteException;
	public boolean hasTicket() throws RemoteException;
	public Location getLocation() throws RemoteException;
	public void pushTicketButton(int boothId, boolean isSimulation) throws RemoteException;
	public String getTicketId()throws RemoteException;
	public boolean hasExited() throws RemoteException;
}
