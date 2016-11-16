package common;

import model.Location;

public interface Booth extends java.rmi.Remote
{
	public int getId() throws java.rmi.RemoteException;
	public Boolean isExit() throws java.rmi.RemoteException;
	public Location getLocation() throws java.rmi.RemoteException;
	public boolean gateIsOpen() throws java.rmi.RemoteException;
	public void closeGate() throws java.rmi.RemoteException;
}
