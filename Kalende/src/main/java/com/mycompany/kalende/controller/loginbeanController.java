/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.controller;

import com.mycompany.kalende.bean.loginbean;
import com.mycompany.kalende.dao.UserDAO;
import com.mycompany.kalende.global.Global;
import com.mycompany.kalende.model.User;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Jacob
 */
@ManagedBean(name="loginbean")
@RequestScoped
public class loginbeanController {
 
    private loginbean loginbean;
    private User user;
    private UserDAO userDAO;
    public String username;
    public String password;

    public loginbeanController() {
        super();
    }

    public loginbean getLoginbean() {
        return loginbean;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public UserDAO getUserDAO() {

        if (userDAO == null) {
            userDAO = new UserDAO();
        }

        return userDAO;
    }

    public void setLoginbean(loginbean loginbean) {
        this.loginbean = loginbean;
    }

    public String checkValidUser() {
        Boolean bool = false;
        bool = getUserDAO().checkUser(getUsername(), getPassword());
        
        User loginUser = getUserDAO().find(getUsername(), getPassword());

        if(bool) {
            Global.setUserName(loginUser.getUsername());
            Global.setUserId(loginUser.getId());
            Global.strTreeName.clear();
            Global.strTreeType.clear();
            
            return "valid";
        } else {
            return "invalid";
        }
    }
        
}