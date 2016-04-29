package co.quickwork.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import co.quickwork.database.Database;
import co.quickwork.service.Validation;

@WebServlet("/MCQServlet")
public class MCQServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
	Database database= new Database();
    Validation validation=new Validation();
  //  private QuickWorkPropertyGiver qwPropGiver = null;
    
    public MCQServlet() {
        super();
      
    }   
   
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     
		PrintWriter out= response.getWriter();
		String context=request.getParameter("context");
		String message =request.getParameter("message");
		boolean var=database.checkUserExist(context);
		
		if(var==false)
		{
			database.insertData(context);
		}
		else
		{
			//user already exist
		}
//		
		String lineBreak ="                                                                 ";
    	String data;
    	 	
    	if(database.getCheckFlag(context)==1)
    	{
    		int questionCounter=database.getQuestionCounter(context);
        	int questionSize=database.getMaxQuestionNo(database.getQuestionNoList());
        	
    		if(validation.validateMessage(message, questionCounter)==true &&questionCounter<=questionSize)
    		{
		    	database.insertQuestionNoAndWeight(context,questionCounter,message);
		    	database.updateQuestionCounter(context);	
		    	int questionNo=database.getQuestionCounter(context);
		    	String question=database.getQuestion(questionNo);
    			out.println("Question"+questionNo+":  "+question+lineBreak);
		    	int optionSize=database.getOptionSize(database.getQuestionCounter(context));
		    	for(int i=1;i<=optionSize;i++)
		    	{
		    		String opt=database.getOption(i,database.getQuestionCounter(context));
		    		out.println(i +" "+opt);		    	
		    	}
		    
    		}
    		else if(questionCounter==questionSize+1)
    		{
    			out.println("Thank for your time");
    		}
    		else
    		{
    			out.println("Invalid entry. Please type a valid option number");
    		}
				
    	}
    	else
    	{
    		if(database.getCheckFlag(context)==0)
    		{
    			out.println("Welcome to QuickWork Technology Pvt Ltd MCQ Test");
    			int questionNo=database.getQuestionCounter(context);
		    	String question=database.getQuestion(questionNo);
    			out.println("Question"+questionNo+":  "+question+lineBreak);
    			int optionSize=database.getOptionSize(database.getQuestionCounter(context));
		    	for(int i=1;i<=optionSize;i++)
		    	{
		    		String opt=database.getOption(i,database.getQuestionCounter(context));
		    		out.println(i +" "+opt);		    	
		    	}
		    	database.updateCheckFlag(context, 1);
    		}
    		else
    		{
    			out.println("Invalid entry. Please type a valid option number");
    		}
    	}
	}
}
