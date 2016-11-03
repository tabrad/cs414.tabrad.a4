package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import controller.GarageController;

public class ReportDialog extends Dialog
{
	private JButton financialButton;
	private JButton occupancyButton;
	private JComboBox<String> granularityBox;
	private JTable table;
	
	private GarageController garageController;
	
	public ReportDialog()
	{
		garageController = GarageController.getInstance();
		prepareGUI();
	}
	
	private void prepareGUI()
	{
		frame = new JFrame("Reports");
		frame.setSize(400, 250);
		frame.setLayout(new GridLayout(5, 2));
		
		frame.add(new JLabel("Report Granularity"));
		String[] granularityTypes = {"day", "week", "month"};
		granularityBox = new JComboBox<String>(granularityTypes);
		frame.add(granularityBox);
		
		financialButton = new JButton("Run Financial Report");
		financialButton.setActionCommand("financial");
		financialButton.addActionListener(new ButtonClickListener());
		frame.add(financialButton);
		
		occupancyButton = new JButton("Run Occupancy Report");
		occupancyButton.setActionCommand("occupancy");
		occupancyButton.addActionListener(new ButtonClickListener());
		frame.add(occupancyButton);
	}
	
	private class ButtonClickListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			int granularity = granularityBox.getSelectedIndex();
			String[] columnNames;
			
			if(granularity == 0)
				columnNames = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
			else if(granularity == 1)
				columnNames = new String[] {"Week 1", "Week 2", "Week 3", "Week 4", "Week 5", "Week 6"};
			else
				columnNames = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
			
			boolean isFinancialReport = e.getActionCommand().equals("financial");
			Object[][] data = garageController.getTableData(granularity, isFinancialReport);
    		table = new JTable(data, columnNames);
	    	JFrame tableFrame = new JFrame("Report");
			tableFrame.setSize(400, 400);
			tableFrame.add(new JScrollPane(table));
			tableFrame.setVisible(true);
		}
	}
}
