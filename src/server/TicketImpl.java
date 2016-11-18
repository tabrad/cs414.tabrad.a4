package server;

import java.util.Date;

import common.Ticket;

public class TicketImpl implements Ticket
{
	private Date timeEntered;
	private Date timeExited;
	private Boolean isPaid;
	private int boothEntered;
	private int boothExited;
	private float paymentAmount;
	
	public TicketImpl (Date timeEntered, int boothEntered)
	{
		this.timeEntered = timeEntered;
		this.boothEntered = boothEntered;
		isPaid = false;
	}
	
	public void markPaid(int boothExited, float paymentAmount)
	{
		this.boothExited = boothExited;
		this.paymentAmount = paymentAmount;
		isPaid = true;
	}
	
	public Boolean isPaid()
	{
		return isPaid;
	}
	
	public float getPaymentAmount()
	{
		return paymentAmount;
	}
	
	public Date getTimeEntered()
	{
		return timeEntered;
	}
	
	public Date getTimeExited()
	{
		return timeExited;
	}
	
	public int getBoothEntered()
	{
		return boothEntered;
	}
	
	public int getBoothExited()
	{
		return boothExited;
	}
	
	@Override public boolean equals(Object o)
	{
		return ((TicketImpl)o).getTimeEntered() == timeEntered && ((TicketImpl)o).getBoothEntered() == boothEntered;
	}

	public boolean processedBy(int boothId, boolean asEntrance, boolean asExit) 
	{
		if(asEntrance)
			return boothEntered == boothId;
		
		if(asExit)
			return boothExited == boothId;
		
		return (boothEntered == boothId || boothExited == boothId);
	}

	public String getId() 
	{
		return "T" + timeEntered + "B" + boothEntered;
	}
}
