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
import com.mycompany.kalende.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * @description TaskDAO
 * @author Jacob
 * @date 2012-09-29
 */
public class TaskDAO {
    
    private DBCollection coll;

    public TaskDAO() {
        coll = DBConnection.getDBCollection("task");
        
        BasicDBObject query = new BasicDBObject();
        query.put("taskId", 1);
        query.put("taskName", "Project A");
        query.put("projectId", 0);
        
        DBCursor cur = coll.find(query);
        if (cur.size() == 0) {
            BasicDBObject bdbo = new BasicDBObject();
            bdbo.put("taskId", 1);
            bdbo.put("taskName", "Project A");
            bdbo.put("projectId", 0);
            coll.save(bdbo);
            
            bdbo = new BasicDBObject();
            bdbo.put("taskId", 2);
            bdbo.put("taskName", "Project B");
            bdbo.put("projectId", 0);
            coll.save(bdbo);
            
            bdbo = new BasicDBObject();
            bdbo.put("taskId", 3);
            bdbo.put("taskName", "Project C");
            bdbo.put("projectId", 0);
            coll.save(bdbo);
            
            bdbo = new BasicDBObject();
            bdbo.put("taskId", 4);
            bdbo.put("taskName", "HelpDesk");
            bdbo.put("projectId", 0);
            coll.save(bdbo);
        }
        
        query.put("taskId", 5);
        query.put("taskName", "Sleep");
        query.put("projectId", 1);
        
        DBCursor cur1 = coll.find(query);
        if (cur1.size() == 0) {
            BasicDBObject bdbo = new BasicDBObject();
            bdbo.put("taskId", 5);
            bdbo.put("taskName", "Sleep");
            bdbo.put("projectId", 1);
            coll.save(bdbo);
            
            bdbo = new BasicDBObject();
            bdbo.put("taskId", 6);
            bdbo.put("taskName", "Eat");
            bdbo.put("projectId", 1);
            coll.save(bdbo);
            
            bdbo = new BasicDBObject();
            bdbo.put("taskId", 7);
            bdbo.put("taskName", "Drive and audio");
            bdbo.put("projectId", 1);
            coll.save(bdbo);
            
            bdbo = new BasicDBObject();
            bdbo.put("taskId", 8);
            bdbo.put("taskName", "Family time");
            bdbo.put("projectId", 1);
            coll.save(bdbo);
        }
    }

    public void save(Task task) {
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("taskId", task.getTaskId());
        bdbo.put("taskName", task.getTaskName());
        bdbo.put("projectId", task.getProjectId());

        coll.save(bdbo);
    }

    public void remove(int id) {
        BasicDBObject query = new BasicDBObject();
        query.put("taskId", id);
        
        coll.remove(query);
    }

    public void update(int id, Task task) {
        BasicDBObject query = new BasicDBObject();
        query.put("taskId", id);
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("taskId", task.getTaskId());
        bdbo.put("taskName", task.getTaskName());
        bdbo.put("projectId", task.getProjectId());

        coll.update(query, bdbo);
    }

    public Task findByName(String taskName) {
        BasicDBObject query = new BasicDBObject();
        query.put("taskName", taskName);

        Task task = new Task();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            task.setTaskId((Integer) dbo.get("taskId"));
            task.setTaskName((String) dbo.get("taskName"));
            task.setProjectId((Integer) dbo.get("projectId"));
        }
        
        return task;
    }
    
    public Task find(int taskId) {
        BasicDBObject query = new BasicDBObject();
        query.put("taskId", taskId);

        Task task = new Task();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            task.setTaskId((Integer) dbo.get("taskId"));
            task.setTaskName((String) dbo.get("taskName"));
            task.setProjectId((Integer) dbo.get("projectId"));
        }
        
        return task;
    }
    
    public boolean findExistTask(int projectId, String name) {
        boolean bExist = false;
        
        BasicDBObject query = new BasicDBObject();
        query.put("projectId", projectId);
        query.put("taskName", name);
        
        DBCursor cur = coll.find(query);
        
        if (cur.count() > 0)
            bExist = true;
        
        return bExist;
    }
    
    public int getMaxTaskId() {
        DBCursor cur = coll.find();
        int iMaxId = 0;
        
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            iMaxId ++;
        }
        
        return iMaxId;
    }

    public List<Task> findList(int projectId) {
        BasicDBObject query = new BasicDBObject();
        query.put("projectId", projectId);
        
        List<Task> tasks = new ArrayList<Task>();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            Task task = new Task();
            task.setTaskId((Integer) dbo.get("taskId"));
            task.setTaskName((String) dbo.get("taskName"));
            task.setProjectId((Integer) dbo.get("projectId"));

            tasks.add(task);
        }
        return tasks;
    }
    
}