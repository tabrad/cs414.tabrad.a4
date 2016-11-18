package server;

public class Admin 
{
	private String adminId;
	private String adminPassword;
	
	public Admin(String adminId, String adminPassword)
	{
		this.adminId = adminId;
		this.adminPassword = adminPassword;
	}
	
	public boolean accessBooth(BoothImpl booth)
	{
		return booth.login(this);
	}
	
	@Override public boolean equals(Object o)
	{
		return ((Admin)o).adminId == adminId && ((Admin)o).adminPassword == adminPassword;
	}
}
