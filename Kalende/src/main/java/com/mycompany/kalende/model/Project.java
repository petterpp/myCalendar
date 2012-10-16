/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.model;

/**
 *
 * @author Administrator
 */
public class Project {
    private int projectId;
    private String projectName;
    
    private int categoryId;
    
    public int getProjectId() {
        return projectId;
    }
    
    public void setProjectId(int id) {
        this.projectId = id;
    }
    
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectNaem(String name) {
        this.projectName = name;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int id) {
        this.categoryId = id;
    }
}
