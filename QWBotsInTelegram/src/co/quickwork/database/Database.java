package co.quickwork.database;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

public class Database {
	
	Connection Mcon = new Connection();
	protected DB dBase = Mcon.conn();
	DBCollection track = dBase.getCollection("telegramBotTrack");	
	
	public void insertData(String context)
	{
		BasicDBObject basicDBObject= new BasicDBObject();
		basicDBObject.put("context", context);
		track.insert(basicDBObject);
	}
	
	public  boolean checkUserExist(String context)
	{
		boolean exist=false;
		try{
		track.findOne(new BasicDBObject("context", context)).get("_id");
		exist=true;
		}
		catch(NullPointerException e)
		{
			exist=false;
		}
		return exist;
	}
}