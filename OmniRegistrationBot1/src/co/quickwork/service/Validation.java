package co.quickwork.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.quickwork.database.Database;

public class Validation {

public boolean phoneValidation(String moNo){		
		String regex ="^(?:(\\d{0,9})[\\s-]?)?(\\d{6})$";		
		long checkNo = 0;
		boolean isMobileNoValid = false;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(moNo);
		if (m.matches())
		{
		   checkNo = Integer.parseInt(m.group(1));;
		   for(int i = 7000 ; i<=9999 ;++i){				  
				if(checkNo == i){
					isMobileNoValid = true;
					break;
				}				
				else{
			       isMobileNoValid = false;
					}					
		     }			   
	     }
		return isMobileNoValid;
	}

public boolean validateName(String message){
	    String tokens[] = null;
	    String splitPattern = " ";
	    int flag1=0;
	    int flag2=0;	    
	    boolean validate=false;
	    Pattern p = Pattern.compile(splitPattern);
	    tokens = p.split(message);
	    for (int i = 0; i <tokens.length; i++) {	    	
	    	String word=tokens[i];
	    	if(i==0)
	    	{
	    		if(word.length()>=2)
	    		{
	    			flag1=1;
	    		}	
	    	}
	    	else if(i==(tokens.length-1))
	    	{
	    		if(word.length()>=3)
	    		{
	    			flag2=1;
	    		}
	    	}
	    }	    
	   if(flag1==1&&flag2==1)
	   {
		   validate=true;
	   }
	return validate;   
}

public boolean validateIndexing(String index,int max){
	boolean isNumberValid = false;
	try{
	for(int i=1;i<=max;i++)
	{
		if(Integer.parseInt(index)==i)
		{
			isNumberValid=true;
			break;
		}
		else
		{
			isNumberValid=false;
		}
	}	
	}catch(NullPointerException e)
	{
		isNumberValid=false;
	}
	catch(NumberFormatException e)
	{
		isNumberValid=false;
	}
	return isNumberValid;
}

public boolean validateProfileNumber(String index){	
	boolean isNumberValid = false;
	try{
	for(int i=0;i<=11;i++)
	{
		if(Integer.parseInt(index)==i)
		{
			isNumberValid=true;
			break;
		}
		else
		{
			isNumberValid=false;
		}
	}
	}
	catch(NumberFormatException e)
	{
		isNumberValid=false;
	}
	return isNumberValid;
}

public int getAltNumberValidation(String message,String sender){
	 Database db= new Database();
	 String llRegexwith91 ="^(?:(\\d{0,9})[\\s-]?)?(\\d{10})$";
	 String llRegexwith0 ="^(?:(\\d{0,9})[\\s-]?)?(\\d{9})$";
	 String mobRegex ="^(?:(\\d{0,9})[\\s-]?)?(\\d{6})$";
	 String firstDigit = ((message).substring(0, 1));	
	 int numberLength=message.length();
	 long checkNo = 0;
    int flag=0;
    if(message.matches(".*\\d.*")){
	 if(Integer.parseInt(firstDigit)==9&& numberLength==12)
	 {
		 Pattern p = Pattern.compile(llRegexwith91);
			Matcher m = p.matcher(message);
			if (m.matches())
			{	
				 checkNo = Integer.parseInt(m.group(1));;
				 int thirdDigit = Integer.parseInt((message).substring(2,3));	
				 System.out.println("thirdDigit"+thirdDigit);
				 if(checkNo==91&&thirdDigit!=0)
				 {
					 flag=2;    //matched with regex	
				 }
				 else{
					 flag=3;  // invalid number
				     }
			}
			else
			{
				flag=3;  // invalid number
			}
	 }	 
	 else if(((Integer.parseInt(firstDigit)==9)||(Integer.parseInt(firstDigit)==8)||(Integer.parseInt(firstDigit)==7))&& numberLength==10)
	 {
		 Pattern p = Pattern.compile(mobRegex);
			Matcher m = p.matcher(message);
			if (m.matches())
			{	
				if(message.equals(db.getMobileUsingSender(sender)))
				{
				  flag=1;	// same with MOB
				}
				else
				{
					 checkNo = Integer.parseInt(m.group(1));
					  for(int i = 7000 ; i<=9999 ;++i)
					  {						  
						 if(checkNo == i){
				            flag=2;    //matched with regex
				            break;
				            }
						 else
						   {
							 System.out.println("*");
							 flag=3;    // invalid number
						   }
				        }		
			     }
			}
			else
			{
				flag=3;  // invalid number
			}
	 }
	 else if(Integer.parseInt(firstDigit)==0 && numberLength==11)
	 {
		 Pattern p = Pattern.compile(llRegexwith0);
			Matcher m = p.matcher(message);
			if (m.matches())
			{				
			    checkNo = Integer.parseInt(m.group(1));;
			    if(checkNo==00)
			    {
			    	flag=3;  // invalid number
			    }
			    else
			    {
			    	flag=2;    //matched with regex	
			    }
								
			}
			else
			{
				flag=3;  // invalid number
			}
	 }
	 else
	 {
		 flag=3;
	 }
		
    }
    else
    {
   	 flag=3;
    }
	return flag;
}

public boolean isValidEmail(String mail)
{
	String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
    Boolean b = mail.matches(EMAIL_REGEX);
    return b;
}

public boolean isValidAvaliabilitity(String message)
{
	 boolean valid=false;
	 if(message.equalsIgnoreCase("y")||message.equalsIgnoreCase("n")||message.equalsIgnoreCase("yes")||message.equalsIgnoreCase("no"))
	 {
		 valid=true;
	 }
	 else
	 {
		 valid=false;
	 }
	 return valid;
}

public  boolean isValidDate(String date){
	boolean validity=false;
	 Calendar current = Calendar.getInstance();
	 String regex= "^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
	 Pattern pattern = Pattern.compile(regex);
	     Matcher matcher = pattern.matcher(date);
	     if(matcher.matches()==true)
	     {
	    	 String myFormatString = "dd/MM/yyyy";
	 	     SimpleDateFormat df = new SimpleDateFormat(myFormatString);
	 	     Date givenDate;
	 	     try {
				givenDate = df.parse(date);
				 Long l = givenDate.getTime();
				 Date next = new Date(l);
				 if(next.after(current.getTime()) || (next.equals(current.getTime()))){
				        validity=true;
				    } else {
				        validity=false;
				    }
			} catch (ParseException e) {
				e.printStackTrace();
			}
	     }
	     else
	     {
	    	 validity=false;
	     }
	     return validity;
}

}
