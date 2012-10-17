/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.model;

/**
 * @date 2012-09-30
 * @author Jacob
 */
public class Task {
    private int taskId;
    private String taskName;
    
    private int projectId;
    
    public int getTaskId() {
        return taskId;
    }
    
    public void setTaskId(int id) {
        this.taskId = id;
    }
    
    public String getTaskName() {
        return taskName;
    }
    
    public void setTaskName(String name) {
        this.taskName = name;
    }
    
    public int getProjectId() {
        return projectId;
    }
    
    public void setProjectId(int id) {
        this.projectId = id;
    }
    
}