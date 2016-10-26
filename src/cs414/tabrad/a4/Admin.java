package cs414.tabrad.a4;

public class Admin 
{
	private String adminId;
	private String adminPassword;
	
	public Admin(String adminId, String adminPassword)
	{
		this.adminId = adminId;
		this.adminPassword = adminPassword;
	}
	
	public Boolean accessBooth(Booth booth)
	{
		return booth.login(this);
	}

	public void settlePayment(Booth booth, Driver driver, Ticket ticket) 
	{
		booth.insertTicket(driver, ticket);
	}
	
	@Override public boolean equals(Object o)
	{
		return ((Admin)o).adminId == adminId && ((Admin)o).adminPassword == adminPassword;
	}
}