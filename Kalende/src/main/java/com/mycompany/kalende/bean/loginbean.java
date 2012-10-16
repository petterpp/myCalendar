/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.bean;

import com.mycompany.kalende.dao.UserDAO;
import com.mycompany.kalende.model.User;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * @since 2012.09-17
 * @author Jacob
 */


public class loginbean {
    
    private String username = "";
    private String password = "";
    
    private User user;
    private UserDAO dao;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public String checkValidUser()
    {
        if (dao == null) {
            dao = new UserDAO();
        }

        if (user == null) {
            user = new User();
        }
        
        System.out.print("User Namd is " + getUsername());
        System.out.print("Password is " + getPassword());
        
        user = dao.find(getUsername(), getPassword());
        
        if(user != null)
        {
            System.out.println("aaa");
            return "valid";
        }
        else
        {
            System.out.println("bbb");
            return "invalid";
        }
    }
}

