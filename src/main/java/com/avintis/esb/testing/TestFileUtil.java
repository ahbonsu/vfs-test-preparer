package com.avintis.esb.testing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

public class TestFileUtil
{
	private final Random random = new Random();
	
	public String createMessage(int length)
	{
		char[] array = new char[length];
		for(int i = 0; i < length; i++)
		{
		 array[i] = (char)(random.nextInt(26) + 'a');
		}
		 
		return new String(array);
	}
	
	public String createHashName(String message) throws NoSuchAlgorithmException
	{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		String name = Base64.getEncoder().encodeToString(md.digest(message.getBytes()));
		
		
		name = name.contains("/") ? name.replaceAll("/", ".") : name;
		while(name.startsWith("."))
		{
			name = name.substring(1);
		}
		
		return name + ".txt";
	}
	
}
