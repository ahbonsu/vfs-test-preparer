package com.avintis.esb.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class TestFileWriter
{
	private Tester tester;
	private boolean running = true;

	public TestFileWriter(Tester tester)
	{
		this.tester = tester;
	}

	public String write()
	{

		FileOutputStream fos = null;
		String fileName = null;

		try
		{
			String message = TestFileUtil.createMessage(tester.getFileSize());
			fileName = TestFileUtil.createHashName(message);

			File newFile = new File(tester.getIn(), fileName);

			newFile.createNewFile();
			fos = new FileOutputStream(newFile);
			fos.write(message.getBytes());
			fos.flush();

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

	public void stop()
	{
		running = false;
	}
}
