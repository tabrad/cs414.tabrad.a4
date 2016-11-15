package common;

import java.util.Set;

import model.Booth;
import model.Driver;
import model.Location;

public interface GarageBroker extends java.rmi.Remote 
{

	public void simulate() throws java.rmi.RemoteException;

	public Object createDriver() throws java.rmi.RemoteException;

	public Object[][] getTableData(int granularity, boolean isFinancialReport) throws java.rmi.RemoteException;

	public Set<Location> getParkingStalls() throws java.rmi.RemoteException;

	public Set<Location> getRoad() throws java.rmi.RemoteException;

	public Set<Booth> getBooths() throws java.rmi.RemoteException;

	public Driver findDriver(int x, int y) throws java.rmi.RemoteException;

	public Set<Driver> getDrivers() throws java.rmi.RemoteException;

	public boolean isFull() throws java.rmi.RemoteException;

}
