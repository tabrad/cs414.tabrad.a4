package cs414.tabrad.a4;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


public class ReportDialog 
{
	private TicketTracker ticketTracker;
	private JFrame trackerFrame;
	private JButton financialButton;
	private JButton occupancyButton;
	private JComboBox<String> granularityBox;
	private JTable table;
	
	public ReportDialog(TicketTracker ticketTracker)
	{
		this.ticketTracker = ticketTracker;
		prepareGUI();
	}
	
	private void prepareGUI()
	{
		trackerFrame = new JFrame("Reports");
		trackerFrame.setSize(400, 250);
		trackerFrame.setLayout(new GridLayout(5, 2));
		
		trackerFrame.add(new JLabel("Report Granularity"));
		String[] granularityTypes = {"day", "week", "month"};
		granularityBox = new JComboBox<String>(granularityTypes);
		trackerFrame.add(granularityBox);
		
		financialButton = new JButton("Run Financial Report");
		financialButton.setActionCommand("financial");
		financialButton.addActionListener(new ButtonClickListener());
		trackerFrame.add(financialButton);
		
		occupancyButton = new JButton("Run Occupancy Report");
		occupancyButton.setActionCommand("occupancy");
		occupancyButton.addActionListener(new ButtonClickListener());
		trackerFrame.add(occupancyButton);
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
			Object[][] data = ticketTracker.getTableData(granularity, isFinancialReport);
    		table = new JTable(data, columnNames);
	    	JFrame tableFrame = new JFrame("Report");
			tableFrame.setSize(400, 400);
			tableFrame.add(new JScrollPane(table));
			tableFrame.setVisible(true);
		}
	}
	
	public void showDialog()
	{
		trackerFrame.setVisible(true);
	}
}
