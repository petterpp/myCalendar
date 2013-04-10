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
import com.mycompany.kalende.dao.ScheduleDAO;
import com.mycompany.kalende.global.Global;
import com.mycompany.kalende.model.Category;
import com.mycompany.kalende.model.Project;
import com.mycompany.kalende.model.Schedule;
import com.mycompany.kalende.model.Task;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.event.ValueChangeListener;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.OptionPaneUI;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
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
@SessionScoped

public class TaskTreeController {
    private TreeNode root;
    
    private String lblRegName;       //label lblRegName in New Category Dialog
    private String regName;          //New Name text in New Category Dialog
    public String dlgHeader;        //Header in New Category Dialog
    public String strSelTreeType = "";      //Now selected tree's node name
    public String startDate;
    public String endDate;
    
    private String strSelectedTreeType = "";     //Selected Type Controll Name in Tree
    private String strSelectedTreeName = "";     //Selected Leaf or Node Name in Tree
    
    public int iCategoryId;     //Check Category Id in New Register Action
    public int iProjectId;      //Check Project Id in New Register Action
    public int iTaskId;         //Check Task Id in New Tegister Action
    
    private UserDAO userDAO;
    private CategoryDAO categoryDAO;
    private ProjectDAO projectDAO;
    private TaskDAO taskDAO;
    private ScheduleDAO scheduleDAO;

    public List<Category> categoryList = new ArrayList<Category>();
    public List<Project> projectList = new ArrayList<Project>();
    public List<Task> taskList = new ArrayList<Task>();
    public List<Schedule> scheduleList = new ArrayList<Schedule>();
    
    private TreeNode selectedNode; 
    private ValueChangeEvent valueChangeEvent;
    
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
            categoryName[i].setExpanded(true);
            projectList = getProjectDAO().findList(categoryList.get(i).getCategoryId());
            
            for (int j = 0; j < projectList.size(); j ++) {
                if (projectList.get(j).getProjectName().equals("")) {
                    taskList = getTaskDAO().findList(projectList.get(j).getProjectId());
                
                    for (int k = 0; k < taskList.size(); k ++) {
                        taskName[k] = new DefaultTreeNode("task", taskList.get(k).getTaskName(), categoryName[i]);
                    }
                } else {
                    projectName[j] = new DefaultTreeNode("project", projectList.get(j).getProjectName(), categoryName[i]);
                    projectName[j].setExpanded(true);
                    
                    taskList = getTaskDAO().findList(projectList.get(j).getProjectId());
                
                    for (int k = 0; k < taskList.size(); k ++) {
                        taskName[k] = new DefaultTreeNode("task", taskList.get(k).getTaskName(), projectName[j]);
                    }
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
        } else if (strSelTreeType.equals("task")) {
            loggedIn = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Register Error", "Don't register in Task.");
        }
        
        try {
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception ex) {
            if (strSelTreeType.equals("")) {
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
        System.out.print(event.getTreeNode().toString());
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
  
    public void onNodeCollapse(NodeCollapseEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Collapsed", event.getTreeNode().toString());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }  
  
    public String[] onNodeSelect(NodeSelectEvent event) throws IOException {  
        System.out.print("This is test : " + startDate);
        String[] selectInfo = new String[10];
        
        boolean loggedIn = false;
        
        RequestContext context = RequestContext.getCurrentInstance();
        
        try {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Selected", event.getTreeNode().toString());
            System.out.print(event.getTreeNode());

            Global.strTreeType.add(event.getTreeNode().getType().toString());
            Global.strTreeName.add(event.getTreeNode().toString());

            setStrSelectedTreeType(event.getTreeNode().getType().toString());        
            setStrSelectedTreeName(event.getTreeNode().toString());

            setSelectedNode(event.getTreeNode());

            selectInfo[0] = Global.strTreeType.toString();
            selectInfo[1] = Global.strTreeName.toString();

            FacesContext.getCurrentInstance().addMessage(null, message);
            
            if (Global.getChooseEvent()) {
                Global.setChooseEvent(true);
                
                try {
                    strSelTreeType = Global.strTreeType.get(Global.strTreeType.size()-1);
                } catch (Exception ex) {

                }
                
                if (strSelTreeType.equals("task")) {
                    Task task = getTaskDAO().findByName(Global.strTreeName.get(Global.strTreeName.size()-1));
                    
                    int iUsedAccount = 0;
                    int iNum = 0;
                    
                    try {
                        iUsedAccount = getScheduleDAO().findUsedAccount(getUserDAO().find(Global.getUserId()), task);
//                        iNum = getScheduleDAO().findByIdNum();
                    } catch (Exception e) {
                        
                    }
                    iUsedAccount++;
                    
                    Schedule schedule = new Schedule();
//                    schedule.setScheduleId(iNum);
                    schedule.setScheduleStartDate(Global.STARTDATE);
                    schedule.setScheduleEndDate(Global.ENDDATE);
                    schedule.setScheduleTitle(Global.strTreeName.get(Global.strTreeName.size()-1));
                    schedule.setTask(task);
                    schedule.setUsedAccount(iUsedAccount);
                    schedule.setUser(getUserDAO().find(Global.getUserId()));
                    
//                    getScheduleDAO().save(schedule);
                    
                    loggedIn = true;
                    
                    //Write Data from mongodb data in json file                    
                    writeJsonFile();
                    
                    context.addCallbackParam("loggedIn", loggedIn);
                }
            }
        } catch (ClassCastException ex) {
            
        }
        
        return selectInfo;
    }
    
    public void writeJsonFile() throws IOException {
        
        scheduleList = getScheduleDAO().find(getUserDAO().find(Global.getUserId()));
        String strJson = "";
        
        int DEFAULT_BUFFER_SIZE = 1024; // 10KB
        
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource("..\\..\\pages\\events.json");
        File file = null;
        
//        FileInputStream fileInput = null;
//        InputStreamReader isr = null;
//        BufferedReader br = null;
//        
//        StringBuffer buffer = new StringBuffer();
        
//        try {
//            file = new File(url.toURI());
//            
//            
//            fileInput = new FileInputStream(file);
//            isr = new InputStreamReader(fileInput, "SHIFT_JIS");
//            br = new BufferedReader(isr);
//            
//            String line = "";
//            
//            while ((line = br.readLine()) != null) {
//                    buffer.append(line);
//                    buffer.append("\n");
//            }
//            
//            strJson = buffer.toString();
//            System.out.println(strJson);
//            strJson = "aaa";
            
        // Prepare.
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

        BufferedInputStream input = null;
        
        BufferedOutputStream output = null;

        try {
            // Open file.
            file = new File(url.toURI());
            input = new BufferedInputStream(new FileInputStream(file));
            InputStream fis = new FileInputStream(file);
            
            DataInputStream dis = new DataInputStream(input);
 
            while (dis.available() != 0) {
                    System.out.println(dis.readLine());
            }
            
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            
            int offset = 0;
            int numRead = 0;
            while ((offset < buffer.length) && ((numRead = fis.read(buffer, offset, buffer.length - offset)) >= 0)) {

                offset += numRead;

            }
            fis.close();
            
            // Init servlet response.
            response.reset();
            response.setHeader("Content-Type", "application/json");
            response.setHeader("Content-Length", String.valueOf(file.length()));
            response.setHeader("Content-Disposition", "attachment; filename='temp.json'");
            response.getOutputStream().write(buffer);
            
            response.getOutputStream().flush();
            response.getOutputStream().close();
            FacesContext.getCurrentInstance().responseComplete();
//            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

            // Write file contents to response.
                        
//            int length;
//            while ((length = input.read(buffer)) > 0) {
//                
//                output.write(buffer, 0, length);
//            }
            

            // Finalize task.
//            output.flush();
        } catch (URISyntaxException ex1) {
            
        } finally {
            // Gently close streams.
//            output.close();
//            input.close();
        }

        // Inform JSF that it doesn't need to handle response.
        // This is very important, otherwise you will get the following exception in the logs:
        // java.lang.IllegalStateException: Cannot forward after response has been committed.
        facesContext.responseComplete();
        System.out.print("The End");
        
//        String engine = getFileContent(abspath);
        
    }
    
    /**
        * Name: getFileContent
        * Read the File
        * @param strFilePath
        * @return String
        */
    public String getFileContent(String strFilePath) {
        Properties property = null;
        String str = "";
        File newFile = new File(strFilePath);
        if (newFile.exists()) {
            try {
                System.out.print("File is exist!!!");
                    FileInputStream fileInput;
                    fileInput = new FileInputStream(newFile);
                    InputStreamReader isr = new InputStreamReader(fileInput,
                                    "SHIFT_JIS");
                    BufferedReader in = new BufferedReader(isr);
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = in.readLine()) != null) {
                            buffer.append(line);
                            buffer.append("\n");
                    }
                    str = buffer.toString();

//				BufferedInputStream bis = new BufferedInputStream(fileInput);
//				byte[] contents = new byte[99999];
//                
//                int bytesRead=0;
//               
//                while( (bytesRead = bis.read(contents)) != -1){
//                	str = new String(contents, 0, bytesRead);
//                }
//                bis.close();
            } catch (IOException e) {
                    return null;
            }
        }
        return str;
    }
  
    public void onNodeUnselect(NodeUnselectEvent event) {  
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unselected", event.getTreeNode().toString());  
  
        FacesContext.getCurrentInstance().addMessage(null, message);  
    }
    
    public void setSelectTreeNode() {
        System.out.print("StartDate::::" + startDate);
    }
    
    public void onStartDateChange(ValueChangeEvent e) throws ParseException{
        startDate = e.getNewValue().toString();        
        System.out.print("startDate------------"+this.startDate);
        
        String[] strDates = startDate.split("@");
        String strStartInfo = strDates[0].substring(4, 24).toString();
        String strEndInfo = strDates[1].substring(4, 24).toString();
        
        System.out.print(strStartInfo);
        System.out.print(strEndInfo);
//        Calendar startdate = new GregorianCalendar();
//        Calendar enddate = new GregorianCalendar();
        
        try {
            SimpleDateFormat sdfSource = new SimpleDateFormat("MMM dd yyyy H:mm:ss", new Locale("es", "ES"));
            
            Date m_StartDate = sdfSource.parse(strStartInfo);
            Date m_EndDate = sdfSource.parse(strEndInfo);
            
            SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            
            String cacheStart = sdfDestination.format(m_StartDate);
            String cacheEnd = sdfDestination.format(m_EndDate);
            
            Global.STARTDATE = sdfDestination.parse(cacheStart);
            Global.ENDDATE = sdfDestination.parse(cacheEnd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
//        Global.STARTDATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES")).parse(strStartInfo);
//        Global.ENDDATE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("es", "ES")).parse(strEndInfo);
        
        System.out.print(Global.STARTDATE.toString());
        System.out.print(Global.ENDDATE.toString());
        
        Global.setChooseEvent(true);
    }
    
    public String replyWith() {
        return startDate;
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
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate (String str) {
        startDate = str;
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
    
    public ScheduleDAO getScheduleDAO() {
        if (scheduleDAO == null)
            scheduleDAO = new ScheduleDAO();
        
        return scheduleDAO;
    }    
}
