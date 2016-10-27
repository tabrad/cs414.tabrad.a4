package cs414.tabrad.a4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.JUnit4TestAdapter;


@RunWith(Suite.class)

@Suite.SuiteClasses({
	BoothTest.class,
	DriverTest.class,
	GarageTest.class,
	TicketTest.class,
	TicketTrackerTest.class
})

public class TestAll 
{
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(suite());
	}
	
	public static junit.framework.Test suite()
	{
		return new JUnit4TestAdapter(TestAll.class);
	}
	
}
