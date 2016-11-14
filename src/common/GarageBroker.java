package common;

import java.util.Set;

import model.Booth;
import model.Driver;
import model.Location;

public interface GarageBroker extends java.rmi.Remote 
{

	public void simulate();

	public Driver createDriver();

	public Object[][] getTableData(int granularity, boolean isFinancialReport);

	public Set<Location> getParkingStalls();

	public Set<Location> getRoad();

	public Set<Booth> getBooths();

	public Driver findDriver(int x, int y);

	public Set<Driver> getDrivers();

	public boolean isFull();

}
