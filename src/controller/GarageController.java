package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import model.Garage;
import model.Location;
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
	private static OverviewDialog overviewDialog;
	private static GarageView garageView;
	
	//model
	private static Garage garage;
	
	public static void main(String[] args)
    {
        overviewDialog = new OverviewDialog();
        updateOverview();
        overviewDialog.showDialog();
        
        garageView = GarageView.getInstance();
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        frame.add(garageView);
        frame.setVisible(true);
    }
        
	private GarageController()
	{
		garage = Garage.getInstance();
	}
	
	public static GarageController getInstance()
	{
		if(instance == null)
		{
			instance = new GarageController();
			initialize();
		}
		
		return instance;
	}
	
	private static void initialize()
	{
		garage.createBooth(1, new Location(5, 5), false);
		garage.createBooth(1, new Location(10, 15), true);
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
		Location location = new Location(0,0);
		overviewDialog.update(garage.getTicketTracker().getOccupancy(), 
				garage.getMaxOccupancy(), 
				garage.getNearestBooth(location, false).getGate().isOpen(), 
				garage.getNearestBooth(location, true).getGate().isOpen());
	}
	
	public void simulate() 
	{
		garage.simulate();
	}

	public void createDriver() 
	{
		//update the driver dialog
		String license = "" + System.currentTimeMillis();
    	Driver driver = garage.createDriver(license);
    	DriverDialog driverDialog = new DriverDialog(license, driver.getLocation().x, driver.getLocation().y, driver.hasTicket(), driver.isParked());
    	driverDialog.showDialog();
    	
    	//add the garage view as an observer
    	driver.addObserver(garageView);
    	garageView.repaint();
	}

	public void reportsClicked() 
	{
		ReportDialog reportDialog = new ReportDialog();
        reportDialog.showDialog();
	}

	public Object[][] getTableData(int granularity, boolean isFinancialReport) 
	{
		return garage.getTicketTracker().getTableData(granularity, isFinancialReport);
	}
}
