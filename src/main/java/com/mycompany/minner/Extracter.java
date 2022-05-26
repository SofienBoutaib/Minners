/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minner;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author asus
 */
public class Extracter {

    static void extractCodeFile(String Path, ArrayList<String> names, ArrayList<Integer> occ) {
        try {
            BufferedReader input = new BufferedReader(
                    new FileReader(Path));
            String line;
            while ((line = input.readLine()) != null) {
                if (line != "") {
                    System.out.println(filterLines(line, names, occ).trim());
                }
            }
            input.close();
        } catch (IOException ex) {
            System.err.println("Error occured");
        }
    }

    public static String removeTillWord(String input, String word) {
        return input.substring(input.indexOf(word));
    }

    
    // Browsing the line of code in each class and extracting the methods names
    static String filterLines(String line, ArrayList<String> names, ArrayList<Integer> occ) {
        String out = "", flag = "";
        String str[];
        boolean verif = true;
        List<String> al = new ArrayList<String>();

        if (line.contains("def ")) {

            line = line.replaceAll("\\(.*$", "");// deleating parameters ()
            line = removeTillWord(line, "def"); // deleating all words before def

            line = line.replaceAll("[\\s\\_]", " "); // deleating all words before def
            line = line.replaceAll("[\\s\\#]", " ");// deleating all words before def
            str = line.split(" "); // splitting the obtained line by the spaces. The line is composed by def and the name of the method
            al = Arrays.asList(str);
            al = al.stream().filter(empty -> (!empty.equals(""))).collect(Collectors.toList()); // removing the empty spaces in the obtained list

            al.remove("def"); // removing the case that contains def to obtain at the end the name of the method
            
            
            // Sme process is repeated for the java class. The program test if the code starts with public or private....
            if ((line.contains("public") || line.contains("private") || line.contains("protected")) && !line.contains("class") && line.contains("{")) {
                line = line.replaceAll("\\(.*$", "");
                line = line.replaceAll("[\\s\\_]", " ");
                line = line.replaceAll("[\\s\\#]", " ");
                str = line.split(" ");
                al = Arrays.asList(str);
                al = al.stream().filter(empty -> (!empty.equals(""))).collect(Collectors.toList());
                verif = true;
                int i = 0;
                while (verif) {
                    if (al.size() != 1) {
                        al.remove(i);
                    } else {
                        verif = false;
                    }
                }

                for (char c : al.get(0).toCharArray()) {
                    if (Character.isUpperCase(c)) {
                        line += " ";
                        line += c;
                    } else {
                        line += c;
                    }
                }

                al.clear();
                al.addAll(Arrays.asList(line.split(", ")));

            }

            
            // 
            for (int i = 0; i < al.size(); i++) {
                if (names.contains(al.get(i))) { // the program test if the word exist or not. If it exists it adds + 1 to the number of appearing.
                    occ.set(names.indexOf(al.get(i)), occ.get(names.indexOf(al.get(i))) + 1);
                } else {
                    names.add(al.get(i)); // if the word does not exist it saves it in the array as a new detected word and initialize the number of occurence at 1.
                    occ.add(1);
                }
            }
        }
        return out;
    }
}
