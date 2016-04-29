package co.quickwork.database;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.BSONObject;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mysql.fabric.xmlrpc.base.Data;

public class Database {

	Connection Mcon = new Connection();
	protected DB dBase = Mcon.conn();
	
	DBCollection userTestDetails = dBase.getCollection("UserTestDetails");
	DBCollection questions = dBase.getCollection("OmniTestingQuestion");
	
	//inserting data into UserTestDetails table
	public void insertData(String context)
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("context", context);
		basicDBObject.put("questionCounter", 1);
		basicDBObject.put("checkFlag", 0);
		userTestDetails.insert(basicDBObject);
	}
	
	//this method gets checkFlag data of a user
	public int getCheckFlag(String context)
	{
		int flag = (int) userTestDetails.findOne(new BasicDBObject("context",context)).get("checkFlag");
		return flag;
	}
	
	//this method updates checkFlag of a user
	public void updateCheckFlag(String context, int flag)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("context", context);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("checkFlag", flag);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		userTestDetails.update(objDb1, updateDBobj);
		
	}
	
	// this method return questionCounter of a user
	public int getQuestionCounter(String context)
	{
		int qnCounter = (int) userTestDetails.findOne(new BasicDBObject("context",context)).get("questionCounter");
		return qnCounter;
	}


	public void insertQuestionNoAndWeight(String context,int QuestionNo,String message )
	{
		String weight=getAnswerWeight(QuestionNo,message);
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("context", context);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put(Integer.toString(QuestionNo), weight);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		userTestDetails.update(objDb1, updateDBobj);
	}
	
	//checked
	public String getAnswerWeight(int QuestionNo,String optionNo)
	{
	 String weightage=questions.findOne(new BasicDBObject("QuestionNo", QuestionNo)).get("Weightage"+optionNo).toString();
	 return weightage;
	}
	
	
	//this method return weather user exist or not
	public boolean checkUserExist(String context)
	{
	 try{
		    int value=(int) userTestDetails.findOne(new BasicDBObject("context", context)).get("questionCounter");
			return true;
			
		}catch(NullPointerException e){
			return false;
		}		
	}
	
	//this method updates questionCounter
	public void updateQuestionCounter(String context)
	{
		BasicDBObject objDb1 = new BasicDBObject();
		objDb1.put("context", context);
		
		BasicDBObject objDb2 = new BasicDBObject();
		objDb2.put("questionCounter", getQuestionCounter(context)+1);
		BasicDBObject updateDBobj = new BasicDBObject();
		updateDBobj.put("$set", objDb2);
		userTestDetails.update(objDb1, updateDBobj);
	}
	
	// this method returns question based on question number
	public String getQuestion(int questionNo)
	{
		String question=questions.findOne(new BasicDBObject("QuestionNo",questionNo)).get("Question").toString();	
		return question;
	}
	
	//Get size of option based on questionNo
	public int getOptionSize(int questionNo)
	{
		int count=0;
		for(int i=1;i<=5;i++)
		{
			if(checkOption(questionNo, i)==true)
			{
				count++;
			}
			else
			{
				//option doesnot exist;
			}
		}
		return count;
	}
	
	//this method given option value of a questionNo
	public String getOption(int i,int questionNo)
	{
		String option =questions.findOne(new BasicDBObject("QuestionNo",questionNo)).get("Option"+i).toString();
		return option;
	}
	
	//this method checks weather that Option exist or not base on Question No
	public boolean checkOption(int questionNo, int no)
	{
		boolean exist=false;
		try{
		questions.findOne(new BasicDBObject("QuestionNo",questionNo)).get("Option"+no).toString();
		exist=true;
		}
		catch(NullPointerException e)
		{
			exist=false;
		}
		return exist;
	}
	
public int getCurrentQuestionCounter(){
		
		List<Integer> questionList = getQuestionNoList();
		int maxId = getMaxQuestionNo(questionList);
		return maxId;
     }
	
	 public List<Integer> getQuestionNoList()
	 {

         List<Integer> questionNoList = new ArrayList<Integer>();
		 BasicDBObject objBasicDBObject = new BasicDBObject();
         //objBasicDBObject.put("TestingContentId", testingTitleId);
	     DBCursor questionCursor = questions.find(objBasicDBObject);

			while (questionCursor.hasNext()) {

				DBObject questionDbObj = questionCursor.next();

			    int questionNo      = Integer.parseInt(questionDbObj.get("QuestionNo").toString());
				
				questionNoList.add(questionNo);
			}
			
        return questionNoList;
	 }
	
	 public int getMaxQuestionNo(List<Integer> list){
		 int max;
		 
		 if(list.isEmpty()){
			 max=0;
		 }
		 else{
		 
		    max = Integer.MIN_VALUE;
		    for(int i=0; i<list.size(); i++){
		        if(list.get(i)> max){
		            max = list.get(i);
		        }
		    } 
			
		 }
		    return max;
	 }
	 
	 public static void main(String[] args) {
		Database d= new Database();
	//	System.out.println(d.insertQuestionNoAndWeight("{\"botname\":\"QWork03\",\"channel\":\"twitter\",\"contextid\":\"mallikagogoi7\",\"type\":\"msg\"}"));
	}
}