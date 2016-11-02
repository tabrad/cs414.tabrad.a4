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


public class OverviewDialog extends Dialog
{
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
	
	private void prepareController()
	{
		garageController = GarageController.getInstance();
	}
	
	private void prepareGUI()
    {
		 frame = new JFrame("Parking Garage");
	     frame.setSize(400,400);
	     frame.setLayout(new GridLayout(7, 1));
	     frame.addWindowListener(new WindowAdapter() {
	    	 public void windowClosing(WindowEvent windowEvent)
	    	 {
	    		 System.exit(0);
	         }        
	      });
	     
	     //Elements of the main frame
	     headerLabel = new JLabel("", JLabel.CENTER);
	     headerLabel.setText("Parking Garage Overview");
	     statusLabel = new JLabel("", JLabel.CENTER);
	     statusLabel.setSize(350,100);
	     occupancyLabel = new JLabel();
	     boothGateLabel = new JLabel();
	     signLabel = new JLabel();
	     controlPanel = new JPanel();
	     controlPanel.setLayout(new FlowLayout());	     

	     frame.add(headerLabel);
	     frame.add(signLabel);
	     frame.add(occupancyLabel);
	     frame.add(boothGateLabel);
	     frame.add(controlPanel);
	     frame.add(statusLabel);
		 
	     //Buttons
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
	     
		 //initialize the labels and show the dialog
		 update(0,0,false, false);
		 showDialog();
    }
	
	public void update(int occupancy, int maxOccupancy, boolean isEntranceOpen, boolean isExitOpen)
	{
		occupancyLabel.setText("Occupancy: " + occupancy + " out of " + maxOccupancy + " vehicles.");
		signLabel.setText("Garage is: " + (occupancy >= maxOccupancy ? "FULL" : "NOT FULL"));
		boothGateLabel.setText("Entrance Gate: " + (isEntranceOpen ? "Open " : "Closed ") + "     Exit Gate: " + (isExitOpen ? "Open" : "Closed"));
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
             int result = JOptionPane.showConfirmDialog(frame, message, "Create Car", JOptionPane.YES_NO_OPTION);
     	
             if(result == JOptionPane.YES_OPTION)
             {
             	ActionEvent event = new ActionEvent(this, 0, "New Car");
             	garageController.actionPerformed(event);
             }
         }	
      }		
	}
}
