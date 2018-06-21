/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author tmartinez
 */
public class StateClasse {

    private String name;

    ObservableList<Properties> stateclasse = FXCollections.observableArrayList();
    
    /**
     *
     * @param name
     */
    public StateClasse(String name){
        this.name = name;
    }
    
    /**
     *
     * @param name
     */
    public void setStateName(String name){
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getStateName(){
        return this.name;
    }
    
    /**
     *
     * @param aThis
     */
    public void addProperties(Properties aThis){
        this.stateclasse.add(aThis);
    }
    
    /**
     *
     * @return
     */
    public ObservableList<Properties> getProperties(){
        return this.stateclasse;
    }
    
}
