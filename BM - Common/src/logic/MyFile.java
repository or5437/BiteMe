package logic;

import java.io.Serializable;

@SuppressWarnings("serial")
/**
 * MyFile: The class represent the details of Files.
 * 
 * @author Raz Avraham.
 * @version 1.0 Build 100 December 29, 2021
 */
public class MyFile implements Serializable {
	
	private String Description=null;
	private String fileName=null;	
	private int size=0;
	public  byte[] mybytearray;
	
	
	public void initArray(int size)
	{
		mybytearray = new byte [size];	
	}
	
	public MyFile( String fileName) {
		this.fileName = fileName;
	}
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public byte[] getMybytearray() {
		return mybytearray;
	}
	
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	public void setMybytearray(byte[] mybytearray) {
		
		for(int i=0;i<mybytearray.length;i++)
		this.mybytearray[i] = mybytearray[i];
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}	
}

