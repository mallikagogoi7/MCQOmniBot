package co.quickwork.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class TestDB {
	private Connection Mcon = new Connection();
	private DB dBase = Mcon.conn();
	DBCollection applications = dBase.getCollection("applications");
	DBCollection users = dBase.getCollection("users");
	DBCollection detail=dBase.getCollection("personalDetails");
	DBCollection checkInLocation=dBase.getCollection("checkIn");
	DBCollection checkOutLocation=dBase.getCollection("checkOut");
	
	public void justTest(String mob)
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("mobile", mob);
		String id=null;
		
			DBCursor cursor=users.find();
		    while(cursor.hasNext())
			{
				DBObject dbObject=cursor.next();
				id=dbObject.get("_id").toString();
			}	
		
		System.out.println(id);
		
	}
		
	public String getTodayDate()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
	    Date today = null;
		try {
			today = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		
		String Today=today.toString();
	    String[] parts = Today.split(" ");
	    //System.out.println("Day: " + parts[0]);
	    String dateToday=parts[0]+" "+parts[1]+" "+parts[2]+" "+parts[5];
	    return dateToday;
	}
	
public List<String>getMobileNumber(){
		
		List<String> userMobile = new ArrayList<String>();
		
		String mobile=null;
		BasicDBObject basicDBObject=new BasicDBObject();
		basicDBObject.put("appstatus", 3);
		
		DBCursor mobileCursor = applications.find(basicDBObject);
		while(mobileCursor.hasNext())
		{
			DBObject objDBObject=mobileCursor.next();
			mobile=objDBObject.get("mobile").toString();
			userMobile.add(mobile);
		}
		
		return userMobile;
		
	}

public List<String> getUserCheckOutLocationUsingMobileandDate(List<String> userMobile,String today) throws  JSONException
{
	List<String> Location =new ArrayList<String>();
	BasicDBObject basicDBObject=new BasicDBObject();
	
	//DBCursor CheckInLocationCursor=checkInLocation.find();
//	for(int i=0; i<userMobile.size();i++)
//	{
		basicDBObject.put("mobile","9930770326");
		
		DBCursor CheckInLocationCursor = checkOutLocation.find(basicDBObject);
		System.out.println("1");
		while(CheckInLocationCursor.hasNext()){
			System.out.println("2");
			
			DBObject dbObject=CheckInLocationCursor.next();
			String dateTime=dbObject.get("dateTime").toString();
			String Today=dateTime.toString();
		    String[] parts = Today.split(" ");
		    String dateToday=parts[0]+" "+parts[1]+" "+parts[2]+" "+parts[5];
		    if(dateToday.matches(today))
		    {
			String lat=dbObject.get("lat").toString();
			String lon=dbObject.get("lng").toString();
			String json=apiCall(lat, lon);
			JSONObject res=new JSONObject(json);
			JSONArray arrResults=res.getJSONArray("results");
			
			JSONObject obj=arrResults.getJSONObject(0);
			String Add=obj.getString("formatted_address");
			System.out.println("ADD"+Add);
			if(Add.length()>0)
			{
     //setResult()
	 
		//System.out.println("add is:"+Add);
				
		Location.add(Add);
     
			}
			
 
		    } 
		    else
		    {
		    	System.out.println("ayyub");
		    }
		}						
//	}
		return Location;
}

public String apiCall(String lat, String lon){
	StringBuilder sb = new StringBuilder();
	URLConnection urlConn = null;
	InputStreamReader in = null;
	URL myURL = null;
	
	//myURL = new URL("http://enterprise.smsgupshup.com/apps/TwoFactorAuth/incoming.php?phone="+phoneNo+"&key="+key+"&code="+code+"&format="+objJsonObject);
	try{
	   myURL = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&sensor=false");
	   
	  System.out.println("My URL Is:"+myURL);
		
		//URL url = new URL(myURL);
		
		urlConn = myURL.openConnection();
		
		if (urlConn != null)
			
			urlConn.setReadTimeout(60 * 1000);
		
		if (urlConn != null && urlConn.getInputStream() != null) {
			
			in = new InputStreamReader(urlConn.getInputStream(), Charset.defaultCharset());
			
			BufferedReader bufferedReader = new BufferedReader(in);
			
			if (bufferedReader != null) {
				
				int cp;
				while ((cp = bufferedReader.read()) != -1) {
					
					sb.append((char) cp);
				}
				
				bufferedReader.close();
			}
		}
	in.close();
	} catch (Exception e) {
		
		throw new RuntimeException("Exception while calling URL:"+ myURL, e);
	} 
	return sb.toString();				
	
}

public void getUser(String mobile,String date)
{
	BasicDBObject basicDBObject = new BasicDBObject();
	basicDBObject.put("mobile", "9930770326");
}

public static void main(String[] args) {
	TestDB db =new TestDB();
//	List<String> mobile=db.getMobileNumber();
	String today=db.getTodayDate();
	System.out.println("today: "+today);
//	try {
//	//	db.getUserCheckOutLocationUsingMobileandDate(mobile,today);
//	} catch (JSONException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
}

}
