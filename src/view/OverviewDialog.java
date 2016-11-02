package view;

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

import controller.GarageController;


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
	private JLabel signLabel;
	private JPanel controlPanel;
	
	GarageController garageController;
	   
	public OverviewDialog()
	{
		prepareController();
		prepareGUI();
	}
	
	@Override
	public void update(Observable observable, Object arg)
	{
		updateLabels();
	}
	
	private void prepareController()
	{
		garageController = GarageController.getInstance();
	}
	
	private void prepareGUI()
    {
		 mainFrame = new JFrame("Parking Garage");
	     mainFrame.setSize(400,400);
	     mainFrame.setLayout(new GridLayout(7, 1));

	     headerLabel = new JLabel("",JLabel.CENTER );
	     statusLabel = new JLabel("",JLabel.CENTER);
	     occupancyLabel = new JLabel();
	     boothGateLabel = new JLabel();
	     signLabel = new JLabel();

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
	     mainFrame.add(signLabel);
	     mainFrame.add(occupancyLabel);
	     mainFrame.add(boothGateLabel);
	     mainFrame.add(controlPanel);
	     mainFrame.add(statusLabel);
	     
	     mainFrame.setVisible(true);  
    }
	
	private void updateLabels()
	{
	//	occupancyLabel.setText("Occupancy: " + ticketTracker.getOccupancy() + " out of " + garage.getMaxOccupancy() + " vehicles.");
	//	signLabel.setText("Garage is: " + (ticketTracker.getOccupancy() == garage.getMaxOccupancy() ? "FULL" : "NOT FULL"));
	//	boothGateLabel.setText("Entrance Gate: " + (entranceBooth.getGate().isOpen() ? "Open " : "Closed ") + "     Exit Gate: " + (exitBooth.getGate().isOpen() ? "Open" : "Closed"));
	}
	
	public void showDialog()
	{
		 headerLabel.setText("Parking Garage Overview");
		 updateLabels();

		 JButton newCarButton = new JButton("New Car");
		 JButton submitButton = new JButton("Reports");
		 JButton cancelButton = new JButton("Simulate");
		
		 newCarButton.setActionCommand("New Car");
		 submitButton.setActionCommand("Reports");
		 cancelButton.setActionCommand("Simulate");
		
		 newCarButton.addActionListener(new ButtonClickListener()); 
		 submitButton.addActionListener(garageController); 
		 cancelButton.addActionListener(garageController); 
		
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
        	 JPanel message = new JPanel();           
             message.add(new JLabel("Create a New Car?"));
             int result = JOptionPane.showConfirmDialog(mainFrame, message, "Create Car", JOptionPane.YES_NO_OPTION);
     	
             if(result == JOptionPane.YES_OPTION)
             {
             	ActionEvent event = new ActionEvent(this, 0, "New Car");
             	garageController.actionPerformed(event);
             }
         }	
      }		
	}
}
