/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.bean;

/**
 *
 * @author Jacob
 */
public class TaskTree {
    private String name;

    private String size;

    private String type;

    public TaskTree(String name, String size, String type) {
        this.name = name;
        this.size = size;
        this.type = type;
    }

    public TaskTree() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
