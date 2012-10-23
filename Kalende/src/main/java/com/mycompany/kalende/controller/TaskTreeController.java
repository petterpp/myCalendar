/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.controller;

import com.mycompany.kalende.bean.TaskTree;
import com.mycompany.kalende.dao.CategoryDAO;
import com.mycompany.kalende.dao.ProjectDAO;
import com.mycompany.kalende.dao.TaskDAO;
import com.mycompany.kalende.dao.UserDAO;
import com.mycompany.kalende.global.Global;
import com.mycompany.kalende.model.Category;
import com.mycompany.kalende.model.Project;
import com.mycompany.kalende.model.Task;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import org.primefaces.context.DefaultRequestContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
  
import org.primefaces.model.DefaultTreeNode;  
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;  
  
/**
 *
 * @author Jacob
 */
@ManagedBean(name="taskTreeBean")
@RequestScoped

public class TaskTreeController {
    private TreeNode root;
    
    private String lblRegName;       //label lblRegName in New Category Dialog
    private String regName;          //New Name text in New Category Dialog
    public String dlgHeader;        //Header in New Category Dialog
    public String strSelTreeType = "";      //Now selected tree's node name
    
    private String strSelectedTreeType = "";     //Selected Type Controll Name in Tree
    private String strSelectedTreeName = "";     //Selected Leaf or Node Name in Tree
    
    public int iCategoryId;     //Check Category Id in New Register Action
    public int iProjectId;      //Check Project Id in New Register Action
    public int iTaskId;         //Check Task Id in New Tegister Action
    
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    
    public List<Category> categoryList = new ArrayList<Category>();
    public List<Project> projectList = new ArrayList<Project>();
    public List<Task> taskList = new ArrayList<Task>();
    
    private TreeNode selectedNode; 
    
    public TaskTreeController() {
               
    }
    
    @PostConstruct
    public void init() {
        categoryList = getCategoryDAO().findList(Global.getUserId());
        
        TreeNode[] categoryName = new TreeNode[100];
        TreeNode[] projectName = new TreeNode[100];
        TreeNode[] taskName = new TreeNode[100];
        
        root = new DefaultTreeNode("root", null);
        root.setExpanded(true);
        
        for (int i = 0; i < categoryList.size(); i ++ ) {
            categoryName[i] = new DefaultTreeNode("category", categoryList.get(i).getCategoryName(), root);
            
            projectList = getProjectDAO().findList(categoryList.get(i).getCategoryId());
            
            for (int j = 0; j < projectList.size(); j ++) {
                projectName[j] = new DefaultTreeNode("project", projectList.get(j).getProjectName(), categoryName[i]);
                
                taskList = getTaskDAO().findList(projectList.get(j).getProjectId());
                
                for (int k = 0; k < taskList.size(); k ++) {
                    taskName[k] = new DefaultTreeNode("task", taskList.get(k).getTaskName(), projectName[j]);
                }
            }
        }
        
        TreeNode nextCategory = new DefaultTreeNode("new", "New Category", root);
        nextCategory.setSelectable(false);
    }
    
    public void categoryReg(ActionEvent event) {
        String strSelTreeName = "";
        String strSelTreeType = "";
        
        RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg = null;  
        boolean loggedIn = false;  
          
        try {
            strSelTreeName = Global.strTreeName.get(Global.strTreeName.size()-1);
            strSelTreeType = Global.strTreeType.get(Global.strTreeType.size()-1);
        } catch (Exception ex) {
            
        }
        
        if (strSelTreeType.equals("category")) {
            iCategoryId = getCategoryDAO().find(strSelTreeName).getCategoryId();
            
            if (!getProjectDAO().findExistProject(iCategoryId, getRegName())) {
                Project project = new Project();
                project.setCategoryId(iCategoryId);
                project.setProjectId(getProjectDAO().getMaxProjectId()+1);
                project.setProjectNaem(getRegName());
                
                getProjectDAO().save(project);
                
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully Register the Project '" +getRegName()+ "'");
            } else {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Register Error", "Exist the Project as same name.");
            }
        } else if (strSelTreeType.equals("project")) {
            iProjectId = getProjectDAO().find(strSelTreeName).getProjectId();
            
            if (!getTaskDAO().findExistTask(iProjectId, getRegName())) {
                Task task = new Task();
                task.setTaskId(getTaskDAO().getMaxTaskId()+1);
                task.setProjectId(iProjectId);
                task.setTaskName(getRegName());
                
                getTaskDAO().save(task);
                
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully Register the Task '" +getRegName()+ "'");
            } else {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Register Error", "Exist the Task as same name.");
            }
        } else if (strSelTreeType.equals("Task")) {
            
        }
        
        try {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (!getCategoryDAO().findExistCategory(Global.getUserId(), getRegName())) {
                Category category = new Category();
                category.setCategoryId(getCategoryDAO().getMaxCategoryId()+1);
                category.setCategoryName(getRegName());
                category.setUserId(Global.getUserId());

                getCategoryDAO().save(category);
                
                loggedIn = true;
                msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Successfully Register the Category '" +getRegName()+ "'");
            } else {
                loggedIn = false;
                msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Register Error", "Exist the Category as same name.");
            }
        }
        
        init();
        
        context.addCallbackParam("loggedIn", loggedIn);
    }
    
    /**
     * @description: Get action to Save Dialog 
     */
    public String submitConfirm() {
        String strSubmit = "";
        
        
        return strSubmit;
    }
    
    public void regNewCategory(ActionEvent event) {
        System.out.print("Button Click");
        try {
            strSelTreeType = Global.strTreeType.get(Global.strTreeType.size()-1);
        } catch (Exception ex) {
            
        }
        
        if (strSelTreeType.equals("category")) {
            dlgHeader = "Category New Register";
            lblRegName = "New Category Name: *";
        } else if (strSelTreeType.equals("project")) {
            dlgHeader = "Project New Register";
            lblRegName = " New Project Name: *";
        } else if (strSelTreeType.equals("task")) {
            dlgHeader = "Task New Register";
            lblRegName = " New  Task  Name : *";
        }
//        showDlg();
    }
    
    /*
     * @discription Show Dialog from "New Category"
     */
    public String showDlg() {
        System.out.print("strSelTreeType =" + strSelTreeType);
        String strShowDlg = "";
        try {
            if (strSelTreeType.equals("")) {
                System.out.print("SelType=" + strSelTreeType);
                FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Invalid Error", "Please select a Category");  

                FacesContext.getCurrentInstance().addMessage(null, msg);

                strShowDlg = "dlg.hide()";
            } else
                strShowDlg = "dlg.show()";   
        } catch (Exception ex) {
            
        }
        System.out.print("show = " + strShowDlg);
        return strShowDlg;
    }
            
    public void onNodeExpand(NodeExpandEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Expanded", event.getTreeNode().toString());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
  
    public void onNodeCollapse(NodeCollapseEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
  
    public void onNodeSelect(NodeSelectEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
        
        Global.strTreeType.add(event.getTreeNode().getType().toString());
        Global.strTreeName.add(event.getTreeNode().toString());
        
        setStrSelectedTreeType(event.getTreeNode().getType().toString());        
        setStrSelectedTreeName(event.getTreeNode().toString());
        
        setSelectedNode(event.getTreeNode());
        
        System.out.print(strSelectedTreeType);
        
        FacesContext.getCurrentInstance().addMessage(null, message);
        
//        if (message.getDetail().equals("New Category")) {
//            System.out.print("OK"); 
//        }
    }  
  
    public void onNodeUnselect(NodeUnselectEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }
    
    public TreeNode getRoot() {  
        return root;  
    }
    
    public String getLblRegName() {
        return lblRegName;
    }
    
    public void setLblRegName(String strLbl) {
        this.lblRegName = strLbl;
    }
    
    public String getRegName() {
        System.out.print("regName="+regName);
        return regName;
    }
    
    public void setRegName(String str) {
        this.regName = str;
    }
    
    public String getDlgHeader() {
        return dlgHeader;
    }
    
    public void setDlgHeader(String str) {
        this.dlgHeader = str;
    }
    
    public TreeNode getSelectedNode() {  
        return selectedNode;  
    }  
  
    public void setSelectedNode(TreeNode selectedNode) {  
        this.selectedNode = selectedNode;  
    }
    
    public String getStrSelectedTreeType() {
        return strSelectedTreeType;
    }
    
    public void setStrSelectedTreeType(String str) {
        this.strSelectedTreeType = str;
    }
    
    public String getStrSelectedTreeName() {
        return strSelectedTreeName;
    }
    
    public void setStrSelectedTreeName(String str) {
        this.strSelectedTreeName = str;
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
