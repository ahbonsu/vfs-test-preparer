package com.avintis.esb.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;;

public class Tester
{
	private String in;
	private String out;
	private int fileSize;
	private int duration;
	private int maxWaitOnFile;
	private int frequency;
	private int filesIn = 0;
	private int filesOut = 0;
	private int filesCorrupted = 0;

	private Properties props;
	
	private long startTime;

	public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException
	{

		/**
		 * if(!(args.length == 2 && (new File(args[0]).exists()))) {
		 * System.out.println("Either no property file submitted or given file does not
		 * exist!"); System.exit(1); } props.load(new FileReader(new File(args[0])));
		 */

		
		File f = new File("/home/hauensteina/app.properties");
		Properties props = new Properties();
		props.load(new FileReader(f));

		Tester tester = new Tester(props);
		tester.test();
		

	}

	public Tester(Properties props) throws FileNotFoundException, IOException
	{
		this.props = props;
		readProperties();
		startTime = System.currentTimeMillis();

	}

	private void readProperties()
	{
		in = props.getProperty("IN");
		out = props.getProperty("OUT");
		fileSize = Integer.valueOf(props.getProperty("FileSize"));
		duration = Integer.valueOf(props.getProperty("Duration"));
		maxWaitOnFile = Integer.valueOf(props.getProperty("MaxWaitOnFile"));
		frequency = Integer.valueOf(props.getProperty("Frequency"));
		
		if(isNullOrEmpty(in) || isNullOrEmpty(out))
		{
			System.out.println("Check properties file: No IN or OUT defined!");
			System.exit(1);
		}
		
		if(fileSize == 0 || duration == 0 || maxWaitOnFile == 0)
		{
			System.out.println("Either fileSize, duration or maxWaitOnFile is 0. Abort!");
			System.exit(1);
		}
	}
	
	private boolean isNullOrEmpty(String s)
	{
		if(s == null)
		{
			return true;
		}
		if(s.equals(""))
		{
			return true;
		}
		return false;
	}

	private void test() throws FileNotFoundException, IOException
	{
		
		while(startTime + (duration * 1000) > System.currentTimeMillis())
		{	
			TestFileDialog dialog = new TestFileDialog(this);
			
			Thread t = new Thread(dialog);
			t.start();
			
			try
			{
				Thread.sleep(frequency);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
		}
		
		try
		{
			Thread.sleep(maxWaitOnFile * 1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("FileSize\tFrequency\tDuration\tMaxWaitOnFile\tNumber of files IN\tNumber of files OUT\tcorrupted");
		System.out.println(fileSize + "\t\t" + frequency + "\t\t" + duration + "\t\t" + maxWaitOnFile + "\t\t" + filesIn + "\t\t\t" + filesOut + "\t\t\t" + filesCorrupted);
	}
		
		public int getFileSize()
		{
			return fileSize;
		}
		
		public String getIn()
		{
			return in;
		}
		
		public String getOut()
		{
			return out;
		}
		
		public int getMaxWaitOnFile()
		{
			return maxWaitOnFile;
		}
		
		public void incrementFileIn()
		{
			filesIn++;
		}
		
		public void processResult(Result res, String messageFileName)
		{
			switch(res)
			{
				case CORRUPTED:
					filesCorrupted++;
					break;
				case NOT_RECEIVED:
					System.out.println("MISSING File: " + messageFileName);
					break;
				case OK:
					filesOut++;
					break;
			}
		}
}
