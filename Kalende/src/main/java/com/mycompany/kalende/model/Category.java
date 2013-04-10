/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.model;

/**
 *
 * @author Administrator
 */
public class Category {
    
    private int categoryId;
    private String categoryName;
    
    private int userId;
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int id) {
        this.categoryId = id;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String name) {
        this.categoryName = name;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int id) {
        this.userId = id;
    }
    
}
