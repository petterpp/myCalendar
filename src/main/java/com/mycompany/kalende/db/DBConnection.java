/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.db;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import java.net.UnknownHostException;

public class DBConnection {

    private static Mongo m;
    private static DB db;

    public static DBCollection getDBCollection(String collection) {
        try {
            m = new Mongo("localhost", 27017);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        } catch (MongoException e) {
            e.printStackTrace();
        }
        
        db = m.getDB("Calender");
        
        return db.getCollection(collection);
    }
}
