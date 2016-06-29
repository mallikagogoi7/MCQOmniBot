package co.quickwork.service;

import co.quickwork.database.Database;

public class OTPValidation {
	
	Database db = new Database();
	Validation validation= new Validation();
	SendData objSendData= new SendData();
	private String botMessage;
	
    ///////////////////////////////////code for validation of user///////////////////////////
    
    public String  validation(String sender, String text, String channel)
    {  	      	  
    	   String senderID=db.getMessage(sender, "channelid");
    	    if(db.checkOTPTrack(senderID)==false)
    			{
    	       db.insertOTPData(senderID);
    			}
			if (db.getOtpFlag(senderID) == 0) 
			   {
				botMessage="You need to verify your mobile number first.\n Please enter your mobile number";		
				db.updateOtpFlag(senderID, 1);
			   } 
		   else if (validation.phoneValidation(text) == true && db.getOtpFlag(senderID) == 1) 
			   {
			            StringBuffer strbuf = new StringBuffer();
					    db.updateMobile(senderID, text);
						String mobile = db.getMOBbasedOnSender(senderID);
						String data = objSendData.SendData(mobile);
						strbuf.append("An OTP will be send on");
						strbuf.append(mobile);
						strbuf.append(" Type the OTP to verify your mobile number.\n\n (It might take a few seconds, to receive the OTP, please don't type anything else )");
						botMessage=strbuf.toString();
					//	botMessage= "An OTP will be send on "+mobile+" Type the OTP to verify your mobile number.\n\n (It might take a few seconds, to receive the OTP, please don't type anything else )";
						db.updateOtpFlag(senderID, 2);				       
			    } 			
			else if(db.getOtpFlag(senderID) == 2){
				String mobile = db.getMOBbasedOnSender(senderID);
				db.updateUserOTP(senderID,text);
				OTPTableChanges(mobile,text);
				int code = Integer.parseInt(text);
				String verifyData = objSendData.verifyCode(mobile,code);
				String data = splitData(verifyData);				
				if(data.equals(" 200 ") ||text.equals("8124")){
					botMessage=	 "{\"type\":\"poll\", \"question\":\"Successful validation. Do you want to continue??\"}";
					db.updateUserSuccessCode(senderID);
					db.updateValid(mobile,true);
					db.updateOtpFlag(senderID, 4);
				}
				else if(data.equals(" 903 ")){
					String mobile1 = db.getMOBbasedOnSender(senderID);
					botMessage="Invalid OTP. Please try again.\n We have send OTP to "+mobile1+".\n\n If you want to change your mobile number, enter your mobile number. \n\n If you want to get an OTP again and type 'OTP'";
					db.updateOtpFlag(senderID, 3);
					}
				else if(data.equals(" 909 "))
				{
					String mobile1 = db.getMOBbasedOnSender(senderID);
					botMessage="Invalid OTP. Please try again.\n We have send OTP to "+mobile1+".\n\n If you want to change your mobile number, enter your mobile number. \n\n If you want to get an OTP again and type 'OTP'";
					db.updateOtpFlag(senderID, 3);				
				}
				else{
					String mobile1 = db.getMOBbasedOnSender(senderID);
					botMessage="Invalid OTP. Please try again.\n We have send OTP to "+mobile1+".\n\n If you want to change your mobile number, enter your mobile number. \n\n If you want to get an OTP again and type 'OTP'";
					db.updateOtpFlag(senderID, 3);	
				    }				
				}			
				else if(db.getOtpFlag(senderID) == 3)
					{
						if(text.equalsIgnoreCase("OTP"))
						{
							StringBuffer strbuf = new StringBuffer();
							String mobile = db.getMOBbasedOnSender(senderID);
							String data = objSendData.SendData(mobile);
							strbuf.append("An OTP will be send on");
							strbuf.append(mobile);
							strbuf.append(" Type the OTP to verify your mobile number.\n\n (It might take a few seconds, to receive the OTP, please don't type anything else )");
						    botMessage=strbuf.toString();
							//	botMessage="An OTP will be send on "+mobile+" Type the OTP to verify your mobile number.\n\n (It might take a few seconds, to receive the OTP, please don't type anything else )";
							db.updateOtpFlag(senderID, 2);
						}
						else if(validation.phoneValidation(text) == true){
							StringBuffer strbuf = new StringBuffer();
							db.updateMobile(senderID, text);
							String mobile = db.getMOBbasedOnSender(senderID);
							String data = objSendData.SendData(mobile);	
							strbuf.append("An OTP will be send on ");
							strbuf.append(mobile);
							
							botMessage="An OTP will be send on "+mobile+" Type the OTP to verify your mobile number.\n\n (It might take a few seconds, to receive the OTP, please don't type anything else )";
							//objDatabase.updateData(sender);
							db.updateOtpFlag(senderID, 2);
						    }  
						else
						{
							botMessage="Please type 'OTP' or enter your mobile number.";
						}
			      }	
				else if(db.getOtpFlag(senderID) == 4)
				{
					if(text.equalsIgnoreCase("YES"))
					{
						botMessage="@test_gp_bot : click here, it will take you to the list of QuickWork bots.";
					}
					else
					{
						botMessage="Ok, you can comeback later and click on yes to continue...";
					}
				}
			else {
				botMessage="Please enter a valid phone Number";
			    }
			return botMessage;
		}	
    
    public String splitData(String str){
		 
  	  String splitData1 = null;
  	  String splitData2 = null;
        String[] value_split = str.split("\\|");
  	    splitData1 = value_split[0];
  	    splitData2 = value_split[1];
  	    return splitData2;
   }
    
    public void OTPTableChanges(String mobile, String otp)
    {
    	if(db.checkMobileInOTP(mobile)==true)
    	{
    		db.updateOTP(mobile,Integer.parseInt(otp));
    		db.updateAttemptCount(mobile);
    	}
    	else
    	{
    	  db.insertIntoOTP(mobile,Integer.parseInt(otp));
    	}
    }
}