package co.quickwork.service;

import co.quickwork.database.Database;

public class Validation {

	Database database= new Database();
	
	//this function validates weather in that question, that optionNo(message) exist or not
	public boolean validateMessage(String message,int questionNo)
	{
		boolean valid=false;
		int optionSize=database.getOptionSize(questionNo);
		try{
		for(int i=1;i<=optionSize;i++)
		{
			if(Integer.parseInt(message)==i)
			{
				valid=true;
				break;
			}
			else
			{
				valid=false;
			}
		}
		}
		catch(Exception e)
		{
			valid=false;
		}
	    return valid;
	}
	
	public static void main(String[] args) {
		Validation v= new Validation();
		System.out.println(v.validateMessage("1", 1));
	}
	
}	