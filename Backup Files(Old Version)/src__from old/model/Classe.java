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

    public Classe() {

    }

    public Classe(String name) {
        this.nameClasse = name;
    }

    public void addEquipment(equipment eqp) {
        if (eqpName.add(eqp.getEquipmentName())) {
            System.out.println("Equipment name : "+eqp.getEquipmentName()+"Nombre position : "+eqp.getPositions().size());
            this.equipmentInClass.add(eqp);
            //Collections.sort(this.equipmentInClass, new AgeComparator());
        }
    }

    public void removeEquipment(equipment eqp){
        if (eqpName.remove(eqp.getEquipmentName())) {
                this.equipmentInClass.remove(eqp);
        }

    }

    public void setClasseName(String name) {
        this.nameClasse = name;
    }

    public String getClasseName() {
        return this.nameClasse;
    }

    public ObservableList<equipment> getEquipments() {
        return this.equipmentInClass;
    }

    public void addState(StateClasse prop) {
        this.state.add(prop);
    }

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
