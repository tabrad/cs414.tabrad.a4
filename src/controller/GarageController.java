package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;

import model.Garage;
import model.Location;
import model.Booth;
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
        garageView = GarageView.getInstance();
        JFrame frame = new JFrame();
        frame.setSize(672, 672);
        frame.add(garageView);
        
        updateOverview();
        overviewDialog.showDialog();
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
		garage.createBooth(1, new Location(2, 2), false);
		garage.createBooth(1, new Location(18, 2), true);
		
		//setup road locations
		HashSet<Location> roads = new HashSet<Location>();
		mapLocationToColumn(1, 20, 4, roads);
		mapLocationToColumn(1, 20, 5, roads);
		mapLocationToColumn(1, 20, 8, roads);
		mapLocationToColumn(1, 20, 9, roads);
		mapLocationToColumn(1, 20, 12, roads);
		mapLocationToColumn(1, 20, 13, roads);
		mapLocationToColumn(1, 20, 16, roads);
		mapLocationToColumn(1, 20, 17, roads);
		mapLocationToRow(1, 20, 1, roads);
		mapLocationToRow(4, 17, 2, roads);
		mapLocationToRow(4, 17, 19, roads);
		mapLocationToRow(4, 17, 20, roads);
		garage.setRoad(roads);
		
		//setup parking stalls
		HashSet<Location> parkingStalls = new HashSet<Location>();
		mapLocationToColumn(3, 18, 6, parkingStalls);
		mapLocationToColumn(3, 18, 7, parkingStalls);
		mapLocationToColumn(3, 18, 10, parkingStalls);
		mapLocationToColumn(3, 18, 11, parkingStalls);
		mapLocationToColumn(3, 18, 14, parkingStalls);
		mapLocationToColumn(3, 18, 15, parkingStalls);
		garage.setParkingStalls(parkingStalls);
	}
	
	private static void mapLocationToColumn(int yStart, int yEnd, int xColumn, HashSet<Location> locations)
	{
		for(int y = yStart; y < yEnd + 1; y++)
		{
			Location location = new Location(xColumn, y);
			locations.add(location);
		}
	}
	
	private static void mapLocationToRow(int xStart, int xEnd, int yRow, HashSet<Location> locations)
	{
		for(int x = xStart; x < xEnd + 1; x++)
		{
			Location location = new Location(x, yRow);
			locations.add(location);
		}
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
		garage.simulate();
	}

	public void createDriver() 
	{
		//update the driver dialog
		String license = "" + System.currentTimeMillis();
    	Driver driver = garage.createDriver(license);
    	DriverDialog driverDialog = new DriverDialog(driver);
    	driver.addObserver(driverDialog);
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

	public Set<Location> getParkingStalls() 
	{
		return garage.getParkingStalls();
	}

	public Set<Location> getRoad() {
		// TODO Auto-generated method stub
		return garage.getRoad();
	}

	public Set<Booth> getBooths() 
	{
		return garage.getBooths();
	}

	public void garageClicked(int x, int y) 
	{
		for(Driver driver : garage.getDrivers())
		{		
			if(x != driver.getX())
				continue;
			
			if(y != driver.getY())
				continue;
			
			DriverDialog dialog = new DriverDialog(driver);
			driver.addObserver(dialog);
			driver.addObserver(garageView);
			dialog.showDialog();
			return;
		}
		
	}

	public Set<Driver> getDrivers() 
	{
		return garage.getDrivers();
	}
}
