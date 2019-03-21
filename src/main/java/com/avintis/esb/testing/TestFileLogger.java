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
				tester.writeProps();
				Thread.sleep(2000);
				
			} 
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}

	}

}
