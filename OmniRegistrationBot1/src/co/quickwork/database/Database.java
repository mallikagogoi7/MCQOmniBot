package co.quickwork.database;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

    public class Database{

	private Connection Mcon = new Connection();
	private DB dBase = Mcon.conn();
	private DBCollection track = dBase.getCollection("bot_registrationTrack");
	private DBCollection personalDetails = dBase.getCollection("personalDetails");
	private DBCollection additional_pD = dBase.getCollection("additional_personalDetails");
	private DBCollection locations = dBase.getCollection("locations");
	private DBCollection degrees = dBase.getCollection("degrees");
	private DBCollection skills = dBase.getCollection("FieldOfInterest");
	private DBCollection otp_track=dBase.getCollection("bot_OTP_Logs");
	private DBCollection otp=dBase.getCollection("OTP_Logs");
	private DBCollection assesment=dBase.getCollection("assesment");
	private DBCollection users = dBase.getCollection("users");
	
	
	public void checkUserExistInOtherChannels(String senderID)
	{
		String mobile=getMOBbasedOnSender(senderID);
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("mobile", mobile);
		track.findOne(basicDBObject).get("_id").toString();
	}
	
	public boolean checkUserExistInUsers(String mobile)
	{
		boolean valid=false;
		try{
		String id=users.findOne(new BasicDBObject("mobile",mobile)).get("_id").toString();
		valid=true;
		System.out.println("1");
		}
		catch(NullPointerException e)
		{
			valid=false;
		}
		return valid;
	}
	
	//checked
	public String getMessage( String message, String data)
	{
		String name = null;
		try {
			JSONObject jsonObj = new JSONObject(message);
		     name = jsonObj.getString(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return name;
	}	
	
	//checked
	public boolean userValidation(String channelID)
	{
		boolean valid=false;
		try{
			 valid=(boolean) otp_track.findOne(new BasicDBObject("senderID",channelID)).get("valid");
		    
		}catch(NullPointerException e)
		{
			valid= false;
		}	
		return valid;
	}
	
	public void insertIntoOTPTrack()
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("senderID", "194529172");
		basicDBObject.put("mobile", "8286578819");
		basicDBObject.put("valid", true);
		otp_track.insert(basicDBObject);
	}
	
	//checked
	public void insertOTPData(String context)
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("senderID", context);
		basicDBObject.put("otpFlag", 0);
		otp_track.insert(basicDBObject);
	}
	
	//checked
	public boolean checkOTPTrack(String senderID)
	{
		try{
			String id=otp_track.findOne(new BasicDBObject("senderID",senderID)).get("_id").toString();
			return true;
		}catch(NullPointerException e)
		{
			return false;
		}
	}
	
	//checked
	public int getOtpFlag(String senderID)
	{ 		
	    return (int) otp_track.findOne(new BasicDBObject("senderID", senderID)).get("otpFlag");
	}
	//checked
	public void  updateOtpFlag(String senderID,int text)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("senderID", senderID);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("otpFlag", text);
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp_track.update(objDb1, updateDBobj);
	}
	
	//checked
	public void updateMobile(String senderID, String mob)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("senderID", senderID);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("mobile", mob);
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp_track.update(objDb1, updateDBobj);
	}
	
	//checked
	public String getMOBbasedOnSender(String senderID)
	{
		String mob= otp_track.findOne(new BasicDBObject("senderID",senderID)).get("mobile").toString();
		return mob;
	}
	
	//checked
	public void insertIntoOTP(String mobile, int otpCode)
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("mobile", mobile);
		basicDBObject.put("OTP", new Long(otpCode));
		basicDBObject.put("attemptCount", new Long(0));
		otp.insert(basicDBObject);
	}
	
	//checked
	public boolean checkMobileInOTP(String mobile)
	{
		try{
		   otp.findOne(new BasicDBObject("mobile",mobile)).get("_id").toString();
		   return true;		
		}catch(NullPointerException e)
		{
	     return false;
		}
	}
	
	//checked
	public void updateAttemptCount(String mobile)
	{
		long attempt=getAttemptCount(mobile)+1;
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("attemptCount", new Long(attempt));
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp.update(objDb1, updateDBobj);				
	}
	
	//checked
	public void updateOTP(String mobile,int otpCount)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("OTP", new Long(otpCount));
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp.update(objDb1, updateDBobj);	
	}
	
	//checked
	public void updateValid(String mobile,boolean valid)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("VALID", valid);
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp.update(objDb1, updateDBobj);
	}
	
	//checked
	public long getAttemptCount(String mobile)
	{
		return (long) otp.findOne(new BasicDBObject("mobile", mobile)).get("attemptCount");
	}
	
	//checked
	public void insertAssesment(String mobile)
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("mobile", mobile);
		basicDBObject.put("quickscore", new Long(4));
		assesment.insert(basicDBObject);
	}
	
	//checked
	public void updateAssesment(String mobile, Number score)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("quickscore", score);
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		assesment.update(objDb1, updateDBobj);		
	}
	
	//checked
	public void  updateUserFailureCode(String context)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("senderID", context);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("valid", false);
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp_track.update(objDb1, updateDBobj);
	}
	
	//checked
	public void  updateUserOTP(String context,String text)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("senderID", context);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("OTP", text);
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp_track.update(objDb1, updateDBobj);
	}
	
	//checked
	public void  updateUserSuccessCode(String context)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("senderID", context);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("valid", true);
		
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		otp_track.update(objDb1, updateDBobj);
	}
	
	//checked
	public  String getUserNameUsingMobile(String mobile)
	{
		String userName = personalDetails.findOne(new BasicDBObject("mobile", mobile)).get("name").toString();
		return userName;
	}
	
	//checked
	public  String getUserNameUsingMobileFromAdditionPD(String mobile)
	{
		String userName = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("name").toString();
		return userName;
	}
	
	//checked
	public String getSkillUsingSkillID(int skillId)
	{
		String userName = skills.findOne(new BasicDBObject("FieldOfInterestID", skillId)).get("FieldOfInterestName").toString();
		return userName;
	}
	
	//checked
	public String getLocationUsingLID(String LId)
	{
		String location = locations.findOne(new BasicDBObject("_id", new ObjectId(LId))).get("City").toString();
		return location;
	}
	
	//checked
	public String getLIDUsingMobile(String mobile)
	{
		String userLID = personalDetails.findOne(new BasicDBObject("mobile", mobile)).get("lid").toString();
		return userLID;
	}
	
	//checked
	public String getLIDUsingMobileFromAdditionPD(String mobile)
	{
		String userLID = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("lid").toString();
		return userLID;
	}
	
	//checked
	public String getLIDUsingLocation(String location)
	{
		String LID = locations.findOne(new BasicDBObject("City", location)).get("_id").toString();
		return LID;
	}
	
	//checked
	public String getUserDegreeUsingDID(String did)
	{
		String userDegree = degrees.findOne(new BasicDBObject("_id",new ObjectId(did))).get("degreeName").toString();
		return userDegree;
	}
	
	//checked
	public String getDIDUsingDegree(String degree)
	{
		String userDegree = degrees.findOne(new BasicDBObject("degreeName",degree)).get("_id").toString();
		return userDegree;
	}
	
	//checked
	public String getUserYOPUsingMobile(String mobile)
	{
		String userYOP = personalDetails.findOne(new BasicDBObject("mobile", mobile)).get("personYOP").toString();
		return userYOP;
	}
	
	//checked
	public String getUserYOPUsingMobileFromAdditionPD(String mobile)
	{
		String userYOP = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("personYOP").toString();
		return userYOP;
	}
	
	//checked
	public String getUserDIDUsingMobile(String mobile)
	{
		String userDID = personalDetails.findOne(new BasicDBObject("mobile", mobile)).get("did").toString();
		return userDID;
	}
	
	//checked
	public String getUserDIDUsingMobileFromAdditionPD(String mobile)
	{
		String userDID = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("did").toString();
		return userDID;
	}
	
	//checked
	public String getUserGenderUsingMobile(String mobile)
	{
		String gender = personalDetails.findOne(new BasicDBObject("mobile", mobile)).get("gender").toString();
		return gender;
	}
	
	//checked
	public String getUserGenderUsingMobileFromAdditionPD(String mobile)
	{
		String gender = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("gender").toString();
		return gender;
	}
	
	//checked
	public String getCareerInterestUsingMobile(String mobile)
	{
		String careerID = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("career Interest").toString();
		String career=getSkillUsingSkillID(Integer.parseInt(careerID));
		return career;
	}
	
	//checked
	public String getEmailUsingMobile(String mobile)
	{
		String email = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("email").toString();
		return email;
	}
	
	//checked
	public String getAlternateNoUsingMobile(String mobile)
	{
		String alt = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("alternateNumber").toString();
		return alt;
	}
	
	//checked
	public String getAvailabilityUsingMobile(String mobile)
	{
		String ava = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("availibilty").toString();
		return ava;
	}
	
	//checked
	public String getAvailabilityDateUsingMobile(String mobile)
	{
		String date=null;
		try{
		 date = additional_pD.findOne(new BasicDBObject("mobile", mobile)).get("availibiltyDate").toString();
		}
		catch(NullPointerException e)
		{
			date=null;
		}
		return date;
	}
		
	//checked
	public boolean checkUserExist(String sender)
	{
	 try{
		 BasicDBObject basicDBObject= new BasicDBObject();
		 basicDBObject.put("sender", sender);
		 int value=(int) track.findOne(basicDBObject).get("regCounter");
		 return true;
		 
		}catch(NullPointerException e){
			return false;
		}		
	}
	
	//checked
	public void insertRegData(String sender, String channel, String msg)
	{		
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("sender", sender);
		basicDBObject.put("regFlag", 0);
		basicDBObject.put("regCounter", 0);
		basicDBObject.put(channel, getMessage(sender, "channelid"));
		basicDBObject.put("regEditFlag", 1);
		track.insert(basicDBObject);
	}
	
	//checked
	public int getRegCount(String sender)
	{
		return (int) track.findOne(new BasicDBObject("sender", sender)).get("regCounter");
	}
	
	//checked
	public int getRegFlag(String sender)
	{
		return (int) track.findOne(new BasicDBObject("sender", sender)).get("regFlag");
	}
	
	//checked
	public void  updateRegCounter(String sender)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("sender", sender);		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("regCounter", getRegCount(sender)+1);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		track.update(objDb1, updateDBobj);
	}
	
	//checked
	public void  updateMessage(String sender, String message)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("sender", sender);		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("message",message);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		track.update(objDb1, updateDBobj);
	}

	//checked
	public void  enterEdited(String context,int flag)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("sender", context);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("regEditFlag",flag);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		track.update(objDb1, updateDBobj);
	}
	
	//checked
	public int getRegEditFlag(String sender)
	{
		int editFlag = (int) track.findOne(new BasicDBObject("sender", sender)).get("regEditFlag");
		return editFlag;
	}
	
	//checked
	public void  updateRegFlag(String sender,int regFlag)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("sender", sender);		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("regFlag", regFlag);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		track.update(objDb1, updateDBobj);
	}
	
	//checked
	public void  addMobile(String sender,String mobile)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("sender", sender);		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("mobile",mobile);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		track.update(objDb1, updateDBobj);
	}
	
	public String getMobileUsingSender(String sender)
	{
		String mobile = track.findOne(new BasicDBObject("sender", sender)).get("mobile").toString();
		return mobile;
	}
	
	public boolean userRegistered(String mobile)
	{
		boolean exist=false;
		try{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("mobile", mobile);
		personalDetails.findOne(basicDBObject).get("gender");
		exist=true;
		}
		catch(NullPointerException e)
		{
			exist=false;
		}
		return exist;
	}
	
	public void  decreaseCounter(String sender)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("sender", sender);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("regCounter", getRegCount(sender)-1);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		track.update(objDb1, updateDBobj);
	}
	
	public void updateRegCounter(String sender,int no)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("sender", sender);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("regCounter", no);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		track.update(objDb1, updateDBobj);		
	}
	
	public void insertInPersonalDetails(String mob)
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("mobile", mob);
		additional_pD.insert(basicDBObject);
	}
	
	public void insertName(String mobile,String name)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("name", name);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);		
	}
	
	public void insertNameInPD(String mobile,String name)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("name", name);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		personalDetails.update(objDb1, updateDBobj);		
	}
	
	public void insertLocation(String mobile,String lid)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("lid", lid);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);		
	}
	
	public void insertLocationInPD(String mobile,String lid)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("lid", lid);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		personalDetails.update(objDb1, updateDBobj);		
	}
	
	public void insertCareerInterest(String mobile,int id)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("career Interest", id);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);		
	}
	
	public void insertEmailID(String mobile,String email)
	{
		BasicDBObject objDb1= new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("email", email);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);
	}
	
	public void insertAlternateNo(String mobile,String no)
	{
		BasicDBObject objDb1= new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("alternateNumber", no);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);
	}
	
	public void insertAvailibility(String mobile,String data)
	{
		BasicDBObject objDb1= new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("availibilty", data);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);
	}
	
	public void insertAvailibilityDate(String mobile,String data)
	{
		BasicDBObject objDb1= new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("availibiltyDate", data);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);
	}
	
	public void insertDegree(String mobile,String did)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("did", did);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);		
	}
	
	public void insertDegreeInPD(String mobile,String did)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("did", did);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		personalDetails.update(objDb1, updateDBobj);		
	}
	
	
	public void insertGender(String mobile,String no)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		BasicDBObject objDb2 = new BasicDBObject();
		if(no.equals("1"))
		{			
			objDb2.put("gender", "male");
		}
		else if(no.equals("2"))
		{
			objDb2.put("gender", "female");
		}
		else if(no.equals("3"))
		{
			objDb2.put("gender", "other");
		}
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);	
	}
	public void insertGenderInPD(String mobile,String no)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		BasicDBObject objDb2 = new BasicDBObject();
		if(no.equals("1"))
		{			
			objDb2.put("gender", "male");
		}
		else if(no.equals("2"))
		{
			objDb2.put("gender", "female");
		}
		else if(no.equals("3"))
		{
			objDb2.put("gender", "other");
		}
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		personalDetails.update(objDb1, updateDBobj);	
	}
	
	public void insertYearOFPassing(String mobile,String no)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		BasicDBObject objDb2 = new BasicDBObject();
		if(no.equals("1"))
		{			
			objDb2.put("personYOP", "2013");
		}
		else if(no.equals("2"))
		{
			objDb2.put("personYOP", "2014");
		}
		else if(no.equals("3"))
		{
			objDb2.put("personYOP", "2015");
		}
		else if(no.equals("4"))
		{
			objDb2.put("personYOP", "2016");
		}
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		additional_pD.update(objDb1, updateDBobj);	
	}
	
	public void insertYearOFPassingInPD(String mobile,String no)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("mobile", mobile);
		BasicDBObject objDb2 = new BasicDBObject();
		if(no.equals("1"))
		{			
			objDb2.put("personYOP", "2013");
		}
		else if(no.equals("2"))
		{
			objDb2.put("personYOP", "2014");
		}
		else if(no.equals("3"))
		{
			objDb2.put("personYOP", "2015");
		}
		else if(no.equals("4"))
		{
			objDb2.put("personYOP", "2016");
		}
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		personalDetails.update(objDb1, updateDBobj);	
	}
	
	public List<String> getLocationList()
	{
		List<String> location_list = new ArrayList<String>();
		String city=null;
		DBCursor cursor=locations.find();
		while(cursor.hasNext())
		{
			DBObject dbObject=cursor.next();
			city=dbObject.get("City").toString();
			location_list.add(city);
		}		
		return location_list;
	}
	
	public List<String> getCareerList()
	{
		List<String> location_list = new ArrayList<String>();
		String city=null;
		DBCursor cursor=skills.find();
		while(cursor.hasNext())
		{
			DBObject dbObject=cursor.next();
			city=dbObject.get("FieldOfInterestName").toString();
			location_list.add(city);
		}		
		return location_list;
	}
	
	
	public List<String> getDegreeList()
	{
		List<String> location_list = new ArrayList<String>();
		String city=null;
		DBCursor cursor=degrees.find();
		while(cursor.hasNext())
		{
			DBObject dbObject=cursor.next();
			city=dbObject.get("degreeName").toString();
			location_list.add(city);
		}		
		return location_list;
	}
	
	public int getLocationCount()
	{
		int counter=0;
		String city=null;
		DBCursor cursor=locations.find();
		while(cursor.hasNext())
		{
			DBObject dbObject=cursor.next();
			city=dbObject.get("City").toString();
			counter++;
		}
		return counter;
	}
	
	public int getDegreeCount()
	{
		int counter=0;
		String city=null;
		DBCursor cursor=degrees.find();
		while(cursor.hasNext())
		{
			DBObject dbObject=cursor.next();
			city=dbObject.get("degreeName").toString();
			counter++;
		}
		return counter;
	}
	
	public int getCareerCount()
	{
		int counter=0;
		String city=null;
		DBCursor cursor=skills.find();
		while(cursor.hasNext())
		{
			DBObject dbObject=cursor.next();
			city=dbObject.get("FieldOfInterestName").toString();
			counter++;
		}
		return counter;
	}
	
	public void deleteEnties(String mob)
	{
		additional_pD.remove(new BasicDBObject().append("mobile", mob));
	}
	
	 public String getStatisticsTableHeaders(String mobile) {
		 String field=null;
		 try{
	        DBCollection coll = dBase.getCollection("personalDetails");
			BasicDBObject basicDBObject = new BasicDBObject();
			basicDBObject.put("mobile", mobile);
			DBCursor cursor = coll.find(basicDBObject);
			String ret = "";
			Set<String> keys = null;
			keys = cursor.next().keySet();
			System.out.println("keys is:"+keys);
			ret = keys.toString();
			System.out.println("ret is:"+ret);
			
			for (String element : keys) {
			    System.out.println("Single Element:"+element);
			    field=element;
			}
			System.out.println(field);
			
		 }catch(NoSuchElementException e)
		 {
			 field="";
		 }
			return field;		
	}
	 
	    public void insertIssue(String mobile)
		{
			BasicDBObject objDb1 = new BasicDBObject();
			objDb1.put("mobile", mobile);
			
			BasicDBObject objDb2 = new BasicDBObject();
			objDb2.put("issue", "issue");
			BasicDBObject updateDBobj = new BasicDBObject();
			updateDBobj.put("$set", objDb2);
			track.update(objDb1, updateDBobj);		
		}
	
	    public void deleteUnwanted(String mob)
	    {
	    	track.remove(new BasicDBObject().append("mobile", mob));

	    }
	    
	public static void main(String[] args) {
		Database db = new Database();
		db.deleteUnwanted("8286578819");
	//	System.out.println(db.getLIDUsingLocation("Mumbai"));;
	//	System.out.println(db.checkUserExistInUsers("8286578819"));;
	//	db.insertIntoOTPTrack();
	//	System.out.println(db.getMOBbasedOnSender("194529172"));
	//	System.out.println(db.getLIDUsingMobile("8286578819"));
	}
}
