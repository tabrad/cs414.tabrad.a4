package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import common.Driver;
import common.Garage;

import controller.BoothController;
import controller.DriverController;

public class DriverDialog extends Dialog
{
	//representation of the Driver model
	private Driver driver;
	private Garage garage;
	
	//Controller
	private DriverController driverController; 
	
	//view elements
	private JPanel infoPanel = new JPanel();
	private JLabel licenseLabel = new JLabel();
	private JLabel locationLabel = new JLabel();
	private JLabel parkedLabel = new JLabel();
	private JLabel ticketLabel = new JLabel();
	private JButton enterButton;
	private JButton exitButton;
	
	Timer timer = new Timer();
	
	public DriverDialog(Driver driver, Garage garage) throws RemoteException
	{		
		this.driver = driver;
		this.garage = garage;
		driverController = new DriverController(driver, garage);

		frame = new JFrame();
		frame.setSize(350, 250);
		frame.setLayout(new GridLayout(2, 1));
		
		infoPanel.setLayout(new GridLayout(5, 1));
		infoPanel.add(new JLabel("Driver", JLabel.CENTER));
		
		infoPanel.add(licenseLabel);
		infoPanel.add(locationLabel);
		infoPanel.add(ticketLabel);
		infoPanel.add(parkedLabel);
		
	    enterButton = new JButton("Enter Garage");
	    enterButton.addActionListener(new EnterGarageListener()); 
	    exitButton = new JButton("Exit Garage");
	    exitButton.addActionListener(new ExitGarageListener());
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setSize(225, 100);
	    buttonPanel.setLayout(new GridLayout(1, 3));
	    buttonPanel.add(enterButton);
	    buttonPanel.add(exitButton);
	    
	    frame.add(infoPanel);
	    frame.add(buttonPanel);
	    
	    timer.scheduleAtFixedRate(new TimerTask() {
	        @Override
	        public void run() {
	            try {
					update();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }, 1000, 1000);
	    
	    updateLabels();
		updateButtons();
	}
	
	public void update() throws RemoteException 
	{
		updateLabels();
		updateButtons();
	}
	
	private void updateButtons() throws RemoteException
	{
		enterButton.setEnabled(!driver.isParked());
		exitButton.setEnabled(driver.isParked());
	}
	
	private void updateLabels() throws RemoteException
	{
		licenseLabel.setText("Driver ID: " + driver.getLicense());
		locationLabel.setText("X-Location: " + driver.getX() + " Y-Location: " + driver.getY());
		ticketLabel.setText("Has Ticket: " + (driver.hasTicket() ? "Yes" : "No"));
		parkedLabel.setText("Driver Parked: " + (driver.isParked() ? "Yes" : "No"));
	}
	
	private class EnterGarageListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			try{
				System.out.println("going to move to entrance");
				driverController.moveDriverToEntrance();
				
				//show dialog that says driver drove to booth, have option to push ticket button or to leave
				JPanel message = new JPanel();
				message.add(new JLabel("Push Ticket Button?"));
				int result = JOptionPane.showConfirmDialog(frame, message, "Entrance Booth", JOptionPane.YES_NO_OPTION);
				
				if(result == JOptionPane.YES_OPTION)
				{
					driverController.pushTicketButton(garage.getBooth(false).getId());
					
					if(!driver.hasTicket())
					{
						result = JOptionPane.showOptionDialog(frame, 
								"The garage is full, do you want to wait for an open spot?", 
								"Entrance Booth", 
								JOptionPane.YES_NO_OPTION, 
								JOptionPane.INFORMATION_MESSAGE,
								null,
								new String[]{"Wait", "Leave"},
								"default");
						
						if(result == JOptionPane.NO_OPTION)
						{
							driverController.driverPrematureExit();
							shutDialog();
						}
					}
					else
					{
						JOptionPane.showMessageDialog(frame, "You may now enter the garage");
						driverController.parkCar();
					}
				}
				
				updateLabels();
				updateButtons();
			}catch(Exception ee){ee.printStackTrace();}
		}
	}
	
	private class ExitGarageListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			try {
				driverController.moveDriverToExit();
			
			
				//show dialog that says driver drove to booth, have option to push ticket button or to leave
				int result = JOptionPane.showOptionDialog(frame, 
						"Please Insert Your Ticket Before Exiting the Garage", 
						"Exit Booth", 
						JOptionPane.YES_NO_CANCEL_OPTION, 
						JOptionPane.INFORMATION_MESSAGE,
						null,
						new String[]{"Insert Ticket", "Lost Ticket", "Enter Ticket ID"},
						"default");
				
				float amountDue;
				if(result == JOptionPane.YES_OPTION) //driver inserts ticket
					amountDue = BoothController.getAmountDue(driver.getTicketId(), false);
				else if(result == JOptionPane.CANCEL_OPTION) //driver manually enters ticket id
				{
					String id = JOptionPane.showInputDialog("Enter Ticket ID");
					
					boolean isValid = BoothController.findTicket(id);
					
					if(!isValid)
					{
						JOptionPane.showMessageDialog(frame, "Invalid Ticket. Maximum Charge.");
						amountDue = BoothController.getAmountDue(driver.getTicketId(), true);
					}
					else
					{
						amountDue = BoothController.getAmountDue(id, true);
					}
				}
				else //driver lost ticket
					amountDue = BoothController.getAmountDue(driver.getTicketId(), true);
				
				result = JOptionPane.showOptionDialog(frame, 
						"You Owe " + amountDue + ". Please Select Payment", 
						"Exit Booth", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.INFORMATION_MESSAGE,
						null,
						new String[]{"Cash", "Credit Card"},
						"default");
				
				if(result == JOptionPane.YES_OPTION)
				{
					BoothController.insertPayment(driver.getTicketId(), amountDue, false);
					JOptionPane.showMessageDialog(frame, "Payment accepted. You may exit the garage.");
				}
				else
				{
					boolean isPaid = BoothController.insertPayment(driver.getTicketId(), amountDue, true);
					
					if(isPaid)
						JOptionPane.showMessageDialog(frame, "Payment accepted. You may exit the garage");
					else
					{
						JOptionPane.showMessageDialog(frame, "Payment failed.");
						return;
					}
				}
				driverController.driverExit();
				shutDialog();
			} catch (RemoteException e1) {}
		}
		
	}
}
