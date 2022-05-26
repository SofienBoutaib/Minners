/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */
package com.mycompany.minner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import visualization.BarChartDisp;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.BasicConfigurator;
import com.jayway.jsonpath.JsonPath;
import java.awt.EventQueue;
import java.net.MalformedURLException;
import javax.swing.SwingUtilities;
import org.json.JSONException;
import org.json.JSONObject;
import visualization.WordsCloud;

/**
 *
 * @author asus
 */
public class Minner {

    public static ArrayList<String> NameWords = new ArrayList<>();
    public static ArrayList<Integer> Occurence = new ArrayList<>();

    public static void main(String[] args) throws MalformedURLException, IOException {

        try {
            BasicConfigurator.configure();
            String Name = "SBSE";
            String Tags = "6.4.0";
            TempFile D = new TempFile();
            String url1 = "https://api.github.com/search/repositories?q=" + Name + "&sort=stars&order=desc"; // This URL contains the subject to search as well as the way the results could be sorted based on the number of stars
            URL url = new URL(url1);
            String a = Minner.search(url1).toString(); // This line call the search method that contains the different parameters for connecting to the github and perform the search.
            
            
            // The lines bellow are devoted to extract the desired information that are included in a JSon file from Github
            List<String> authors = JsonPath.read(a, "$.items[*].full_name"); 
            List<String> Languages = JsonPath.read(a, "$.items[*].language");
            // Keeping only projects that are developed in Java or Python Languages.
            List<String> remaining_auth = Minner.Remove_Rep(authors, Languages);

            // Brows all the resulted projects one by one that are saved in a list.
            for (int z = 0; z < remaining_auth.size(); z++) {
                // Extracting the contents of each project like the name of the classes, the download URL, ....
                String url2 = "https://api.github.com/repos/" + remaining_auth.get(z) + "/contents";

                url = new URL(url2);

                a = Minner.search(url2).toString();

                // Stroing the extracting information within lists 
                List<String> latest_tags = JsonPath.read(a, "$..url");
                List<String> File_URL = JsonPath.read(a, "$..download_url");
                List<String> type = JsonPath.read(a, "$..type");
                List<String> names = JsonPath.read(a, "$..name");
                
   //---------------------------------------------------------------------------------------------------------------------------             
                // Savinf the name of the java and/or Python files in lists as well as their download URL in the aim to 
                // extract later the source code existing in the files.
                int i = 0;
                while (i < latest_tags.size()) {
                    System.out.println(latest_tags.get(i));
                    if (type.get(i).equals("dir")) {
                        url2 = latest_tags.get(i);
                        a = Minner.search(url2).toString();
                        System.out.println(latest_tags.get(i));
                        latest_tags.addAll(JsonPath.read(a, "$..url"));
                        type.addAll(JsonPath.read(a, "$..type"));
                        File_URL.addAll(JsonPath.read(a, "$..download_url"));
                        names.addAll(JsonPath.read(a, "$..name"));
                        i++;
                    } else {
                        i++;
                    }
                }
                ArrayList<File> FileArray = new ArrayList<>();
                File F;

                for (int j = 1; j < type.size(); j++) {

                    if (type.get(j).equals("file") && (names.get(j) != null && (names.get(j).endsWith(".py") || names.get(j).endsWith(".java")))) {
                        F = new File(latest_tags.get(j), names.get(j), type.get(j), File_URL.get(j));
                        FileArray.add(F);
                    }
                }
//---------------------------------------------------------------------------------------------------------------------------------------------
               // Parse all source code files existing in the array that pertain to a software 
                for (int j = 0; j < FileArray.size(); j++) {

                    String Path = D.FileCreation(Parser.getTextFromGithub(FileArray.get(j).Download_url));
                    Extracter.extractCodeFile(Path, NameWords, Occurence);
                }
//----------------------------------------------------------------------------------------------------------------------------------------------
               // Visualization of the obtained methods as well as function names based on their occurences in the software classes

                //      EventQueue.invokeLater(() -> {
                //var ex = new visualization.BarChartDisp(NameWords, Occurence);
                // ex.setVisible(true);
                // });
                
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        new WordsCloud().initUI(NameWords, Occurence);
                    }
                });

            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    // The search function includes the parameters for connecting to the github
    public static String search(String url) throws IOException, JSONException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setUseCaches(false);

        con.addRequestProperty("Authorization", "Bearer" + "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");
        con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        con.setReadTimeout(30000);//this is in milliseconds
        con.setConnectTimeout(30000);//this is in milliseconds
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        // con.disconnect();
        return response.toString();
    }

    // This function is devoted to remove the repositories (authors and programming languages) from the List. Keeping only authors that have Java or Python projects
    public static List<String> Remove_Rep(List<String> AuthorsList, List<String> Languages) {
        List<String> Adequate_auth = new ArrayList<>();
        int i = 0;
        while (i < AuthorsList.size()) {
            System.out.println(Languages.get(i));

            if (Languages.get(i) != null) {
                if (Languages.get(i).toLowerCase().equals("java") || Languages.get(i).toLowerCase().equals("python")) {
                    Adequate_auth.add(AuthorsList.get(i));
                }
            }
            i++;

        }

        System.err.println("fin");

        return Adequate_auth;
    }

}
