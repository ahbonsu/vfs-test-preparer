package com.avintis.esb.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Tester
{
	
	private int fileSize;
	private int fileFrequency;
	private int filesIn = 0;
	private int filesOut = 0;
	private int filesDelta = 0;
	private int filesCorrupted = 0;

	private SortedProperties props;
	private File propsFile;
	
	private String state = "running";
	
	private long startTime;
	
	private String out;
	
	private TestFileWriter writer;

	public static void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException
	{

		/**
		 * if(!(args.length == 1 && (new File(args[0]).exists()))) {
		 * System.out.println("Either no propertie file submitted or given file does not
		 * exist!"); System.exit(1); } props.load(new FileReader(new File(args[0])));
		 */
		
		
		File f = new File("/home/hauensteina/app.properties");

		Tester tester = new Tester(f);
		tester.test();

	}

	public Tester(File propsFile) throws FileNotFoundException, IOException
	{
		startTime = System.currentTimeMillis();
		this.propsFile = propsFile;
		props = new SortedProperties();
		props.load(new FileInputStream(propsFile));
		readProperties();
		
		//delete content of in and out folder
		
		File inFolder = new File(props.getProperty("1IN"));
		for(File child : inFolder.listFiles())
		{
			child.delete();
		}
		
		File outFolder = new File(props.getProperty("2OUT"));
		for(File child : outFolder.listFiles())
		{
			child.delete();
		}
	}

	private void readProperties()
	{
		in = props.getProperty("1IN");
		out = props.getProperty("2OUT");
		fileSize = Integer.valueOf(props.getProperty("3FileSize"));
		fileFrequency = Integer.valueOf(props.getProperty("4FileFrequency"));
	}

	public void writeProps() throws FileNotFoundException, IOException
	{
		long currentTime = System.currentTimeMillis();
		
		props.setProperty("5FilesIN", String.valueOf(filesIn));
		props.setProperty("6FilesOUT", String.valueOf(filesOut));
		props.setProperty("7TotalTime", String.valueOf((currentTime - startTime) / 1000));
		props.setProperty("8FilesDelta", String.valueOf(filesIn - filesOut));
		props.setProperty("9FilesCorrupted", String.valueOf(filesCorrupted));

		props.store(new FileOutputStream(propsFile), "State: " + state);
	}

	private void test() throws FileNotFoundException, IOException
	{

		startWriter();

		TestFileReader reader = new TestFileReader(this);
		Thread t = new Thread(reader);
		t.start();

		TestFileLogger logger = new TestFileLogger(this);
		t = new Thread(logger);
		t.start();

	}
	
	public void stopWriter()
	{
		writer.stop();
	}
	
	public void startWriter()
	{
		writer = new TestFileWriter(this);
		Thread t = new Thread(writer);
		t.start();
	}

	public int getFilesIn()
	{
		return filesIn;
	}

	public void setFilesIn(int filesIn)
	{
		this.filesIn = filesIn;
	}

	public void incrementFilesIn()
	{
		filesIn++;
	}

	public int getFilesOut()
	{
		return filesOut;
	}

	public void setFilesOut(int filesOut)
	{
		this.filesOut = filesOut;
	}

	public void incrementFilesOut()
	{
		filesOut++;
	}

	public int getFileFrequency()
	{
		return fileFrequency;
	}

	public void setFileFrequency(int fileFrequency)
	{
		this.fileFrequency = fileFrequency;
	}

	public int getFilesDelta()
	{
		return filesDelta;
	}

	public void setFilesDelta(int filesDelta)
	{
		this.filesDelta = filesDelta;
	}

	public int getFileSize()
	{
		return fileSize;
	}
	
	private String in;
	public String getIn()
	{
		return in;
	}

	public String getOut()
	{
		return out;
	}
	
	public void incrementFilesCorrupted()
	{
		filesCorrupted++;
	}

}
