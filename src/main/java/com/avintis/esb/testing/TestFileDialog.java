package com.avintis.esb.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class TestFileDialog implements Runnable
{
	private Tester tester;
	private String fileName;
	private File in;
	private File out;
	
	private TestFileUtil testFileUtil = new TestFileUtil();
	
	public TestFileDialog(Tester tester)
	{
		this.tester = tester;
	}

	public void run()
	{
		in = new File(tester.getIn());
		out = new File(tester.getOut());
		
		if(!(in.exists() || out.exists()))
		{
			System.out.println("Either IN-Folder: " + in.getAbsolutePath() + " or OUT-Folder: " + out.getAbsolutePath() + " does not exist! Abort ...");
			return;
		}
		
		fileName = write();
		Result res = read();
		tester.processResult(res, fileName);
		
	}
	
	private String write()
	{
		FileOutputStream fos = null;
		String fileName = null;

		try
		{
			String message = testFileUtil.createMessage(tester.getFileSize());
			fileName = testFileUtil.createHashName(message);

			File newFile = new File(in, fileName);

			newFile.createNewFile();
			fos = new FileOutputStream(newFile);
			fos.write(message.getBytes());
			fos.flush();
			
			tester.incrementFileIn();

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{

			if (fos != null)
			{
				try
				{
					fos.close();
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return fileName;
	}
	
	private Result read()
	{
		File outFolder = new File(tester.getOut());
		File returnedFile = null;
		
		
		try
		{
			Thread.sleep(tester.getMaxWaitOnFile() * 1000);
		} catch (InterruptedException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		returnedFile = new File(outFolder, fileName);
		if(!returnedFile.exists()) 
		{
			returnedFile = null;
		}
		
		if(returnedFile == null)
		{
			//System.out.println(Thread.currentThread().getName());
			return Result.NOT_RECEIVED;
		}
		else
		{
			FileInputStream fis = null;
			try
			{
				fis = new FileInputStream(returnedFile);
				byte[] bMessage = new byte[tester.getFileSize()];
				fis.read(bMessage);
				String message = new String(bMessage);
				String hashName = testFileUtil.createHashName(message);
				if(hashName.equals(fileName))
				{
					return Result.OK;
				}
				else
				{
					System.out.println("HASHNAME: " + hashName);
					System.out.println("FILENAME: " + fileName);
					return Result.CORRUPTED;
				}
				
			} catch (Exception e)
			{
				System.out.println("EXCEPION WHILE READ");
				e.printStackTrace();
				return Result.CORRUPTED;
			}
			finally
			{
				try
				{
					fis.close();
					returnedFile.delete();
				} catch (IOException ignore)
				{
				}
			}
		}
		
	}

}
