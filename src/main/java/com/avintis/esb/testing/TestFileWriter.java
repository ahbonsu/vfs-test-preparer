package com.avintis.esb.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class TestFileWriter implements Runnable
{
	private Tester tester;
	private boolean running = true;
	
	public TestFileWriter(Tester tester)
	{
		this.tester = tester;
	}

	public void run()
	{
		while(running)
		{
			FileOutputStream fos = null;
			
			try
			{
				String message = TestFileUtil.createMessage(tester.getFileSize());
				String fileName = TestFileUtil.createHashName(message);
				
				File newFile = new File(tester.getIn(), fileName);
				
				newFile.createNewFile();
				fos = new FileOutputStream(newFile);
				fos.write(message.getBytes());
				fos.flush();
				
				tester.incrementFilesIn();
				
			} catch (NoSuchAlgorithmException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			finally
			{
				if(fos != null)
				{
					try
					{
						fos.close();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try
					{
						Thread.sleep(tester.getFileFrequency());
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	
	public void stop()
	{
		running = false;
	}
}
