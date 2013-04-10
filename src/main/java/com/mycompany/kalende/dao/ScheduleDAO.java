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
import com.mycompany.kalende.model.Schedule;
import com.mycompany.kalende.model.User;
import com.mycompany.kalende.model.Task;
import java.sql.Date;

import java.util.ArrayList;
import java.util.List;

/**
 * @description ScheduleDAO
 * @author Jacob
 * @date 2012-09-29
 */
public class ScheduleDAO {
    private DBCollection coll;

    public ScheduleDAO() {
        
        if (coll == null)
            coll = DBConnection.getDBCollection("schedule");
                
    }

    public void save(Schedule schedule) {
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("scheduleId", schedule.getScheduleId());
        bdbo.put("scheduleStartDate", schedule.getScheduleStartDate());
        bdbo.put("scheduleEndDate", schedule.getScheduleEndDate());
        bdbo.put("scheduleTitle", schedule.getScheduleTitle());
        bdbo.put("taskId", schedule.getTask().getTaskId());
        bdbo.put("usedAccount", schedule.getUsedAccount());
        bdbo.put("userId", schedule.getUser().getId());

        coll.save(bdbo);
    }

    public void remove(int id) {
        BasicDBObject query = new BasicDBObject();
        query.put("scheduleId", id);
        
        coll.remove(query);
    }

    public void update(Schedule schedule) {
        BasicDBObject query = new BasicDBObject();
        query.put("scheduleId", schedule.getScheduleId());
        BasicDBObject bdbo = new BasicDBObject();
        bdbo.put("scheduleId", schedule.getScheduleId());
        bdbo.put("scheduleStartDate", schedule.getScheduleStartDate());
        bdbo.put("scheduleEndDate", schedule.getScheduleEndDate());
        bdbo.put("scheduleTitle", schedule.getScheduleTitle());
        bdbo.put("taskId", schedule.getTask().getTaskId());
        bdbo.put("usedAccount", schedule.getUsedAccount());
        bdbo.put("userId", schedule.getUser().getId());

        coll.update(query, bdbo);
    }

    public List<Schedule> find(User user) {
        BasicDBObject query = new BasicDBObject();
        query.put("userId", user.getUsername());
        
        List<Schedule> schedules = new ArrayList<Schedule>();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            Schedule schedule = new Schedule();
            schedule.setScheduleId((Integer) dbo.get("scheduleId"));
            schedule.setScheduleStartDate((Date) dbo.get("scheduleStartDate"));
            schedule.setScheduleEndDate((Date) dbo.get("scheduleEndDate"));
            schedule.setScheduleTitle((String) dbo.get("scheduleTitle"));
            schedule.setTask((Task) dbo.get("taskId"));
            schedule.setUsedAccount((Integer) dbo.get("usedAccount"));
            schedule.setUser((User) dbo.get("userId"));

            schedules.add(schedule);
        }
        return schedules;
    }

    public List<Schedule> find(User user, Date startDate, Date endDate) {
        BasicDBObject date = new BasicDBObject();
        date.append("$gte", startDate);
        date.append("$lte", endDate);
        
        BasicDBObject query = new BasicDBObject();
        query.put("userId", user.getUsername());
        query.put("scheduleStartDate", date);
        query.put("scheduleEndDate", date);
        
        List<Schedule> schedules = new ArrayList<Schedule>();
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            Schedule schedule = new Schedule();
            schedule.setScheduleId((Integer) dbo.get("scheduleId"));
            schedule.setScheduleStartDate((Date) dbo.get("scheduleStartDate"));
            schedule.setScheduleEndDate((Date) dbo.get("scheduleEndDate"));
            schedule.setScheduleTitle((String) dbo.get("scheduleTitle"));
            schedule.setTask((Task) dbo.get("taskId"));
            schedule.setUsedAccount((Integer) dbo.get("usedAccount"));
            schedule.setUser((User) dbo.get("userId"));

            schedules.add(schedule);
        }
        return schedules;
    }
    
    public Integer findUsedAccount(User user, Task task ) {        
        BasicDBObject query = new BasicDBObject();
        query.put("userId", user.getUsername());
        query.put("task", task);
                
        int iUsedAccount = 0;
        DBCursor cur = coll.find(query);
        while (cur.hasNext()) {
            DBObject dbo = cur.next();
            iUsedAccount ++;
        }
        
        return iUsedAccount;
    }   
}