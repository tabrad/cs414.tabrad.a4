package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

import common.Booth;
import common.Driver;
import common.Garage;
import model.Location;
import view.DriverDialog;
import view.OverviewDialog;
import view.ReportDialog;

// GarageController is singleton
public class GarageController implements ActionListener
{
	private static GarageController instance = null;
	
	//view
	private static OverviewDialog overviewDialog;
	
	//model
	private static Garage garage;
	
	public static void main(String[] args) throws RemoteException
    {
        overviewDialog = new OverviewDialog();
        
        updateOverview();
        overviewDialog.showDialog();
    }
        
	private GarageController()
	{
		try{
			garage = (Garage) Naming.lookup("rmi://localhost:2500/Garage");
		} catch (MalformedURLException murle) {
            System.out.println("MalformedURLException");
            System.out.println(murle);
        } catch (RemoteException re) {
            System.out.println("RemoteException"); 
            System.out.println(re);
        } catch (NotBoundException nbe) {
            System.out.println("NotBoundException");
            System.out.println(nbe);
        } catch (java.lang.ArithmeticException ae) {
             System.out.println("java.lang.ArithmeticException");
             System.out.println(ae);
        }
	}
	
	public static GarageController getInstance()
	{
		if(instance == null)
		{
			instance = new GarageController();
		}
		
		return instance;
	}

	public void actionPerformed(ActionEvent e) 
	{
		String command = e.getActionCommand();
		
		if(command.equals("Reports"))  
        {
			reportsClicked();
        }
        else if (command.equals("Simulate")) 
        {
           simulate();
        } 
        else if (command.equals("New Car"))
        {
        	try{
        		createDriver();
        	}catch(Exception ee){}
        }
		
		updateOverview();
	}
	
	public static void updateOverview()
	{	
		overviewDialog.update();
	}
	
	public void simulate() 
	{
		try{
		garage.simulate();
		}catch(Exception e){}
		
	}

	public void createDriver() throws RemoteException 
	{
		//update the driver dialog
		Driver driver = null;
		try{
    	 driver = (Driver)garage.createDriver();
		}catch(Exception e){System.out.println("failed to create driver");}
	
    	DriverDialog driverDialog = new DriverDialog(driver, garage);
    	driverDialog.showDialog();
    	updateOverview();
	}

	public void reportsClicked() 
	{
		ReportDialog reportDialog = new ReportDialog();
        reportDialog.showDialog();
	}

	public Object[][] getTableData(int granularity, boolean isFinancialReport) 
	{
		try{
		return garage.getTableData(granularity, isFinancialReport);
		}catch(Exception e){}
		return null;
	}

	public Set<Location> getParkingStalls() 
	{
		try{
			return garage.getParkingStalls();
		}catch(Exception e){System.out.println("error getting stalls");}
		return null;
	}

	public Set<Location> getRoad() 
	{
		try{
		return garage.getRoad();
		}catch(Exception e){}
		return null;
	}
	
	public Booth getBooth(boolean isExit) throws RemoteException
	{
		return garage.getBooth(isExit);
	}

	public void garageClicked(int x, int y) throws RemoteException 
	{
		Driver driver = garage.findDriver(x, y);
		if(driver == null)
			return;
		
		DriverDialog dialog = new DriverDialog(driver, garage);
		dialog.showDialog();
	}

	public Set<Driver> getDrivers() 
	{
		try{
		return garage.getDrivers();
		}catch(Exception e){}
		return null;
	}
	
	public boolean isFull()
	{
		try{
		return garage.isFull();
		}catch(Exception e){}
		return false;
	}

	public int getMaxOccupancy() 
	{
		try{
			return garage.getMaxOccupancy();
		}catch(Exception e){System.out.println("failed to get max occupancy");}
		
		return 0;
	}

	public int getOccupancy() 
	{
		try{
			return garage.getOccupancy();
		}catch(Exception e){}
		
		return 0;
	}

	public boolean isEntranceOpen() 
	{
		try{
			return garage.isEntranceOpen();
		}catch(Exception e){}
		
		return false;
	}

	public boolean isExitOpen() 
	{
		try{
			return garage.isExitOpen();
		}catch(Exception e){}
		
		return false;
	}

	public Set<Location> getDriversLocations() throws RemoteException 
	{
		return garage.getDriversLocations();
	}

	public Driver getDriver(String license) throws RemoteException
	{
		return garage.getDriver(license);
	}

	public void removeVehicle(Location location, String license) throws RemoteException 
	{
		garage.removeVehicle(location, license);
	}
}
