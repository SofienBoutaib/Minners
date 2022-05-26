/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minner;

import java.util.ArrayList;

/**
 *
 * @author asus
 */
public class File {


    String name;
    String URL;
    String type;
    String Download_url;
    ArrayList<File> Set_Files = new ArrayList<>();

    public File() {
    }

    public File(String name, String URL, String type, String Download_url) {
        this.name = name;
        this.URL = URL;
        this.type = type;
        this.Download_url = Download_url;
    }

    public String getDownload_url() {
        return Download_url;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getURL() {
        return URL;
    }

    public void setDownload_url(String Download_url) {
        this.Download_url = Download_url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    
  public File Fill_Array (String URL, String Name, String type, String download_Url){
      
      File F = new File(Name, URL, type, download_Url);   
     
      
      return F;
  }
  
  
  public String getFileName(String fileName){
      
       String extension = null;
      int i = fileName.lastIndexOf('.');
      if (i >= 0) {
      extension = fileName.substring(i+1);
      }
      
      return extension;
  }
    
    
}

