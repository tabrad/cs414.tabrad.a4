package cs414.tabrad.a4;

public class Booth 
{
	private int boothId;
	private Location location = new Location();
	private Boolean isExit;
	private Gate gate = new Gate();
	private Rate rates = new Rate();
	private TicketTracker ticketTracker;
	private PaymentProcessor paymentProcessor = new PaymentProcessor();
	private Boolean adminMode = false;
	
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
		if(!isExit)
			return false;
		
		if(!adminMode  && payment != getAmountDue(ticket))
			return false;
			
		if(!adminMode && !paymentProcessor.processPayment())
			return false;
		
		ticket.markPaid(boothId, payment);
		ticketTracker.markTicketPaid(ticket);
		gate.open();
		
		return true;
	}

	public void closeGate() 
	{
		gate.close();
	}

	public boolean login(Admin admin) 
	{
		if(!ticketTracker.getGarage().isAdmin(admin))
			return false;
		
		adminMode = true;
		return true;
	}
	
	public void logout() 
	{
		adminMode = false;
	}
	
	public void requestAdmin(Driver driver, Ticket ticket) 
	{
		Admin admin = ticketTracker.getGarage().getAdmin();
		if(!admin.accessBooth(this))
			return;
		
		admin.settlePayment(this, driver, ticket);
	}
}
