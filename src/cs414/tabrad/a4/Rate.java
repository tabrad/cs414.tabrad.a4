package cs414.tabrad.a4;

public class Rate 
{
	public float hourlyRate;
	public float minCharge;
	public float maxCharge;
	
	public Rate()
	{
		hourlyRate = 0;
		minCharge = 0;
		maxCharge = 0;
	}
	
	public Rate(float hourlyRate, float minCharge, float maxCharge)
	{
		this.hourlyRate = hourlyRate;
		this.minCharge = minCharge;
		this.maxCharge = maxCharge;
	}
}
