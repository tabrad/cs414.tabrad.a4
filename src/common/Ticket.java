package common;

import java.util.Date;

public interface Ticket extends java.rmi.Remote
{
	public void markPaid(int boothExited, float paymentAmount) throws java.rmi.RemoteException;
	public Boolean isPaid() throws java.rmi.RemoteException;
	public float getPaymentAmount() throws java.rmi.RemoteException;
	public Date getTimeEntered() throws java.rmi.RemoteException;
	public Date getTimeExited() throws java.rmi.RemoteException;
	public int getBoothEntered() throws java.rmi.RemoteException;
	public int getBoothExited() throws java.rmi.RemoteException;
	public boolean processedBy(int boothId, boolean asEntrance, boolean asExit) throws java.rmi.RemoteException;
	public String getId() throws java.rmi.RemoteException;
}
