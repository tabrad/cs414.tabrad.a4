package cs414.tabrad.a4;

public class Ticket 
{
	private int timeEntered;
	private int timeExited;
	private Boolean isPaid;
	private int boothEntered;
	private int boothExited;
	private float paymentAmount;
	
	public Ticket(int timeEntered, int boothEntered)
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
	
	public int getTimeEntered()
	{
		return timeEntered;
	}
	
	public int getTimeExited()
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
