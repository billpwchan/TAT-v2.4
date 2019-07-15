/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.Script;

/**
 * @author tmartinez
 */
public class paramsForScript {

    //Constant = 1
    //Variable = 2
    private boolean hasBeenConfigured = false;

    private int scriptID;

    private String value = "";

    private String pathToVariable = "";

    private Script personalScript;

    private int order;

    /**
     *
     */
    public paramsForScript() {
    }

    /**
     * @param order
     */
    public paramsForScript(int order) {
        this.order = order;
    }

    /**
     * @param location
     */
    public void locationOfVariable(String location) {
        //System.out.println(location);
    }

    /**
     * @return
     */
    public String getValue() {
        return this.value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return
     */
    public String getPathToVariable() {
        return this.pathToVariable;
    }

    /**
     * @param id
     */
    public void setPathToVariable(String id) {
        this.pathToVariable = id;
    }

    /**
     * @return
     */
    public boolean getConfigured() {
        return this.hasBeenConfigured;
    }

    /**
     * @param configured
     */
    public void setConfigured(boolean configured) {
        this.hasBeenConfigured = configured;
    }

    /**
     * @return
     */
    public Script getScript() {
        return this.personalScript;
    }

    /**
     * @param script
     */
    public void setScript(Script script) {
        this.personalScript = script;
    }

    /**
     * @return
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * @param order
     */
    public void setOrder(int order) {
        this.order = order;
    }
}
