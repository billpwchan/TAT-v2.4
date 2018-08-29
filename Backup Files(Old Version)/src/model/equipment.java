/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.ObservableList;
import org.sikuli.api.ScreenRegion;

/**
 *
 * @author T0155040
 */
public class equipment implements Serializable {

    private String name;

    private final Set<Position> inFolder = new HashSet<>();

    private boolean hasBeenConfigured = false;

    private int mappedNumber=0;

    /**
     *
     */
    public equipment() {
       // hasBeenConfigured = false;
    }

    /**
     *
     * @param name
     */
    public equipment(String name) {
        this.name = name;
        this.hasBeenConfigured = false;
    }

    /**
     *
     * @param name
     * @param sr
     * @param path
     */
    public equipment(String name, ScreenRegion sr, Path path) {
        this.name = name;
        this.inFolder.add(new Position(sr, path));
        hasBeenConfigured=true;
        mappedNumber++;
    }

    /**
     *
     * @param name
     */
    public void setEquipmentName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getEquipmentName() {
        return this.name;
    }

    /**
     *
     * @param sr
     */
    public void setPosition(Position sr) {
        this.inFolder.add(sr);
        this.hasBeenConfigured=true;
        mappedNumber++;
    }

    /**
     *
     * @return
     */
    public Set<Position> getPositions() {
        return this.inFolder;
    }

    /**
     *
     * @return
     */
    public boolean isConfigured() {
        return this.hasBeenConfigured;
    }
    
    /**
     *
     * @param b
     */
    public void setConfigured(boolean b){
        this.hasBeenConfigured = b;
    }
    
    /**
     *
     * @return
     */
    public int getMappedNumber(){
        return this.mappedNumber;
    }

    /**
     *
     * @param eqpPosition
     */
    public void setAllPosition(ObservableList<Position> eqpPosition) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        this.inFolder.clear();
        this.inFolder.addAll(eqpPosition);
        this.mappedNumber=eqpPosition.size();
    }
    
    /**
     *
     * @param pos
     */
    public void removePosition(Position pos){
        this.inFolder.remove(pos);
        this.hasBeenConfigured = false;
        this.mappedNumber--;
    }
}
