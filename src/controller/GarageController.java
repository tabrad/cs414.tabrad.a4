package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

import javax.swing.JFrame;

import common.Garage;
import model.Location;
import server.BoothImpl;
import model.Driver;
import view.DriverDialog;
import view.GarageView;
import view.OverviewDialog;
import view.ReportDialog;

// GarageController is singleton
public class GarageController implements ActionListener
{
	private static GarageController instance = null;
	
	//view
	//private static OverviewDialog overviewDialog;
	private static GarageView garageView;
	
	//model
	private static Garage garage;
	
	public static void main(String[] args)
    {
       // overviewDialog = new OverviewDialog();
        garageView = GarageView.getInstance();
        JFrame frame = new JFrame();
        frame.setSize(672, 672);
        frame.add(garageView);
        
        updateOverview();
        //overviewDialog.showDialog();
        frame.setVisible(true);
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
        	createDriver();
        }
		
		updateOverview();
	}
	
	public static void updateOverview()
	{	
		garageView.repaint();
	}
	
	public void simulate() 
	{
		try{
		garage.simulate();
		}catch(Exception e){}
		
	}

	public void createDriver() 
	{
		//update the driver dialog
		Driver driver = null;
		try{
    	 driver = (Driver)garage.createDriver();
		}catch(Exception e){}
	
    	DriverDialog driverDialog = new DriverDialog(driver);
    	driver.addObserver(driverDialog);
    	driverDialog.showDialog();
    	
    	//add the garage view as an observer
    	garageView.repaint();
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

	public Set<BoothImpl> getBooths() 
	{
		try{
		return garage.getBooths();
		}catch(Exception e){}
		return null;
	}

	public void garageClicked(int x, int y) 
	{
		try{
		Driver driver = garage.findDriver(x, y);
		if(driver == null)
			return;
		
		DriverDialog dialog = new DriverDialog(driver);
		dialog.showDialog();
		}catch(Exception e){}
		return;
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
}
