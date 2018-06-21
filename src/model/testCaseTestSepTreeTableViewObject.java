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

    /**
     *
     */
    public testCaseTestSepTreeTableViewObject() {
    }

    /**
     *
     * @param isCase
     */
    public testCaseTestSepTreeTableViewObject(boolean isCase) {

        this.hasBeenConfiguredCase = false;
        this.isCase = isCase;
        this.Name = new SimpleStringProperty();
        this.User_ID = new SimpleStringProperty();

    }

    /**
     *
     * @param name
     * @param user_ID
     * @param isCase
     */
    public testCaseTestSepTreeTableViewObject(String name, String user_ID, boolean isCase) {

        this.hasBeenConfiguredCase = false;
        this.Name = new SimpleStringProperty(name);
        this.User_ID = new SimpleStringProperty(user_ID);
        // this.isBolcking  = isBlocking;
        this.isCase = isCase;
    }

    /**
     *
     * @return
     */
    public SimpleStringProperty NameProperty() {
        if (Name == null) {
            Name = new SimpleStringProperty(this, "Name");
        }
        return Name;
    }

    /**
     *
     * @return
     */
    public SimpleStringProperty userIDProperty() {
        if (User_ID == null) {
            User_ID = new SimpleStringProperty(this, "User_ID");
        }
        return User_ID;
    }

    /**
     *
     * @param id
     */
    public void setIDCase(int id) {
        this.idCase = id;
    }

    /**
     *
     * @param id
     */
    public void setIDStep(int id) {
        this.idStep = id;
    }

    /**
     *
     * @param Name
     */
    public void setName(String Name) {
        this.Name.set(Name);
    }

    /**
     *
     * @param user_ID
     */
    public void setUser_ID(String user_ID) {
        this.User_ID.set(user_ID);
    }

    /**
     *
     * @param configured
     */
    public void setHasBeenConfiguredCase(boolean configured) {
        this.hasBeenConfiguredCase = configured;
    }

    /**
     *
     * @return
     */
    public boolean getType() {
        return this.isCase;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.Name.get();
    }

    /**
     *
     * @return
     */
    public int getCaseID() {
        return this.idCase;
    }

    /**
     *
     * @return
     */
    public String getUserID() {
        return this.User_ID.get();
    }

    /**
     *
     * @return
     */
    public boolean getHasBeenConfiguredCase() {
        return this.hasBeenConfiguredCase;
    }
}
