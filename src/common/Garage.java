package common;

import java.rmi.RemoteException;
import java.util.Set;

public interface Garage extends java.rmi.Remote
{
	public void simulate() throws RemoteException;
	public Object[][] getTableData(int granularity, boolean isFinancialReport) throws RemoteException;
	public Set<Location> getParkingStalls() throws RemoteException;
	public Set<Location> getRoad() throws RemoteException;
	public boolean isFull() throws RemoteException;
	public int getMaxOccupancy() throws RemoteException;
	public int getOccupancy() throws RemoteException;
	public Driver createDriver() throws RemoteException;
	public Driver findDriver(int x, int y) throws RemoteException;
	public Set<Driver> getDrivers() throws RemoteException;
	public Set<Location> getDriversLocations() throws RemoteException;
	public Driver getDriver(String license) throws RemoteException;
	public void removeVehicle(Location location, String license, int boothId) throws RemoteException;
	public void createBooth(int boothId, Location location, Boolean isExit) throws RemoteException;
	public Booth getBoothById(int boothId) throws RemoteException;
	public Set<Booth> getBooths() throws RemoteException;
}
