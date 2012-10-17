/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.global;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2012-10-01
 * @author Jacob
 */
public class Global {
    private static int userId = 0;
    private static String userName = "";
    
    public static String strTempText = "";
    public static List<String> strTreeType = new ArrayList<String>();
    public static List<String> strTreeName = new ArrayList<String>();
    
    public static int getUserId() {
        return userId;
    }
    
    public static void setUserId(int id) {
        userId = id;
    }
    
    public static String getUserName() {
        return userName;
    }
    
    public static void setUserName(String name) {
        userName = name;
    }
    
}
