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
import com.mycompany.kalende.model.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * @description ProjectDAO
 * @author Jacob
 * @date 2012-09-29
 */
public class ProjectDAO {
    private DBCollection coll;
    
    public ProjectDAO() {
        if (coll == null)
            coll = DBConnection.getDBCollection("project");
        
        BasicDBObject query = new BasicDBObject();
        query.put("projectId", 1);
        query.put("projectName", "");
        query.put("categoryId", 1);
        
        DBCursor cur = coll.find(query);
        if (cur.size() == 0) {
            BasicDBObject bdbo = new BasicDBObject();
            bdbo.put("projectId", 1);
            bdbo.put("projectName", "");
            bdbo.put("categoryId", 1);
            coll.save(bdbo);            
        }
    }

    public void save(Project project) {
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("projectId", project.getProjectId());
        bdbo.put("projectName", project.getProjectName());
        bdbo.put("categoryId", project.getCategoryId());

        coll.save(bdbo);
    }

    public void remove(int id) {
        BasicDBObject query = new BasicDBObject();
        query.put("projectId", id);
        
        coll.remove(query);
    }

    public void update(int id, Project project) {
        BasicDBObject query = new BasicDBObject();
        query.put("projectId", id);
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("projectId", project.getProjectId());
        bdbo.put("projectName", project.getProjectName());
        bdbo.put("categoryId", project.getCategoryId());

        coll.update(query, bdbo);
    }

    public Project find(int id) {
        BasicDBObject query = new BasicDBObject();
        query.put("projectId", id);

        Project project = new Project();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            project.setProjectId((Integer) dbo.get("projectId"));
            project.setProjectNaem((String) dbo.get("projectName"));
            project.setCategoryId((Integer) dbo.get("categoryId"));
        }
        
        return project;
    }
    
     public Project find(String name) {
        BasicDBObject query = new BasicDBObject();
        query.put("projectName", name);

        Project project = new Project();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            project.setProjectId((Integer) dbo.get("projectId"));
            project.setProjectNaem((String) dbo.get("projectName"));
            project.setCategoryId((Integer) dbo.get("categoryId"));
        }
        
        return project;
    }
    
    public boolean findExistProject(int categoryId, String name) {
        boolean bExist = false;
        
        BasicDBObject query = new BasicDBObject();
        query.put("categoryId", categoryId);
        query.put("projectName", name);
        
        DBCursor cur = coll.find(query);
        
        if (cur.count() > 0)
            bExist = true;
        
        return bExist;
    }
    
    public int getMaxProjectId() {
        DBCursor cur = coll.find();
        int iMaxId = 0;
        
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            iMaxId ++;
        }
        
        return iMaxId;
    }

    public List<Project> findList(int categoryId) {
        BasicDBObject query = new BasicDBObject();
        query.put("categoryId", categoryId);
        
        List<Project> projects = new ArrayList<Project>();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            Project project = new Project();
            project.setProjectId((Integer) dbo.get("projectId"));
            project.setProjectNaem((String) dbo.get("projectName"));
            project.setCategoryId((Integer) dbo.get("categoryId"));

            projects.add(project);
        }
        return projects;
    }
    
}