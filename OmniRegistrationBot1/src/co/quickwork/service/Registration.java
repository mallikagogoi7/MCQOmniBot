package co.quickwork.service;

import java.util.List;

import co.quickwork.apiCall.ApiCall;
import co.quickwork.database.Database;

public class Registration {
	Database database = new Database();
	Validation validation= new Validation();
	private String botMessage;
	public String registrationProcess(String sender, String text, String channel, String message)
	{
		//String text = database.getMessage(message,"text");	
	    String channelID = database.getMessage(sender,"channelid");	
		 boolean var=database.checkUserExist(sender);
			if(var==false)
			{
				database.insertRegData(sender,channel,message);
			}
			else
			{
				 String mob=database.getMOBbasedOnSender(channelID);
				 if(database.checkUserExistInUsers(mob)==false && database.userRegistered(mob)==true )
				 {
				 
				 try {
				     new ApiCall().insertIntoUserAndPD(sender);
				     database.deleteEnties(mob);
					 database.insertInPersonalDetails(mob);	
				  } catch (Exception e) {
					e.printStackTrace();
				  }
				 
				 }
				database.updateRegCounter(sender);
			}
			int count=database.getRegCount(sender);	
			String data=null;
			
			switch(count)
			{
			case 0: 
				    StringBuffer data0 = new StringBuffer();
				    data0.append("Welcome to QuickWork Registration Bot");
				    List<String> career= database.getCareerList(); 			   
				    String mobile=database.getMOBbasedOnSender(channelID);
				    if(database.userValidation(channelID)==true && database.getRegFlag(sender)==0)
				    {
				    	database.addMobile(sender, mobile);
				    	if(database.checkUserExistInUsers(mobile)==true)
					     {	
				    		data0.append("\t Your profile with us is ----");
				    		data0.append("\n1. Name:  ");
				    		data0.append(database.getUserNameUsingMobile(mobile));
				    		data0.append("\n2. Location:  ");
				    		data0.append(database.getLocationUsingLID(database.getLIDUsingMobile(mobile)));
				    		data0.append("\n3. Degree:  ");
				    		data0.append(database.getUserDegreeUsingDID(database.getUserDIDUsingMobile(mobile)));
				    		data0.append("\n4. Year of passing:  ");
				    		data0.append(database.getUserYOPUsingMobile(mobile));
				    		data0.append("\n5. Gender:  ");
				    		data0.append(database.getUserGenderUsingMobile(mobile));
				    		data0.append("Below a list of career interest are mentioned. Type the index of your career interest ");   
								         for(int i=0;i<career.size();i++)
								         {
								        	 data0.append(i+1+". "+career.get(i));
								         }
								         botMessage=data0.toString();         
								         database.insertInPersonalDetails(mobile);	
								         database.updateRegFlag(sender, 7);
								         database.updateRegCounter(sender, 5);    			
									     break;	 
					     }
					     else
					     {
					    	 database.insertInPersonalDetails(mobile);	
					    	 database.updateRegCounter(sender, 1);
					    	 database.updateRegFlag(sender, 1);
					     }
				    }
				    else
				    {
				    	botMessage="Invalid user";
				    }	    
					
			case 1:
				    StringBuffer data1= new StringBuffer();
				    if(validation.validateName(text)==true && database.getRegFlag(sender)==2)
					 {
					 if(database.getRegEditFlag(sender)==0)
					 {
					 database.insertNameInPD(database.getMobileUsingSender(sender), text);	
					 }
					 else
					 {
						database.insertName(database.getMobileUsingSender(sender), text);	 
					 }
					 if(database.getRegEditFlag(sender)==0)
					 {
						
						 data1.append("Ok, I have made the change. ");
						 data1.append("Type 'E' to continue editing your profile, or type 0 to exit");
						 botMessage=data1.toString();
						 database.updateRegCounter(sender, 11);
						 break;   
					 }
					 
					 database.updateRegCounter(sender, 2);
					 }
			       
				     else if(database.getRegFlag(sender)==1)
				     {
				         botMessage="Can you please tell me your full name?";
				//       database.decreaseCounter(sender);	   
				         database.updateRegFlag(sender, 2);		         			
					     break;	    			
				     }
				     else 
				     {
		    	         botMessage="Please enter your full name";
		   // 	         database.decreaseCounter(sender);	 
		    	         break;
			         }
				       		
			case 2:
				 StringBuffer data2= new StringBuffer();
				 List<String> city= database.getLocationList(); 
				 if(channel.equalsIgnoreCase("fb"))
				 {
					 if(database.getRegFlag(sender)==2)
					 {
						 botMessage="{\"type\":\"catalogue\",\"replies\":[\"Location\"],\"items\":[{\"title\": "
									+ city.get(0)
									+ ", \"imgurl\": \"http://i63.tinypic.com/2eydzpz.jpg\"},{\"title\": "
									+ city.get(1)
									+ ", \"imgurl\": \"http://i66.tinypic.com/izyjut.jpg\"},{\"title\": "
									+ city.get(2)
									+ ", \"imgurl\": \"http://i66.tinypic.com/352ksht.jpg\"}]}";
						    
							 database.updateRegFlag(sender, 3);
					//         database.decreaseCounter(sender);	
							 break;
					 }
					 else if(database.getRegFlag(sender)==3 )
					 {
						 
					 }
					 else
					 {
						 botMessage="Please select any of the location";
					 }
				 }
				 else
				 {
				 if(database.getRegFlag(sender)==2)
				 {
					 data2.append("Type the index of your current location from the options below:  ");
					 for(int i=0;i<city.size();i++)
			         {
						 data2.append("\n");
			        	 data2.append(i+1+". "+city.get(i));
			        	 
			         }
					 botMessage=data2.toString();
			         database.updateRegFlag(sender, 3);
			  //       database.decreaseCounter(sender);	
					 break;
				 }
				 else if(validation.validateIndexing(text,database.getLocationCount())==true && database.getRegFlag(sender)==3)
					 {
					 String selectedCity=city.get(Integer.parseInt(text)-1);
					 String lid=database.getLIDUsingLocation(selectedCity);
					 if(database.getRegEditFlag(sender)==0)
					 {
					 database.insertLocationInPD(database.getMobileUsingSender(sender), lid);	
					 }
					 else
					 {
						 database.insertLocation(database.getMobileUsingSender(sender),lid);	 
					 }
					 
					 if(database.getRegEditFlag(sender)==0)
					 {
						 botMessage="Thanks, I have updated your current location. \n Type 'E' to continue editing your profile, or type 0 to exit";
						 database.updateRegCounter(sender, 11);
						 break;
					 }
					 else
					 {
					 database.updateRegCounter(sender, 3);
					 }
					 }
				  else
				     {
				    	 botMessage="Please enter a valid number. Example 1,2,....";			   	        
			 //  	         database.decreaseCounter(sender);	 
			   	         break; 
				     } 
				 
				 }
		case 3: 
				    List<String> degree=database.getDegreeList();
					if(validation.validateIndexing(text,database.getDegreeCount())==true && database.getRegFlag(sender)==4)
					 {
					 String selectedDegree=degree.get(Integer.parseInt(text)-1);
					 String did=database.getDIDUsingDegree(selectedDegree);	 
					 if(database.getRegEditFlag(sender)==0)
					 {
					 database.insertDegreeInPD(database.getMobileUsingSender(sender), did);	
					 }
					 else
					 {
						 database.insertDegree(database.getMobileUsingSender(sender),did);
					 }
					 if(database.getRegEditFlag(sender)==0)
					 {
						 botMessage="I have updated your qualification.\n Type 'E' to continue editing your profile, or type 0 to exit";						 
						 database.updateRegCounter(sender, 11);
						 break;
					 }
					 else
					 {
					 database.updateRegCounter(sender, 4);
					 }
					 }
				     else if(database.getRegFlag(sender)==3)
				     {
				    	 StringBuffer data3= new StringBuffer();
				    	 data3.append("Type the index of your qualification below: ");
				         for(int i=0;i<degree.size();i++)
				         {
				             data3.append("\n");
				        	 data3.append(i+1+". "+degree.get(i));				        	
				         }
				         botMessage= data3.toString();
				         database.updateRegFlag(sender, 4);
				//         database.decreaseCounter(sender);	    			
					     break;	    			
				     }
				     else
				     {
				    	 botMessage="Please enter a valid number. Example 1,2,....";		  	        
			  	//         database.decreaseCounter(sender);	 
			  	         break; 
				     }
				
			case 4: if(validation.validateIndexing(text,4)==true && database.getRegFlag(sender)==5)
					 {
					if(database.getRegEditFlag(sender)==0)
					 {
					 database.insertYearOFPassingInPD(database.getMobileUsingSender(sender), text);	
					 }
					 else
					 {
						 database.insertYearOFPassing(database.getMobileUsingSender(sender),text);
					 }
					 if(database.getRegEditFlag(sender)==0)
					 {
						 botMessage="I have updated your year of passing.\n Type 'E' to continue editing your profile, or type 0 to exit";
						 database.updateRegCounter(sender, 11);
						 break;
					 }
					 else
					 {
					 database.updateRegCounter(sender, 5);
					 }
					 }
				     else if(database.getRegFlag(sender)==4)
				     {
				    	 botMessage="Type the index of your year of passing below: \n 1. 2013 \n 2. 2014 \n 3. 2015 \n 4. 2016";
				         database.updateRegFlag(sender, 5);
			//	         database.decreaseCounter(sender);	    			
					     break;	    			
				     }
				     else
				     {
				    	 botMessage="Please enter a valid number. Example 1,2,....";			         
				//         database.decreaseCounter(sender);	 
				         break; 
				     }
				  
			case 5: if(validation.validateIndexing(text,3)==true && database.getRegFlag(sender)==6)
					 {
				      String mob=database.getMobileUsingSender(sender);
				      if(database.getRegEditFlag(sender)==0)
						 {
				    	  database.insertGenderInPD(mob,text);
						 }
						 else
						 {
							 database.insertGender(mob,text);
							 if(database.checkUserExistInUsers(mob)==false)
							 {
							 
							 try {
							     new ApiCall().insertIntoUserAndPD(sender);
							     database.deleteEnties(mob);
								 database.insertInPersonalDetails(mob);	
							  } catch (Exception e) {
								e.printStackTrace();
							  }
							 
							 }
						 }
					
					 if(database.getRegEditFlag(sender)==0)
					 {
						 botMessage="I have made the changes.\n Type 'E' to continue editing your profile, or type 0 to exit";						
						 database.updateRegCounter(sender, 11);
						 break;
					 }
					 else
					 {
					 database.updateRegCounter(sender, 6);
					 }
					 }
				     else if(database.getRegFlag(sender)==5)
				     {
				    	 botMessage="Type the index of your gender below: \n 1. Male \n 2. Female \n 3. Other";
				         database.updateRegFlag(sender, 6);
				  //       database.decreaseCounter(sender);	    			
					     break;	    			
				     }
				     else
				     {
				    	 botMessage="Please enter a valid number. Example 1,2,....";			 	       
			 	  //       database.decreaseCounter(sender);	 
			 	         break; 
				     }
				
			case 6:    	List<String> career1= database.getCareerList(); 
						if(validation.validateIndexing(text,database.getCareerCount())==true && database.getRegFlag(sender)==7)
						 {
						 database.insertCareerInterest(database.getMobileUsingSender(sender),Integer.parseInt(text));
						 if(database.getRegEditFlag(sender)==0)
						 {
							 botMessage="Your career interest is been changed. \n Type 'E' to continue editing your profile, or type 0 to exit.";
							 database.updateRegCounter(sender, 11);
							 break;
						 }
						 else
						 {
						 database.updateRegCounter(sender, 7);
						 }
						 }
					     else if(database.getRegFlag(sender)==6)
					     {
					    	 StringBuffer data6= new StringBuffer();
					    	 data6.append("Type the index of your career interest below: ");
					       
					         for(int i=0;i<career1.size();i++)
					         {
					             data6.append("\n");
					        	 data6.append(i+1+". "+career1.get(i));
					       }
					         botMessage= data6.toString();
					         database.updateRegFlag(sender, 7);
					 //        database.decreaseCounter(sender);	    			
						     break;	    			
					     }
					     else
					     {
					    	 botMessage="Please enter a valid number. Example 1,2,....";				  	   
				  	  //       database.decreaseCounter(sender);	 
				  	         break; 
					     }
			case 7:  if(validation.isValidEmail(text)==true && database.getRegFlag(sender)==8)
					     {
					    	 database.insertEmailID(database.getMobileUsingSender(sender),text);
					    	 if(database.getRegEditFlag(sender)==0)
							 {
					    		 botMessage="I have updated your email address. \n Type 'E' to continue editing your profile, or type 0 to exit.";
								 database.updateRegCounter(sender, 11);
								 break;
							 }
					    	 else
					    	 {
					    	 database.updateRegCounter(sender, 8);
					    	 }
					     }
					     else if(database.getRegFlag(sender)==7)
					     {
					    	 botMessage="Thank you. Can you please provide your email address?";				   	        
					   	     database.updateRegFlag(sender, 8);
					//         database.decreaseCounter(sender);	    
				   	         break;
					     }
					     else
					     {
					    	  botMessage="Invalid email address. Please enter a valid address.";					    	 
					//    	  database.decreaseCounter(sender);
					    	  break;
					     }
			case 8:  if(database.getRegFlag(sender)==8)
				       {
				    	  botMessage="We also need an alternate phone number so that we can reach you.(eg: your mother's / father's number etc)";
				    	  database.updateRegFlag(sender, 9);
				 //   	  database.decreaseCounter(sender);	    
				          break;
				       }
				   else if(validation.getAltNumberValidation(text, sender)==2 && database.getRegFlag(sender)==9)
					   {
					     database.insertAlternateNo(database.getMobileUsingSender(sender), text);
					     if(database.getRegEditFlag(sender)==0)
						 {
					    	 botMessage="I have updated your alternate number. \n Type 'E' to continue editing your profile, or type 0 to exit.";
							 database.updateRegCounter(sender, 11);
							 break;
						 }
					     else
					     {
					     database.updateRegCounter(sender, 9);
					     }
					   }
				      else if(validation.getAltNumberValidation(text, sender)==1 && database.getRegFlag(sender)==9)
				      {
				    	  botMessage="This is your registered mobile number. Please enter an alternate number (eg: your mother's / father's number)";
				//    	  database.decreaseCounter(sender);	    
			  	          break;
				      }	
				      
				       else
				       {
				    	  botMessage="Invalid phone number. Please enter a valid phone number.";
			  	 //   	 database.decreaseCounter(sender);
			  	    	  break;
				       }
			case 9:  if(validation.isValidAvaliabilitity(text)==true && database.getRegFlag(sender)==10)
						{
						if(text.equalsIgnoreCase("Y")||text.equalsIgnoreCase("yes"))
						  {
						  database.insertAvailibility(database.getMobileUsingSender(sender), "Y");
						  if(database.getRegEditFlag(sender)==0)
							 {
							     botMessage="Your availability is updated.\n Type 'E' to continue editing your profile, or type 0 to exit.";
								 database.updateRegCounter(sender, 11);
								 break;
							 }
						  else
						  {
						  database.updateRegCounter(sender, 10);
						  }
						  }
					else 
						  {
						     StringBuffer data9= new StringBuffer();
					     	 data9.append("Thank you. You can also reach out to us by calling at 7045607365/66 or sending email to hr@quickwork.co \n ");
					     	 database.insertAvailibility(database.getMobileUsingSender(sender), "N");						
							 if(database.getRegEditFlag(sender)==0)
							 {
								data9.append("Your availability is updated.\n Type 'E' to continue editing your profile, or type 0 to exit.");
						        botMessage=data9.toString();		
								 database.updateRegCounter(sender, 11);
								 break;
							 }
							 else
							 {
						     data9.append("To view your profile type'P' \n To edit your profile type'E'");
						     botMessage= data9.toString();						 
							 database.updateRegCounter(sender, 11);
							 break;
							 }						
						  }
					   }
						
						else if(database.getRegFlag(sender)==9)
						{	   
							botMessage="Now, I need to know your availability. Are you available to do a 6-months full-time internship through us?"+"\n"
									+"Please enter Y or yes if available, else N or no.";
							database.updateRegFlag(sender, 10);
				//	    	database.decreaseCounter(sender);	   
							break;
						}
						else
						{
						 botMessage="Invalid input, Please type Y or N.";		  	    	
			  	    	 database.decreaseCounter(sender);
						 break;
						}
			
			case 10: 	if(validation.isValidDate(text)==true && database.getRegFlag(sender)==11)
				        {
		 				 database.insertAvailibilityDate(database.getMobileUsingSender(sender), text);
		 				 if(database.getRegEditFlag(sender)==0)
						 {
		 					 botMessage="I have updated your availability date. \n Type 'E' to continue editing your profile, or type 0 to exit.";		 					
							 database.updateRegCounter(sender, 11);
							 break;
						 }
		 				 else
		 				 {
		 				 database.updateRegCounter(sender, 11);
		 				 }
				        }
			 			else if(database.getRegFlag(sender)==10)
			 			{
			 				 botMessage="From what date are you available full-time. Enter in dd/mm/yyyy format. Example: 01/08/2016";			 	             
			 	             database.updateRegFlag(sender, 11);
				//	         database.decreaseCounter(sender);	   
			 	 	         break; 
			 			}
					    else
				 	    {
				 	        botMessage="Please enter a date in dd/mm/yyyy format. Please enter a date starting from tomorrow.";
				 //	    	database.decreaseCounter(sender);
				 	    	break; 
				 	    }
			case 11:    botMessage="Thank you. Quickwork Team will reach out to you soon. You can also reach out to us by calling at 7045607365/66 or sending email to hr@quickwork.co.\n To view your profile type 'P' \n To edit your profile type 'E'  \n \n"+"********** : click here to check the other bots.";			     
				        break;
				        		        
			case 12: String  mobileNumber= database.getMobileUsingSender(sender);
			         StringBuffer data12= new StringBuffer();
				     if(text.equalsIgnoreCase("P")||text.equalsIgnoreCase("E")||text.equalsIgnoreCase("0"))		    	
			             {		
				    	 data12.append("Your profile is with us.");				    	 
				    	 if(text.equalsIgnoreCase("E"))
				    	 {
				    		 database.enterEdited(sender, 0);
				    		 data12.append("0. My profile is correct. No change required.");
				    	 }
				    	 if(text.equalsIgnoreCase("0"))
				    	 {
				    		 data12.append("Thank you. If you want to edit your profile later, you can do so by typing the index of required field you want to change.\n \n"+"********* : click here to check the other bots");				    		
				    		 botMessage=data12.toString();
				    		 break;
				    	 }
//				    	    data12.append("1. Registered number:  ");
//				    	    data12.append(mobileNumber);
//				    	    data12.append("\n2. Name:  ");
//				    	    data12.append(database.getUserNameUsingMobile(mobileNumber));
//				    	    data12.append("\n3. Location: ");
//				    	    data12.append(database.getLocationUsingLID(database.getLIDUsingMobile(mobileNumber)));
//				    	    data12.append("\n4. Degree: ");
//				    	    data12.append(database.getUserDegreeUsingDID(database.getUserDIDUsingMobile(mobileNumber)));
//				    	    data12.append("\n5. Year of passing: ");
//				    	    data12.append(database.getUserYOPUsingMobile(mobileNumber));
//				    	    data12.append("\n6. Gender: ");
//				    	    data12.append(database.getUserGenderUsingMobile(mobileNumber));
//				    	    data12.append("\n7. Career Interest: ");
//				    	    data12.append(database.getCareerInterestUsingMobile(mobileNumber));
//				    	    data12.append("\n8. EmailId:  ");
//				    	    data12.append(database.getEmailUsingMobile(mobileNumber));
//				    	    data12.append("\n9. Alternate number:  ");
//				    	    data12.append(database.getAlternateNoUsingMobile(mobileNumber));
//				    	    data12.append("\n10. Availibility:  ");
//				    	    data12.append(database.getAvailabilityUsingMobile(mobileNumber));
				           
				           if((database.getAvailabilityDateUsingMobile(mobileNumber))!=null && database.getAvailabilityUsingMobile(mobileNumber).equalsIgnoreCase("Y"))
				           {
				        	   data12.append("\n11. Availibility Date: "+" "+database.getAvailabilityDateUsingMobile(mobileNumber));
				           }
				           
				           if(text.equalsIgnoreCase("E"))
				           {
				        	   data12.append("If your profile is correct, please reply with 0, if you wish to change or update, please reply with 1 or 2 or 3 or 4 or 5......");
				           }
				           else
				           {
				        	   data12.append("Type 'E' to edit your profile");
				        	   database.decreaseCounter(sender);
				           }
				           botMessage=data12.toString();
			             }
						else
						{
							botMessage="Invalid keyword. Type 'P' to view your profile or type 'E' to edit your profile";
						//	database.decreaseCounter(sender);
						}			
			            break;
			            
			    case 13:  StringBuffer data13= new StringBuffer();
			    	  if(validation.validateProfileNumber(text)&&text.equals("0"))  
			             {
			    	       botMessage="Thank you. If you want to edit your profile later, you can do so by typing the the index of required field you want to change. ";
			    	       database.decreaseCounter(sender);
			    	       break;
			             }
			    		if(validation.validateProfileNumber(text)&&text.equals("1"))
			    		{
			    			 botMessage="You cannot change your registered number. \n Type 'E' to continue editing your profile.";
							 database.updateRegCounter(sender, 11);
							 break;
			    			
			    		}
			    		if(validation.validateProfileNumber(text)&&text.equals("2"))
			    		{
			    			botMessage="Enter your name";
			    			database.updateRegCounter(sender, 0);
			    			database.updateRegFlag(sender, 2);
			    			break;
			    		}
			    	    if(validation.validateProfileNumber(text)&&text.equals("3"))
			    		{
			    	    	List<String> city1= database.getLocationList(); 
			    			 data13.append("For editing of Location, type the index of your current location below: ");				       
					         for(int i=0;i<city1.size();i++)
					         {
					        	 data13.append(i+1+". "+city1.get(i));
					         }
					         botMessage=data13.toString();
					         database.updateRegFlag(sender, 3);
					         database.updateRegCounter(sender, 1);    			
						     break;	    	
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("4"))
			    		{
			    			 data13.append("For editing of Degree, type the index of your degree below: ");
			    			 List<String> degree1=database.getDegreeList(); 
					         for(int i=0;i<degree1.size();i++)
					         {
					        	 data13.append(i+1+". "+degree1.get(i));
					         }
					         botMessage= data13.toString();
					         database.updateRegFlag(sender, 4);
					         database.updateRegCounter(sender, 2);       			
						     break;	    
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("5"))
			    		{
			    			 botMessage= "For editing of your year of passing, type the index of your year of passing below: \n 1. 2013 \n 2. 2014 \n 3. 2015 \n 4. 2016";
					         database.updateRegFlag(sender, 5);
					         database.updateRegCounter(sender, 3);       			
						     break;	    		    			
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("6"))
			    		{
			    			 botMessage="For editing of your gender, type the index of your gender below: \n 1. Male \n 2. Female \n 3. Other";
					         database.updateRegFlag(sender, 6);
					         database.updateRegCounter(sender, 4);       		
						     break;	 
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("7"))
			    		{
			    			 data13.append("For editing of your career interest, type the index of your career interest below: ");
			    			 List<String> career2= database.getCareerList(); 
					         for(int i=0;i<career2.size();i++)
					         {
					        	 
					        	 data13.append(i+1+". "+career2.get(i));
					         }
					         botMessage=data13.toString();
					         database.updateRegFlag(sender, 7);
					         database.updateRegCounter(sender, 5);       		    			
						     break;	    	
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("8"))
			    		{
			    			botMessage="Enter your edited email id?";
				   	        database.updateRegFlag(sender, 8);
				   	        database.updateRegCounter(sender, 6);	    
				   	        break; 	    			
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("9"))
			    		{
			    			  botMessage="Enter your edited alternate phone number.";
					    	  database.updateRegFlag(sender, 9);
					    	  database.updateRegCounter(sender, 7);	       
				  	          break;
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("10"))
			    		{
			    			botMessage="Please enter Y or yes if available, else N or no.";
							database.updateRegFlag(sender, 10);
							database.updateRegCounter(sender, 8);	       
							break;
			    		}
			    		else if(validation.validateProfileNumber(text)&&text.equals("11"))
			    		{
			    			 botMessage="From what date are you available full-time. Enter in dd/mm/yyyy format.";
			 	             database.updateRegFlag(sender, 11);
			 	             database.updateRegCounter(sender, 9);	
			 	 	         break; 
			    		}
			    		else
			    		{
			    			botMessage="Invalid entry. Please enter a valid number as 0,1,2,.........";
			    	//		database.decreaseCounter(sender);
			    		}	 
			   } 
			return botMessage;
			
			}	
	}
