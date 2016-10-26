package cs414.tabrad.a4;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * 
 * @author Tom
 *	NOTE: the structure of the swing elements is largely inspired by the swing tutorial on tutorialspoint
 *	https://www.tutorialspoint.com/swing/swing_event_handling.htm
 */


public class OverviewDialog implements Observer
{
	private JFrame mainFrame;
	private JLabel headerLabel;
	private JLabel statusLabel;
	private JLabel boothGateLabel;
	private JLabel occupancyLabel;
	private JPanel controlPanel;
	
	private Garage garage;
	private Booth entranceBooth;
	private Booth exitBooth;
	private TicketTracker ticketTracker;
	private Rate rates; 
	   
	public OverviewDialog()
	{
		prepareModel();
		prepareGUI();
	}
	
	@Override
	public void update(Observable observable, Object arg)
	{
		updateLabels();
	}
	
	public static void main(String[] args)
    {
        OverviewDialog overviewDialog = new OverviewDialog();
        overviewDialog.showDialog();
    }
	
	private void prepareModel()
	{
		garage = new Garage();
		rates = new Rate(3, 3, 20);
		
		ticketTracker = new TicketTracker(garage);
		ticketTracker.addObserver(this);
		
		entranceBooth = garage.createBooth(ticketTracker, 1, new Location(5, 5), false, rates);
		entranceBooth.addObserver(this);
		exitBooth = garage.createBooth(ticketTracker, 1, new Location(10, 15), true, rates);
		exitBooth.addObserver(this);
	}
	
	private void prepareGUI()
    {
		 mainFrame = new JFrame("Java SWING Examples");
	     mainFrame.setSize(400,400);
	     mainFrame.setLayout(new GridLayout(7, 1));

	     headerLabel = new JLabel("",JLabel.CENTER );
	     statusLabel = new JLabel("",JLabel.CENTER);
	     occupancyLabel = new JLabel();
	     boothGateLabel = new JLabel();

	     statusLabel.setSize(350,100);
	     mainFrame.addWindowListener(new WindowAdapter() {
	    	 public void windowClosing(WindowEvent windowEvent)
	    	 {
	    		 System.exit(0);
	         }        
	      });    
	      
	     controlPanel = new JPanel();
	     controlPanel.setLayout(new FlowLayout());	     

	     mainFrame.add(headerLabel);
	     mainFrame.add(occupancyLabel);
	     mainFrame.add(boothGateLabel);
	     mainFrame.add(controlPanel);
	     mainFrame.add(statusLabel);
	     
	     mainFrame.setVisible(true);  
    }
	
	private void updateLabels()
	{
		occupancyLabel.setText("Occupancy: " + ticketTracker.getOccupancy() + " out of " + garage.getMaxOccupancy() + " vehicles.");
		boothGateLabel.setText("Entrance Gate: " + (entranceBooth.getGate().isOpen() ? "Open " : "Closed ") + "     Exit Gate: " + (exitBooth.getGate().isOpen() ? "Open" : "Closed"));
	}
	
	private void showDialog()
	{
		 headerLabel.setText("Parking Garage Overview");
		 updateLabels();

		 JButton newCarButton = new JButton("New Car");
		 JButton submitButton = new JButton("Submit");
		 JButton cancelButton = new JButton("Cancel");
		
		 newCarButton.setActionCommand("New Car");
		 submitButton.setActionCommand("Submit");
		 cancelButton.setActionCommand("Cancel");
		
		 newCarButton.addActionListener(new ButtonClickListener()); 
		 submitButton.addActionListener(new ButtonClickListener()); 
		 cancelButton.addActionListener(new ButtonClickListener()); 
		
		 controlPanel.add(newCarButton);
		 controlPanel.add(submitButton);
		 controlPanel.add(cancelButton);
		 
		 mainFrame.setVisible(true);  
	}
	
	private class ButtonClickListener implements ActionListener
	{
      public void actionPerformed(ActionEvent e) 
      {
         String command = e.getActionCommand();  
         if(command.equals( "New Car" ))  
         {
            showNewCarDialog();
         }
         else if( command.equals( "Submit" ) )  
         {
            statusLabel.setText("Submit Button clicked."); 
         }
         else  
         {
            statusLabel.setText("Cancel Button clicked.");
         }  	
      }		
	}
	
	private void showNewCarDialog()
	{
		JPanel message = new JPanel();           
        message.add(new JLabel("Create a New Car?"));
        int result = JOptionPane.showConfirmDialog(mainFrame, message, "Create Car", JOptionPane.YES_NO_OPTION);
	
        if(result == JOptionPane.YES_OPTION)
        {
        	String license = ""+System.currentTimeMillis();
        	Driver driver = garage.createDriver(license);

        	DriverDialog driverDialog = new DriverDialog(garage, driver);
        	driverDialog.showDialog();
        }
	}
}
