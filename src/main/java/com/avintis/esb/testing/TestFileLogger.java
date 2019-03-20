package com.avintis.esb.testing;

public class TestFileLogger implements Runnable
{
	private Tester tester;

	public TestFileLogger(Tester tester)
	{
		this.tester = tester;
	}

	public void run()
	{

		while (true)
		{
			try
			{
				tester.stopWriter();
				Thread.sleep(10000);
				tester.writeProps();
				tester.startWriter();
				
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

	}

}
