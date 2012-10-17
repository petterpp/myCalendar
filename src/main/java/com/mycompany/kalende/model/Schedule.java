/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.model;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class Schedule {
    private int scheduleId;
    private Date scheduleDate;
    private String scheduleTime;
    private String scheduleTitle;
    
    private Task task;
    private User user;
    
    public int getScheduleId() {
        return scheduleId;
    }
    
    public void setScheduleId(int id) {
        this.scheduleId = id;
    }
    
    public Date getScheduleDate() {
        return scheduleDate;
    }
    
    public void setScheduleDate(Date date) {
        this.scheduleDate = date;
    }
    
    public String getScheduleTime() {
        return scheduleTime;
    }
    
    public void setScheduleTime(String time) {
        this.scheduleTime = time;
    }
    
    public String getScheduleTitle() {
        return scheduleTitle;
    }
    
    public void setScheduleTitle(String title) {
        this.scheduleTitle = title;
    }
    
    public Task getTask() {
        return task;
    }
    
    public void setTask(Task task) {
        this.task = task;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
