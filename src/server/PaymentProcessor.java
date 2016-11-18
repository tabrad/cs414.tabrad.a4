package server;

public class PaymentProcessor 
{
	public boolean processPayment(boolean isCreditCard) 
	{
		if(!isCreditCard)
			return true;
		
		int rand = (int)(Math.random() * 101);
		//70% chance credit card works
		return rand <= 70;
	}
	
}
