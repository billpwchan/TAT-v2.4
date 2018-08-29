/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package script;

import DB.Parameters;
import DB.ParametersExecution;
import DB.Script;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 *
 * @author Thomas Morin
 */
public class SaveInFile {

    private String string;
    private ArrayList<String> strings;
    private String path;
    private String fileName;

    /**
     *
     * @param parameters
     * @param hashMap
     * @return
     * @throws Exception
     */
    public String run(ArrayList<ParametersExecution> parameters, HashMap hashMap) throws Exception {
        int Arraylist = 0;
        int buffer = 0;
        this.string = parameters.get(1).getValue().trim();
        if (this.string.contains("@&Buffer_")) {
            System.out.println("STRING = " + hashMap.get(this.string).getClass());
            if (hashMap.get(this.string) instanceof String) {
                this.string = (String) hashMap.get(this.string);
            } else {
                System.out.println("THIS IS AN ARRAYLIST");
                this.strings = (ArrayList<String>) hashMap.get(this.string);
                Arraylist = 1;
            }
        }
        this.path = parameters.get(2).getValue().trim();
        if (this.path.contains("@&Buffer_")) {
            this.path = (String) hashMap.get(this.path);
        }

        this.fileName = parameters.get(3).getValue().trim();
        if (this.fileName.contains("@&Buffer_")) {
            this.fileName = (String) hashMap.get(this.fileName);
        }
        File file = new File(this.path + "\\" + this.fileName + ".txt");
        PrintWriter out = null;
        if (file.exists()) {
            System.out.println("EXIST");
            out = new PrintWriter(new FileOutputStream(file, true));
            if (Arraylist == 0) {
                System.out.println("WRITE");
                out.append(this.string + "\n");
            } else {
                if (strings.size() > 0) {
                    for (int i = 0; i < strings.size(); i++) {
                        out.append(this.strings.get(i) + "\n");
                    }
                } else {
                    System.out.println("Empty array");
                }
            }
        } else {
            System.out.println("NOT EXIST");
            out = new PrintWriter(this.path + "\\" + this.fileName + ".txt");
            if (Arraylist == 0) {
                System.out.println("THIS.STRING = " + this.string);
                System.out.println("WRITE");
                out.println(this.string);
            } else {
                if (strings.size() > 0) {
                    for (int i = 0; i < strings.size(); i++) {
                        out.println(this.strings.get(i) + "\n");
                    }
                } else {
                    System.out.println("Empty array");
                }

            }
        }
        out.close();
        return null;
    }

    /**
     *
     */
    public void close() {

    }

}
