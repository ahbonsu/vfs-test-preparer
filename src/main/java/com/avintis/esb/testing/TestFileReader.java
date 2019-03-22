package com.avintis.esb.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.security.NoSuchAlgorithmException;

public class TestFileReader implements Runnable
{
	private Tester tester;
	private boolean running = true;

	private File outFolder;

	public TestFileReader(Tester tester)
	{
		this.tester = tester;

		outFolder = new File(tester.getOut());
	}

	public void run()
	{
		while (running)
		{
			File[] txtFiles = outFolder.listFiles(new FilenameFilter()
			{
				
				public boolean accept(File dir, String name)
				{
					return name.endsWith(".txt");	
				}
			});
			
					
					
			for (File child : txtFiles)
			{
				String name = child.getName();
				long fileSize = child.length();
				
				if(fileSize != (long) tester.getFileSize())
				{
					try
					{
						System.out.println("Different file size, maybe still writing....");
						Thread.sleep(2000);
						if(fileSize != (long) tester.getFileSize())
						{
							System.out.println("DIFFERENT FILE SIZE! Received: " + fileSize + ", expected: " + tester.getFileSize());
							tester.incrementFilesCorrupted();
							child.delete();
						}
						
					} catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else
				{
					FileInputStream fis = null;
					try
					{
						fis = new FileInputStream(child);
						byte[] bMessage = new byte[tester.getFileSize()];
						fis.read(bMessage);
						String message = new String(bMessage);
						String hashName = TestFileUtil.createHashName(message);
						
						if(name.equals(hashName))
						{
							tester.incrementFilesOut();
							tester.removeFileRef(hashName);
						}
						else
						{
							System.out.println("CORRUPTED!!!");
							System.out.println("Filename: " + name);
							System.out.println("calculated hashName: " + hashName);
							tester.incrementFilesCorrupted();
						}
						
						child.delete();
						
					} catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally
					{
						child.delete();
						if(fis != null)
						{
							try
							{
								fis.close();
							} catch (IOException ignore)
							{
								
							}
						}
					}
				}
				
				

			}

			try
			{
				Thread.sleep(50);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
