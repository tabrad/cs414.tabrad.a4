package common;

import java.util.Set;

import model.Location;

public interface Garage extends java.rmi.Remote
{
	public void simulate() throws java.rmi.RemoteException;

	public Object createDriver() throws java.rmi.RemoteException;
	
	public void createBooth(int boothId, Location location, Boolean isExit) throws java.rmi.RemoteException;
	
	public Booth getBooth(boolean isExit) throws java.rmi.RemoteException;

	public Object[][] getTableData(int granularity, boolean isFinancialReport) throws java.rmi.RemoteException;

	public Set<Location> getParkingStalls() throws java.rmi.RemoteException;

	public Set<Location> getRoad() throws java.rmi.RemoteException;

	public Driver findDriver(int x, int y) throws java.rmi.RemoteException;

	public boolean isFull() throws java.rmi.RemoteException;

	public int getMaxOccupancy() throws java.rmi.RemoteException;

	public int getOccupancy() throws java.rmi.RemoteException;

	public boolean isEntranceOpen() throws java.rmi.RemoteException;
	
	public boolean isExitOpen() throws java.rmi.RemoteException;

	public Set<Driver> getDrivers() throws java.rmi.RemoteException;

	public void removeVehicle(Location location) throws java.rmi.RemoteException;

	public Set<Location> getDriversLocations() throws java.rmi.RemoteException;
}
