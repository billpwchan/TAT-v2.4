/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author T0155040
 */
public class Classe implements Serializable {

    private String nameClasse;

    private final ObservableList<equipment> equipmentInClass = FXCollections.observableArrayList();//Set();//checkedObservableSet(new ObservableSet<equipment> se);// new HashSet<>();

    private final ObservableList<StateClasse> state = FXCollections.observableArrayList();

    private final Set<String> eqpName = new HashSet<>();

    /**
     *
     */
    public Classe() {

    }

    /**
     *
     * @param name
     */
    public Classe(String name) {
        this.nameClasse = name;
    }

    /**
     *
     * @param eqp
     */
    public void addEquipment(equipment eqp) {
        if (eqpName.add(eqp.getEquipmentName())) {
            System.out.println("Equipment name : "+eqp.getEquipmentName()+"Nombre position : "+eqp.getPositions().size());
            this.equipmentInClass.add(eqp);
            //Collections.sort(this.equipmentInClass, new AgeComparator());
        }
    }

    /**
     *
     * @param eqp
     */
    public void removeEquipment(equipment eqp){
        if (eqpName.remove(eqp.getEquipmentName())) {
                this.equipmentInClass.remove(eqp);
        }

    }

    /**
     *
     * @param name
     */
    public void setClasseName(String name) {
        this.nameClasse = name;
    }

    /**
     *
     * @return
     */
    public String getClasseName() {
        return this.nameClasse;
    }

    /**
     *
     * @return
     */
    public ObservableList<equipment> getEquipments() {
        return this.equipmentInClass;
    }

    /**
     *
     * @param prop
     */
    public void addState(StateClasse prop) {
        this.state.add(prop);
    }

    /**
     *
     * @return
     */
    public ObservableList<StateClasse> getState() {
        return this.state;
    }
}

class AgeComparator implements Comparator<equipment> {
    @Override
    public int compare(equipment a, equipment b) {
        return a.getEquipmentName().compareTo(b.getEquipmentName());
    }
}
