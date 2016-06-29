package co.quickwork.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.quickwork.apiCall.ApiCall;
import co.quickwork.database.Database;
import co.quickwork.service.OTPValidation;
import co.quickwork.service.Registration;
import co.quickwork.service.Validation;

@WebServlet("/OmniRegistrationS")
public class OmniRegistrationS extends HttpServlet {
	private static final long serialVersionUID = 1L;
    Database database= new Database();
    Validation validation= new Validation(); 
    Registration reg = new Registration();
    public OmniRegistrationS() {
      
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out= response.getWriter();
		String sender=request.getParameter("senderobj");
		String message =request.getParameter("messageobj");
		String channel =request.getParameter("channel");
		
		String text = database.getMessage(message,"text");	
	    String channelID = database.getMessage(sender,"channelid");	
		if(database.userValidation(channelID)==false)  			
		{
			OTPValidation otpValidation= new OTPValidation();
			String resp=otpValidation.validation(sender, text, channelID);
			out.println(resp);
		}
		else if(text.equalsIgnoreCase("no"))
		{
			out.println("You can click on 'YES' and get started with registration process");
		}
		else
		{
			String resp1=reg.registrationProcess(sender,text,channel, message);
			out.println(resp1);
		}
	}
}