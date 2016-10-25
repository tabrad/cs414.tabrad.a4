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

	private Ticket getTicket() 
	{
		if(isExit)
			return null;
		
		Ticket ticket = new Ticket((int)System.currentTimeMillis(), boothId);
		ticketTracker.addTicket(ticket);
		
		return ticket;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void ticketButtonPressed(Driver driver)
	{
		Ticket ticket = getTicket();
		driver.setTicket(ticket);
		openGate();

		//wait for driver to move before closing the gate
		try
		{
			while(driver.getLocation() == location)
				Thread.sleep(250);
		} 
		catch(Exception e)
		{
			return;
		}
		
		gate.close();
	}
	
	private float getAmountDue(Ticket ticket) 
	{
		if(!ticketTracker.hasUnpaidTicket(ticket))
			return rates.maxCharge;
		
		return (System.currentTimeMillis() - ticket.getTimeEntered()) / 1000 / 60 * rates.hourlyRate;
	}
	
	private void requestPayment(Driver driver, Ticket ticket)
	{
		float amountDue = getAmountDue(ticket);
		driver.promptPayment(this, amountDue);
	}
	
	public boolean insertPayment(Driver driver, Ticket ticket, float amount)
	{
		if(!adminMode && amount != getAmountDue(ticket))
			return false;
		
		if(!adminMode && !paymentProcessor.processPayment())
			return false;
		
		ticket.markPaid(boothId, amount);
		ticketTracker.markTicketPaid(ticket);
		gate.open();
		driver.promptExit(ticketTracker.getGarage());
		
		return true;
	}

	public void insertTicket(Driver driver, Ticket ticket) 
	{
		if(!isExit)
			return;
		
		requestPayment(driver, ticket);
	}
	
	public Gate getGate() 
	{
		return gate;
	}
	
	public void openGate()
	{
		gate.open();
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
