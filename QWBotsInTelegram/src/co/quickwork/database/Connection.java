
package co.quickwork.database;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class Connection {
	DB dBase;
	public MongoClient CLIENT ;
	public DB conn()
	{
		try{
	      CLIENT = new MongoClient("quickworkapp.com",27017) ;
          dBase = CLIENT.getDB("qw_production_v21"); 
        }catch(Exception E){
	      E.printStackTrace();
        }
        return dBase;  	
	} 
}
