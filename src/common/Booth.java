package common;

public interface Booth extends java.rmi.Remote
{
	public int getId() throws java.rmi.RemoteException;
	public Boolean isExit() throws java.rmi.RemoteException;
	public Location getLocation() throws java.rmi.RemoteException;
	public boolean gateIsOpen() throws java.rmi.RemoteException;
	public void closeGate() throws java.rmi.RemoteException;
	public Ticket ticketButtonPressed(boolean isSimulation) throws java.rmi.RemoteException;
	public float getAmountDue(String string) throws java.rmi.RemoteException;
	public float getAmountDue() throws java.rmi.RemoteException;
	public Ticket findTicket(String id) throws java.rmi.RemoteException;
	public boolean insertPayment(String ticketId, float amountDue, boolean isCreditCard) throws java.rmi.RemoteException;;
}
