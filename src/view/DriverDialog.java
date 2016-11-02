package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Booth;
import model.Driver;
import model.Garage;
import model.Ticket;

public class DriverDialog 
{
	private Garage garage;
	private Driver driver;
	
	private JFrame driverFrame;
	private JPanel infoPanel = new JPanel();
	private JLabel licenseLabel = new JLabel();
	private JLabel locationLabel = new JLabel();
	private JLabel parkedLabel = new JLabel();
	private JLabel ticketLabel = new JLabel();
	private JButton enterButton;
	private JButton exitButton;
	
	public DriverDialog(Garage garage, Driver driver)
	{
		this.garage = garage;
		this.driver = driver;
		prepareGUI();
	}
	
	private void prepareGUI()
	{
		driverFrame = new JFrame();
		driverFrame.setSize(250, 250);
		driverFrame.setLayout(new GridLayout(2, 1));
		
		infoPanel.setLayout(new GridLayout(5, 1));
		infoPanel.add(new JLabel("Driver", JLabel.CENTER));
		
		updateLabels();
		infoPanel.add(licenseLabel);
		infoPanel.add(locationLabel);
		infoPanel.add(ticketLabel);
		infoPanel.add(parkedLabel);
		
	    enterButton = new JButton("Enter Garage");
	    enterButton.addActionListener(new EnterGarageListener()); 
	    exitButton = new JButton("Exit Garage");
	    exitButton.addActionListener(new ExitGarageListener());
	    updateButtons();
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setSize(225, 100);
	    buttonPanel.setLayout(new GridLayout(1, 3));
	    buttonPanel.add(enterButton);
	    buttonPanel.add(exitButton);
	    
	    driverFrame.add(infoPanel);
	    driverFrame.add(buttonPanel);
	    driverFrame.setVisible(true);
	}
	
	private void updateButtons()
	{
		enterButton.setEnabled(!driver.isParked());
		exitButton.setEnabled(driver.isParked());
	}
	
	private void updateLabels()
	{
		licenseLabel.setText("Driver ID: " + driver.getLicense());
		locationLabel.setText("X-Location: " + driver.getLocation().x + " Y-Location: " + driver.getLocation().y);
		ticketLabel.setText("Has Ticket: " + (driver.hasTicket() ? "Yes" : "No"));
		parkedLabel.setText("Driver Parked: " + (driver.isParked() ? "Yes" : "No"));
	}
	
	private class EnterGarageListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{	
			driver.goToEntrance(garage);
			
			//show dialog that says driver drove to booth, have option to push ticket button or to leave
			JPanel message = new JPanel();
			message.add(new JLabel("Push Ticket Button?"));
			int result = JOptionPane.showConfirmDialog(driverFrame, message, "Entrance Booth", JOptionPane.YES_NO_OPTION);
			
			if(result == JOptionPane.YES_OPTION)
			{
				driver.pushTicketButton(garage.getNearestBooth(driver.getLocation(), false), false);
				
				if(!driver.hasTicket())
				{
					result = JOptionPane.showOptionDialog(driverFrame, 
							"The garage is full, do you want to wait for an open spot?", 
							"Entrance Booth", 
							JOptionPane.YES_NO_OPTION, 
							JOptionPane.INFORMATION_MESSAGE,
							null,
							new String[]{"Wait", "Leave"},
							"default");
					
					if(result == JOptionPane.NO_OPTION)
					{
						garage.removeVehicle(driver.getLocation());
						shutDialog();
					}
				}
				else
				{
					JOptionPane.showMessageDialog(driverFrame, "You may now enter the garage");
					driver.parkCar(garage);
					garage.getNearestBooth(driver.getLocation(), false).closeGate();
				}
			}
			
			updateLabels();
			updateButtons();
		}
	}
	
	private class ExitGarageListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Booth booth = garage.getNearestBooth(driver.getLocation(), true);
			driver.goToExit(garage);
			
			//show dialog that says driver drove to booth, have option to push ticket button or to leave
			int result = JOptionPane.showOptionDialog(driverFrame, 
					"Please Insert Your Ticket Before Exiting the Garage", 
					"Exit Booth", 
					JOptionPane.YES_NO_CANCEL_OPTION, 
					JOptionPane.INFORMATION_MESSAGE,
					null,
					new String[]{"Insert Ticket", "Lost Ticket", "Enter Ticket ID"},
					"default");
			
			float amountDue;
			if(result == JOptionPane.YES_OPTION)
				amountDue = booth.getAmountDue(driver.getTicket());
			else if(result == JOptionPane.CANCEL_OPTION)
			{
				String id = JOptionPane.showInputDialog("Enter Ticket ID");
				Ticket ticket = booth.findTicket(id);
				
				if(ticket == null)
				{
					JOptionPane.showMessageDialog(driverFrame, "Invalid Ticket. Maximum Charge.");
					amountDue = booth.getAmountDue();
				}
				else
				{
					amountDue = booth.getAmountDue(ticket);
				}
			}
			else
				amountDue = booth.getAmountDue();
			
			result = JOptionPane.showOptionDialog(driverFrame, 
					"You Owe " + amountDue + ". Please Select Payment", 
					"Exit Booth", 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.INFORMATION_MESSAGE,
					null,
					new String[]{"Cash", "Credit Card"},
					"default");
			
			if(result == JOptionPane.YES_OPTION)
			{
				booth.insertPayment(driver, driver.getTicket(), amountDue, false);
				JOptionPane.showMessageDialog(driverFrame, "Payment accepted. You may exit the garage.");
			}
			else
			{
				//add a delay here
				boolean isPaid = booth.insertPayment(driver, driver.getTicket(), amountDue, true);
				
				if(isPaid)
					JOptionPane.showMessageDialog(driverFrame, "Payment accepted. You may exit the garage");
				else
				{
					JOptionPane.showMessageDialog(driverFrame, "Payment failed.");
					return;
				}
			}
			driver.exitGarage(garage);
			shutDialog();
		}
	}

	public void showDialog() 
	{
		driverFrame.setVisible(true);
	}
	
	public void shutDialog()
	{
		driverFrame.dispose();
	}
}
