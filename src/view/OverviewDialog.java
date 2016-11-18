package view;

import java.rmi.RemoteException;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
	private JLabel occupancyLabel;
	private JPanel controlPanel;
	private GarageView garageView;
	private int maxOccupancy;
	
	GarageController garageController;
	   
	public OverviewDialog() throws RemoteException
	{
		garageController = GarageController.getInstance();
		maxOccupancy = garageController.getMaxOccupancy();
		prepareGUI();
		update();
	}
	
	private void prepareGUI() throws RemoteException
    {
		 frame = new JFrame("Parking Garage");
	     //frame.setSize(1000,1000);
	     frame.setLayout(new GridBagLayout());
	     frame.addWindowListener(new WindowAdapter() {
	    	 public void windowClosing(WindowEvent windowEvent)
	    	 {
	    		 System.exit(0);
	         }        
	      });
	     
	     //use a gridboxlayout to control contents of the frame
	     GridBagConstraints c = new GridBagConstraints();
	     c.fill = GridBagConstraints.HORIZONTAL;
	     
	     c.gridx = 0;
	     c.gridy = 0;
	     headerLabel = new JLabel("", JLabel.CENTER);
	     headerLabel.setText("Parking Garage Overview");
	     frame.add(headerLabel, c);
	     
	     //add buttons to the frame
	     occupancyLabel = new JLabel();
	     controlPanel = new JPanel();
	     controlPanel.setLayout(new FlowLayout());	     
	     controlPanel.add(occupancyLabel);
	     c.gridy = 1;
	     frame.add(controlPanel, c);
	     
	     //add our main GUI "garageView"
	     garageView = GarageView.getInstance();
	     garageView.setSize(672, 672);
	     c.gridy = 2;
	     c.weighty = 1.0;
	     c.ipady = 672;
	     c.ipadx = 672;
	     frame.add(garageView, c);
	     frame.setSize(768, 768);
		 
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
    }
	
	public void update()
	{
		int occupancy = garageController.getOccupancy();
		occupancyLabel.setText("Occupancy: " + occupancy + " out of " + maxOccupancy + " vehicles.");
		garageView.repaint();
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
