package co.quickwork.service;

import co.quickwork.database.Database;

public class QuickWorkBot {

	Database db = new Database();
	
	public  void checkUserExist(String context)
	{ 		
		if(db.checkUserExist(context)==false)
		{
			db.insertData(context);
		} 
		else
		{
			//ignore
		}	  
	}
	
	public static String showData()
	{
		String data=null;
		String intro="Hello, Welcome to QuickWork Technologies Pvt. Ltd."+"\n\n"+"Here you will get the list of bots to get connected with QuickWork"+
		"\n\n"+"If you are new to QuickWork, use:"+"\n\n";

		String data0="@QWRegnBOT :- To register yourself with QuickWork."+"\n\n"+"If you are already connected with QuickWork use: ";
			
	    String data1="@QWRegnBOT :- To complete your registration, view/edit your profile.";

		String data2="@QWAttendBOT :-To CheckIn/Checkout after joining the Internship program.";
        
		String data3=null;
				//"@QWTestBOT :- to take test in various fields based on which you will be offered internship.";

		String data4="@QWRateBOT :- To rate your assigned company and manager.";

		String data5="@QWReviewBOT :- To set multiple goals and update the progress of the same.";
		
		String data6="NOTE: Type help, to check if QuickWork has introduced any new bots, and explore it.";
		
		data=intro+data0+"\n\n"+data1+"\n\n"+data2+"\n\n"+data4+"\n\n"+data5+"\n\n"+data6;
		return data;
	} 
}