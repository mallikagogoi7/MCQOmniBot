package co.quickwork.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.json.JSONObject;

public class SendData {

	public String SendData(String phnoneNo){
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		URL myURL = null;
		try {
		    String key     ="ffbfb4c3e7877def9442c4f96237b2ec";
		    JSONObject objJsonObject = new JSONObject();
		    myURL = new URL("http://enterprise.smsgupshup.com/apps/TwoFactorAuth/incoming.php//?phone="+phnoneNo+"&key="+key+"&format="+objJsonObject);
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
	
	public String verifyCode(String phoneNo, int code){		
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		URL myURL = null;
		try {
		    String key     ="ffbfb4c3e7877def9442c4f96237b2ec";
		    JSONObject objJsonObject = new JSONObject();
		    myURL = new URL("http://enterprise.smsgupshup.com/apps/TwoFactorAuth/incoming.php?phone="+phoneNo+"&key="+key+"&code="+code+"&format="+objJsonObject);
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
}