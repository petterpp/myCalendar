/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.controller;

import com.mycompany.kalende.dao.CategoryDAO;
import com.mycompany.kalende.dao.ProjectDAO;
import com.mycompany.kalende.dao.TaskDAO;
import com.mycompany.kalende.dao.UserDAO;
import com.mycompany.kalende.global.Global;
import com.mycompany.kalende.model.Category;
import com.mycompany.kalende.model.Project;
import com.mycompany.kalende.model.Task;
import javax.faces.event.ActionEvent;

/**
 *
 * @author Administrator
 */
public class CategoryController {
    private String regName;     //text name in New Category Dialog
    private String confirmation;    //Confirm Show Command
    
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    
    public int iCategoryId;     //Check Category Id in New Register Action
    public int iProjectId;      //Check Project Id in New Register Action
    public int iTaskId;         //Check Task Id in New Tegister Action
    
    public CategoryController() {
        
    }
    
    /**
     * @discription: Action function from Save Dialog
     * @param event 
     */
    public void submitAction(ActionEvent event) {
        System.out.print("submit");
        String strTreeName = "";
        String strTreeType = "";
        
        try {
            strTreeName = Global.strTreeName.get(Global.strTreeName.size()-1);
            strTreeType = Global.strTreeType.get(Global.strTreeType.size()-1);
        } catch (Exception ex) {
            
        }
        
        if (strTreeType.equals("category")) {
            if (!getCategoryDAO().findExistCategory(Global.getUserId(), getRegName())) {
                Category category = new Category();
                category.setCategoryId(getCategoryDAO().getMaxCategoryId()+1);
                category.setCategoryName(getRegName());
                category.setUserId(Global.getUserId());

                getCategoryDAO().save(category);
            } else {
                submitConfirm();
            }
        } else if (strTreeType.equals("project")) {
            iCategoryId = getProjectDAO().find(strTreeName).getCategoryId();
            
            if (!getProjectDAO().findExistProject(iCategoryId, getRegName())) {
                Project project = new Project();
                project.setCategoryId(iCategoryId);
                project.setProjectId(getProjectDAO().getMaxProjectId()+1);
                project.setProjectNaem(getRegName());
                
                getProjectDAO().save(project);
            } else {
                submitConfirm();
            }
        } else if (strTreeType.equals("Task")) {
            iProjectId = getTaskDAO().find(strTreeName).getProjectId();
            
            if (!getTaskDAO().findExistTask(iProjectId, getRegName())) {
                Task task = new Task();
                task.setTaskId(getTaskDAO().getMaxTaskId()+1);
                task.setProjectId(iProjectId);
                task.setTaskName(getRegName());
                
                getTaskDAO().save(task);
            } else {
                submitConfirm();
            }
        }
    }
    
    /**
     * @description: Get action to Save Dialog 
     */
    public String submitConfirm() {
        String strSubmit = "";
        
        
        return strSubmit;
    }
    
    
    public String getRegName() {
        return regName;
    }
    
    public void setRegName(String str) {
        this.regName = str;
    }
    
    public String getConfirmation() {
        return confirmation;
    }
    
    public void setConfirmation(String str) {
        this.confirmation = str;
    }
    
    public UserDAO getUserDAO() {
        if (userDAO == null)
            userDAO = new UserDAO();

        return userDAO;
    }
    
    public CategoryDAO getCategoryDAO() {
        if (categoryDAO == null)
            categoryDAO = new CategoryDAO();
        
        return categoryDAO;
    }
    
    public ProjectDAO getProjectDAO() {
        if (projectDAO == null)
            projectDAO = new ProjectDAO();
        
        return projectDAO;
    }
    
    public TaskDAO getTaskDAO() {
        if (taskDAO == null)
            taskDAO = new TaskDAO();
        
        return taskDAO;
    }
}
