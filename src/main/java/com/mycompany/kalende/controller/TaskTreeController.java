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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.application.FacesMessage;
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
@ManagedBean(name="taskTree")
@SessionScoped
public class TaskTreeController {
    private TreeNode root;
    
    public String name;        //label name in New Category Dialog
    public String regName;     //text name in New Category Dialog
    public String dlgHeader;   //Header in New Category Dialog
    public String dlg;         //Dialog Show Command
    public String confirmation;    //Confirm Show Command
    
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
        categoryList = getCategoryDAO().findList(Global.getUserId());
        
        TreeNode[] categoryName = new TreeNode[100];
        TreeNode[] projectName = new TreeNode[100];
        TreeNode[] taskName = new TreeNode[100];
        
        root = new DefaultTreeNode("root", null);
        
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
    
    public TreeNode getRoot() {  
        return root;  
    }
    
    public String getName() {
        return name;
    }
    
    public String getRegName() {
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
    
    public String getDlg() {
        return dlg;
    }
    
    public void setDlg(String dlg) {
        this.dlg = dlg;
    }
    
    public String getConfirmation() {
        return confirmation;
    }
    
    public void setConfirmation(String str) {
        this.confirmation = str;
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
    
    /**
     * @discription: Action function from Save Dialog
     * @param event 
     */
    public void submitAction() {
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
    
    public void regNewCategory() {
        System.out.print(Global.strTreeType.size());
        
        String strType = "";
        try {
            strType = Global.strTreeType.get(Global.strTreeType.size()-1);
        } catch (Exception ex) {
            
        }
        
        System.out.print("strType = " + strType);
        
        if (strType.equals("category")) {
            setDlgHeader("Category Register");
        } else if (strType.equals("project")) {
            setDlgHeader("Project Register");
        } else if (strType.equals("task")) {
            setDlgHeader("Task Register");
        } else {
            setDlgHeader("");
        }
        
        showDlg();
    }
    
    /*
     * @discription Show Dialog from "New Category"
     */
    public String showDlg() {
        String strShowDlg = "";
        
        
//        if (getSelectedNode() != null)
//            getSelectedNode().setSelected(false);
            
//        if (!strSelectedTreeName.equals(""))
        strShowDlg = "dlg.show()";   
        
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
