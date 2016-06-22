package co.quickwork.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.quickwork.service.QuickWorkBot;

@WebServlet("/MainServlet")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	 QuickWorkBot bot= new QuickWorkBot();
    public MainServlet() {

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out =response.getWriter();
		String context=request.getParameter("contextobj");
		String message= request.getParameter("messageobj");	
		out.println(QuickWorkBot.showData());	
	    bot.checkUserExist(context);
	}

}
