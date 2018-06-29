/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author tmartinez
 */
public class testCaseTestSepTreeTableViewObject {

    private SimpleStringProperty Name;

    private SimpleStringProperty User_ID;

    //private boolean isBolcking;
    private boolean isCase;

    private int idCase;

    private int idStep;

    private boolean hasBeenConfiguredCase;

    public testCaseTestSepTreeTableViewObject() {
    }

    public testCaseTestSepTreeTableViewObject(boolean isCase) {

        this.hasBeenConfiguredCase = false;
        this.isCase = isCase;
        this.Name = new SimpleStringProperty();
        this.User_ID = new SimpleStringProperty();

    }

    public testCaseTestSepTreeTableViewObject(String name, String user_ID, boolean isCase) {

        this.hasBeenConfiguredCase = false;
        this.Name = new SimpleStringProperty(name);
        this.User_ID = new SimpleStringProperty(user_ID);
        // this.isBolcking  = isBlocking;
        this.isCase = isCase;
    }

    public SimpleStringProperty NameProperty() {
        if (Name == null) {
            Name = new SimpleStringProperty(this, "Name");
        }
        return Name;
    }

    public SimpleStringProperty userIDProperty() {
        if (User_ID == null) {
            User_ID = new SimpleStringProperty(this, "User_ID");
        }
        return User_ID;
    }

    public void setIDCase(int id) {
        this.idCase = id;
    }

    public void setIDStep(int id) {
        this.idStep = id;
    }

    public void setName(String Name) {
        this.Name.set(Name);
    }

    public void setUser_ID(String user_ID) {
        this.User_ID.set(user_ID);
    }

    public void setHasBeenConfiguredCase(boolean configured) {
        this.hasBeenConfiguredCase = configured;
    }

    public boolean getType() {
        return this.isCase;
    }

    public String getName() {
        return this.Name.get();
    }

    public int getCaseID() {
        return this.idCase;
    }

    public String getUserID() {
        return this.User_ID.get();
    }

    public boolean getHasBeenConfiguredCase() {
        return this.hasBeenConfiguredCase;
    }
}
