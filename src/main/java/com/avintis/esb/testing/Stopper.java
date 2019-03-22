package com.avintis.esb.testing;

import java.io.File;

public class Stopper implements Runnable
{
	
	private File stopFile;
	
	private Tester tester;
	
	private boolean writerRunning = true;
	
	public Stopper(File stopFile, Tester tester)
	{
		this.stopFile = stopFile;
		this.tester = tester;
	}

	public void run()
	{
		while(!Thread.currentThread().isInterrupted())
		{
			if(stopFile.length() != 0 && writerRunning)
			{
				tester.stopWriter();
				writerRunning = false;
				System.out.println("Stopping writer...");
			}
			else
			{
				if(!writerRunning)
				{
					tester.startWriter();
					writerRunning = true;
					System.out.println("Starting writer....");
				}
			}
			try
			{
				Thread.sleep(5000);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
