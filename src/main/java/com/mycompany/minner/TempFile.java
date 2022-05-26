/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minner;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author asus
 */
public class TempFile {

    public String FileCreation(String Data) throws IOException {
          File  tempFile=null; 
        
        
        BufferedWriter writer = null;
        try {
            
            // Creating a temporary file
            tempFile = File.createTempFile(
                "Code", ".tmp",
                new File("C:/Users/asus/Desktop"));
            writer = new BufferedWriter(
                new FileWriter(tempFile));
            writer.write(Data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null)
                    writer.close();
            }
            catch (Exception ex) {
            }
        
        
        

        return tempFile.getAbsolutePath();
    }
 }   
    
}
