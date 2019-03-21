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
	private int filesInAmount;
	private int assembleTime;

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
		
		File inFolder = new File(props.getProperty("01IN"));
		for(File child : inFolder.listFiles())
		{
			child.delete();
		}
		
		File outFolder = new File(props.getProperty("02OUT"));
		for(File child : outFolder.listFiles())
		{
			child.delete();
		}
	}

	private void readProperties()
	{
		in = props.getProperty("01IN");
		out = props.getProperty("02OUT");
		fileSize = Integer.valueOf(props.getProperty("03FileSize"));
		fileFrequency = Integer.valueOf(props.getProperty("04FileFrequency"));
		filesInAmount = Integer.valueOf(props.getProperty("10FilesINAmount"));
		assembleTime = Integer.valueOf(props.getProperty("11AssembleTime"));
	}

	public void writeProps() throws FileNotFoundException, IOException
	{
		long currentTime = System.currentTimeMillis();
		
		props.setProperty("05FilesIN", String.valueOf(filesIn));
		props.setProperty("06FilesOUT", String.valueOf(filesOut));
		props.setProperty("07TotalTime", String.valueOf((currentTime - startTime) / 1000));
		props.setProperty("08FilesDelta", String.valueOf(filesIn - filesOut));
		props.setProperty("09FilesCorrupted", String.valueOf(filesCorrupted));

		props.store(new FileOutputStream(propsFile), "State: " + state);
	}

	private void test() throws FileNotFoundException, IOException
	{

		TestFileWriter writer = new TestFileWriter(this);
		Thread t = new Thread(writer);
		t.start();

		TestFileReader reader = new TestFileReader(this);
		t = new Thread(reader);
		t.start();

		TestFileLogger logger = new TestFileLogger(this);
		t = new Thread(logger);
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
	
	public int getAssembleTime()
	{
		return assembleTime;
	}

}
