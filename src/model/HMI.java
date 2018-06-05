/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author tmartinez
 */
public class HMI {

    private ArrayList<Classe> listCarthographie = new ArrayList<>();

    private final String nameHMI;
    
    private final Path pathHMI;

    public HMI(String name,Path path) {
        this.nameHMI = name;
        this.pathHMI = path;
    }

    public void addClasse(Classe classe) {
        this.listCarthographie.add(classe);
    }

    public void setAllClasse(ArrayList<Classe> classes) {
        this.listCarthographie = classes;
    }
    
    public String getName(){
        return this.nameHMI;
    }

    public ArrayList<Classe> getClasses() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this.listCarthographie;
    }
    
    public Path getPathHMI(){
        return this.pathHMI;
    }
}
