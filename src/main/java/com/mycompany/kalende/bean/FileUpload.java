/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.kalende.bean;

/**
 *
 * @author Jacob
 */
public class FileUpload {
    private String fileName;
    private String contentType;
    private byte[] fileData;

    public FileUpload(String fileName, String contentType, byte[] fileData){
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileData = fileData;
    }

    public String getFileName(){
        return fileName;
    }
    public String getContentType(){
        return contentType;
    }
    public byte[] getFileData(){
        return fileData;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public void getContentType(String contentType){
        this.contentType = contentType;
    }
    public void setFileData(byte[] fileData){
        this.fileData = fileData;
    }
}