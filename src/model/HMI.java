/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author tmartinez
 */
public class HMI {

    private final String nameHMI;
    private final Path pathHMI;
    private ArrayList<Classe> listCarthographie = new ArrayList<>();

    /**
     * @param name
     * @param path
     */
    public HMI(String name, Path path) {
        this.nameHMI = name;
        this.pathHMI = path;
    }

    /**
     * @param classe
     */
    public void addClasse(Classe classe) {
        this.listCarthographie.add(classe);
    }

    /**
     * @param classes
     */
    public void setAllClasse(ArrayList<Classe> classes) {
        this.listCarthographie = classes;
    }

    /**
     * @return
     */
    public String getName() {
        return this.nameHMI;
    }

    /**
     * @return
     */
    public ArrayList<Classe> getClasses() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this.listCarthographie;
    }

    /**
     * @return
     */
    public Path getPathHMI() {
        return this.pathHMI;
    }
}
