/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mycompany.kalende.db.DBConnection;
import com.mycompany.kalende.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private DBCollection coll;

    public UserDAO() {
        coll = DBConnection.getDBCollection("user");
        
        BasicDBObject query = new BasicDBObject();
        query.put("username", "test");
        query.put("password", "test");
        DBCursor cur = coll.find(query);
        if (cur.size() == 0) {
            BasicDBObject bdbo = new BasicDBObject();
            bdbo.put("id", 1);
            bdbo.put("username", "test");
            bdbo.put("password", "test");
            bdbo.put("email", "");
            bdbo.put("status", true);
            coll.save(bdbo);
        }
    }

    public void save(User user) {
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("id", user.getId());
        bdbo.put("username", user.getUsername());
        bdbo.put("password", user.getPassword());
        bdbo.put("email", user.getEmailAdd());
        bdbo.put("status", user.getStatus());
        coll.save(bdbo);
    }

    public void remove(int id) {
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        coll.remove(query);
    }

    public void update(int id, User user) {
        BasicDBObject query = new BasicDBObject();
        query.put("id", id);
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("id", user.getId());
        bdbo.put("username", user.getUsername());
        bdbo.put("password", user.getPassword());
        bdbo.put("email", user.getEmailAdd());
        bdbo.put("status", user.getStatus());
        coll.update(query, bdbo);
    }

    public User find(String username, String password) {
        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
        query.put("password", password);
        User user = new User();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            user.setId((Integer) dbo.get("id"));
            user.setUsername((String) dbo.get("username"));
            user.setPassword((String) dbo.get("password"));
            user.setEmailAdd((String) dbo.get("emailAdd"));
            user.setStatus((Boolean) dbo.get("status"));
        }
        return user;
    }
    
    public Boolean checkUser(String username, String password) {
        BasicDBObject query = new BasicDBObject();
        query.put("username", username);
        query.put("password", password);
        Boolean bool = false;
        DBCursor cur = coll.find(query);
        System.out.print(cur.count());
        System.out.print(cur.getQuery());
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            bool = true;
        }
        return bool;
    }
    
    public User find(int userId) {
        BasicDBObject query = new BasicDBObject();
        query.put("id", userId);

        User user = new User();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            user.setId((Integer) dbo.get("id"));
            user.setUsername((String) dbo.get("username"));
            user.setPassword((String) dbo.get("password"));
            user.setEmailAdd((String) dbo.get("emailAdd"));
            user.setStatus((Boolean) dbo.get("status"));
        }
        return user;
    }

    public List<User> find() {
        List<User> users = new ArrayList<User>();
        DBCursor cur = coll.find();
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            User user = new User();
            user.setId((Integer) dbo.get("id"));
            user.setUsername((String) dbo.get("username"));
            user.setPassword((String) dbo.get("password"));
            user.setEmailAdd((String) dbo.get("emailAdd"));
            user.setStatus((Boolean) dbo.get("status"));
            users.add(user);
        }
        return users;
    }

    public List<User> find(DBObject query) {
        List<User> users = new ArrayList<User>();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            User user = new User();
            user.setId((Integer) dbo.get("id"));
            user.setUsername((String) dbo.get("username"));
            user.setPassword((String) dbo.get("password"));
            user.setEmailAdd((String) dbo.get("emailAdd"));
            user.setStatus((Boolean) dbo.get("status"));
            users.add(user);
        }
        return users;
    }

    public User findOne() {
        DBObject dbo = coll.findOne();
        User user = new User();
        
        user.setId((Integer) dbo.get("id"));
        user.setUsername((String) dbo.get("username"));
        user.setPassword((String) dbo.get("password"));
        user.setEmailAdd((String) dbo.get("emailAdd"));
        user.setStatus((Boolean) dbo.get("status"));
        
        return user;
    }
}