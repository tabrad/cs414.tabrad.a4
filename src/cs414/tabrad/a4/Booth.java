package cs414.tabrad.a4;

public class Booth 
{
	private int boothId;
	private Location location = new Location();
	private Boolean isExit;
	private Gate gate = new Gate();
	private Rate rates = new Rate();
	private TicketTracker ticketTracker = new TicketTracker();
	private PaymentProcessor paymentProcessor = new PaymentProcessor();
	
	public Booth(TicketTracker ticketTracker, int boothId, Location location, Boolean isExit, Rate rates)
	{
		this.ticketTracker = ticketTracker;
		this.boothId = boothId;
		this.location = location;
		this.isExit = isExit;
		this.rates = rates;
	}
	
	public int getId()
	{
		return boothId;
	}
	
	public Boolean isExit()
	{
		return isExit;
	}

	public Ticket getTicket() 
	{
		Ticket ticket = new Ticket((int)System.currentTimeMillis(), boothId);
		ticketTracker.addTicket(ticket);
		
		gate.open();
		return ticket;
	}
	
	public Location getLocation()
	{
		return location;
	}

	public float getAmountDue(Ticket ticket) 
	{
		if(!ticketTracker.hasUnpaidTicket(ticket))
			return rates.maxCharge;
		
		return (System.currentTimeMillis() - ticket.getTimeEntered()) / 1000 / 60 * rates.hourlyRate;
	}

	public boolean payTicket(Ticket ticket, float payment) 
	{
		if(payment != getAmountDue(ticket))
			return false;
			
		if(!paymentProcessor.processPayment())
			return false;
		
		ticketTracker.markTicketPaid(ticket);
		gate.open();
		
		return true;
	}

	public void closeGate() 
	{
		gate.close();
	}
}
