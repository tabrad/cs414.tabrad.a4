package cs414.tabrad.a4;

import java.util.Date;

public class Ticket 
{
	private Date timeEntered;
	private Date timeExited;
	private Boolean isPaid;
	private int boothEntered;
	private int boothExited;
	private float paymentAmount;
	
	public Ticket(Date timeEntered, int boothEntered)
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
		return ((Ticket)o).getTimeEntered() == timeEntered && ((Ticket)o).getBoothEntered() == boothEntered;
	}

	public boolean processedBy(int boothId, boolean asEntrance, boolean asExit) 
	{
		if(asEntrance)
			return boothEntered == boothId;
		
		if(asExit)
			return boothExited == boothId;
		
		return (boothEntered == boothId || boothExited == boothId);
	}
}
