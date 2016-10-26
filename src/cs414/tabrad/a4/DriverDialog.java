package cs414.tabrad.a4;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class DriverDialog 
{
	private Garage garage;
	private Driver driver;
	private JFrame driverFrame;
	private JPanel infoPanel = new JPanel();
	private JLabel licenseLabel = new JLabel();
	private JLabel locationLabel = new JLabel();
	private JLabel parkedLabel = new JLabel();
	
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
		infoPanel.add(parkedLabel);
		
	    
	    JButton enterButton = new JButton("Enter Garage");
	    enterButton.addActionListener(new EnterGarageListener());
	    enterButton.setActionCommand(driver.getLicense());
	    
	    JButton exitButton = new JButton("Exit Garage");
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setSize(225, 100);
	    buttonPanel.setLayout(new GridLayout(1, 3));
	    buttonPanel.add(enterButton);
	    buttonPanel.add(exitButton);
	    
	    driverFrame.add(infoPanel);
	    driverFrame.add(buttonPanel);
	    driverFrame.setVisible(true);
	}
	
	private void updateLabels()
	{
		licenseLabel.setText("Driver ID: " + driver.getLicense());
		locationLabel.setText("X-Location: " + driver.getLocation().x + " Y-Location: " + driver.getLocation().y);
		parkedLabel.setText("Driver Parked: " + (driver.isParked() ? "Yes" : "No"));
	}
	
	private class EnterGarageListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			String license = e.getActionCommand();
			Driver driver = garage.getDriver(license);
			Booth booth = garage.getNearestBooth(driver.getLocation(), false);
			
			//show dialog that says driver drove to booth, have option to push ticket button or to leave
			JPanel message = new JPanel();
			message.add(new JLabel("Push Ticket Button?"));
			
			int result = JOptionPane.showConfirmDialog(driverFrame, message, "Entrance Booth", JOptionPane.YES_NO_OPTION);
			if(result == JOptionPane.YES_OPTION)
			{
				booth.ticketButtonPressed(driver);
			}
			
			updateLabels();
		}
	}

	public void showDialog() 
	{
		driverFrame.setVisible(true);
	}
}
