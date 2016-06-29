package co.quickwork.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;

    class Connection {
	DB dBase;
	private MongoClient CLIENT ;
	protected DB conn()
	{
		try{
		    CLIENT = new MongoClient("52.33.177.69",27017) ;
		    dBase = CLIENT.getDB("qw_v21"); 
		     
        }catch(Exception E){
	      E.printStackTrace();
        }
        return dBase;  	
	}
}
