/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minner;

/**
 *
 * @author asus
 */

    
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Parser {
public static String getTextFromGithub(String link) {
    URL Url = null;
    try {
        Url = new URL(link);
    } catch (MalformedURLException e1) {
        e1.printStackTrace();
    }
    HttpURLConnection Http = null;
    try {
        Http = (HttpURLConnection) Url.openConnection();
    } catch (IOException e1) {
        e1.printStackTrace();
    }
    Map<String, List<String>> Header = Http.getHeaderFields();
    
    for (String header : Header.get(null)) {
        if (header.contains(" 302 ") || header.contains(" 301 ")) {
            link = Header.get("Location").get(0);
            try {
                Url = new URL(link);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                Http = (HttpURLConnection) Url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Header = Http.getHeaderFields();
        }
    }
    InputStream Stream = null;
    try {
        Stream = Http.getInputStream();
    } catch (IOException e) {
        e.printStackTrace();
    }
    String Response = null;
    try {
        Response = GetStringFromStream(Stream);
    } catch (IOException e) {
        e.printStackTrace();
    }
    return Response;
}

private static String GetStringFromStream(InputStream Stream) throws IOException {
    if (Stream != null) {
        Writer Writer = new StringWriter();

        char[] Buffer = new char[2048];
        try {
            Reader Reader = new BufferedReader(new InputStreamReader(Stream, "UTF-8"));
            int counter;
            while ((counter = Reader.read(Buffer)) != -1) {
                Writer.write(Buffer, 0, counter);
            }
        } finally {
            Stream.close();
        }
        return Writer.toString();
    } else {
        return "No Contents";
    }
} 
}
