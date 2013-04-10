/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.model;

import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author Administrator
 */
public class Schedule {
    private int scheduleId;
    private int usedAccount;
    
    private Date scheduleStartDate;
    private Date scheduleEndDate;

    private String scheduleTitle;
    
    private Task task;
    private User user;
    
    public int getScheduleId() {
        return scheduleId;
    }
    
    public void setScheduleId(int id) {
        this.scheduleId = id;
    }
    
    public int getUsedAccount() {
        return usedAccount;
    }
    
    public void setUsedAccount(int account) {
        this.usedAccount = account;
    }
    
    public Date getScheduleStartDate() {
        return scheduleStartDate;
    }
    
    public void setScheduleStartDate(Date date) {
        this.scheduleStartDate = date;
    }
    
    public Date getScheduleEndDate() {
        return scheduleEndDate;
    }

    public void setScheduleEndDate(Date scheduleEndDate) {
        this.scheduleEndDate = scheduleEndDate;
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
    
    /**
    * Convert this object to a JSON object for representation
    */
    public JSONObject toJSON() {
        try{
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("id", this.scheduleId);
            jsonobj.put("start", this.scheduleStartDate);
            jsonobj.put("end", this.scheduleEndDate);
            jsonobj.put("title", this.scheduleTitle);
            return jsonobj;
        }catch(Exception e){
            return null;
        }
    }

    /**
    * Convert this object to a string for representation
    */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("id:");
        sb.append(this.scheduleId);
        sb.append(",start:");
        sb.append(this.scheduleStartDate);
        sb.append(",end:");
        sb.append(this.scheduleEndDate);
        sb.append(",title:");
        sb.append(this.scheduleTitle);
        return sb.toString();
    }
}
