package cs414.tabrad.a4;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
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
			
			Date earliestDate = ticketTracker.getEarliestDate();
			Date latestDate = ticketTracker.getLatestDate();
			
			Set<Ticket> tickets = ticketTracker.getAllTickets();
			String command = e.getActionCommand();
			Calendar calendar = Calendar.getInstance();
	    	if(command.equals("financial"))
	        {
    			if(granularityBox.getSelectedIndex() == 0) //day
    			{
    				float days[] = {0f,0f,0f,0f,0f,0f,0f};
    				for(Ticket ticket : tickets)
    	        	{
    					calendar.setTime(ticket.getTimeEntered());
    					int day = calendar.get(Calendar.DAY_OF_WEEK);
    					
    					
    					days[day - 1] = ticket.getPaymentAmount() + days[day - 1];
    					System.out.println(days[day - 1]);
    	        	}
    				
    				Object[][] data = {{0f,0f,0f,0f,0f,0f,0f}};
    				for(int i = 0; i < days.length; i++)
    					data[0][i] = days[i];
    				
    				String[] columnNames = new String[] {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        			table = new JTable(data, columnNames);
        			
        			JFrame tableFrame = new JFrame("Report");
        			tableFrame.setSize(400, 400);
        			tableFrame.add(new JScrollPane(table));
        			tableFrame.setVisible(true);
    			}
    			
	        }
	        else if(command.equals("occupancy"))
	        {
	        	 
	        }
		}
	}
	
	public void showDialog()
	{
		trackerFrame.setVisible(true);
	}
}
