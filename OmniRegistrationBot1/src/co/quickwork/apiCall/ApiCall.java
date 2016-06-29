package co.quickwork.apiCall;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import co.quickwork.database.Database;

public class ApiCall {
	Database database= new Database();	
   public void insertIntoUserAndPD(String sender) throws Exception {
    String senderID=database.getMessage(sender, "channelid");
	String mobile=database.getMOBbasedOnSender(senderID);
	String name=database.getUserNameUsingMobileFromAdditionPD(mobile);
	String personYOP=database.getUserYOPUsingMobileFromAdditionPD(mobile);
	String did=database.getUserDIDUsingMobileFromAdditionPD(mobile);
	String gender=database.getUserGenderUsingMobileFromAdditionPD(mobile);
	String lid=database.getLIDUsingMobileFromAdditionPD(mobile);
	
		String urlParameters = "name="+URLEncoder.encode(name)+ "&mobile=" + URLEncoder.encode(mobile)
			            	+ "&personYOP=" + URLEncoder.encode(personYOP) + "&did=" + URLEncoder.encode(did)
			        	    +"&gender=" + URLEncoder.encode(gender)+"&otp=" + URLEncoder.encode("8124")+"&lid=" + URLEncoder.encode(lid);
		String url = "http://52.33.177.69:8124/qw_v21/api/v1/Authenticate/users";

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		String USER_AGENT="Mozilla/5.0";
		try{
		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'POST' request to URL : " + url);
//		System.out.println("Post parameters : " + urlParameters);
//		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();		
	//	System.out.println(response.toString());
		}catch(Exception e){
	    e.printStackTrace();
		}

	}

}
