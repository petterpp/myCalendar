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
import com.mycompany.kalende.global.Global;
import com.mycompany.kalende.model.Category;
import com.mycompany.kalende.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @description CategoryDAO
 * @author Jacob
 * @date 2012-09-29
 */
public class CategoryDAO {
    private UserDAO userDAO;
    
    private DBCollection coll;

    public CategoryDAO() {
        coll = DBConnection.getDBCollection("category");
        
        BasicDBObject query = new BasicDBObject();
        query.put("categoryName", "Favorites");
        DBCursor cur = coll.find(query);
        if (cur.size() == 0) {
            BasicDBObject bdbo = new BasicDBObject();
            bdbo.put("categoryId", 1);
            bdbo.put("categoryName", "Favorites");
            bdbo.put("userId", Global.getUserId());

            coll.save(bdbo);
        }
    }

    public void save(Category category) {
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("categoryId", category.getCategoryId());
        bdbo.put("categoryName", category.getCategoryName());
        bdbo.put("userId", category.getUserId());

        coll.save(bdbo);
    }

    public void remove(int id) {
        BasicDBObject query = new BasicDBObject();
        query.put("categoryId", id);
        coll.remove(query);
    }

    public void update(int id, Category category) {
        BasicDBObject query = new BasicDBObject();
        query.put("categoryId", id);
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("categoryId", category.getCategoryId());
        bdbo.put("categoryName", category.getCategoryName());
        bdbo.put("userId", category.getUserId());

        coll.update(query, bdbo);
    }

    public Category find(String name) {
        BasicDBObject query = new BasicDBObject();
        query.put("categoryName", name);

        Category category = new Category();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            category.setCategoryId((Integer) dbo.get("categoryId"));
            category.setCategoryName((String) dbo.get("categoryName"));
            category.setUserId((Integer) dbo.get("userId"));
        }
        
        return category;
    }
    
    public Category find(int id) {
        BasicDBObject query = new BasicDBObject();
        query.put("categoryId", id);

        Category category = new Category();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            category.setCategoryId((Integer) dbo.get("categoryId"));
            category.setCategoryName((String) dbo.get("categoryName"));
            category.setUserId((Integer) dbo.get("userId"));
        }
        
        return category;
    }

    public List<Category> findList(int userId) {
        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);
        
        List<Category> categories = new ArrayList<Category>();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            Category category = new Category();
            category.setCategoryId((Integer) dbo.get("categoryId"));
            category.setCategoryName((String) dbo.get("categoryName"));
            category.setUserId((Integer) dbo.get("userId"));

            categories.add(category);
        }
        return categories;
    }
    
    public boolean findExistCategory(int userId, String name) {
        boolean bExist = false;
        
        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);
        query.put("categoryName", name);
        
        DBCursor cur = coll.find(query);
        
        if (cur.count() > 0)
            bExist = true;
        
        return bExist;
    }
    
    public int getMaxCategoryId() {
        DBCursor cur = coll.find();
        int iMaxId = 0;
        
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            iMaxId ++;
        }
        
        return iMaxId;
    }
    
    public UserDAO getUserDAO() {
        if (userDAO == null)
            userDAO = new UserDAO();
        
        return userDAO;
    }
}