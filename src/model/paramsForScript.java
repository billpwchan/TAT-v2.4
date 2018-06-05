/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import DB.Script;

/**
 *
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

    public paramsForScript() {
    }

    public paramsForScript(int order) {
        this.order = order;
    }

    public void locationOfVariable(String location) {
        //System.out.println(location);
    }

    public void setConfigured(boolean configured) {
        this.hasBeenConfigured = configured;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setPathToVariable(String id) {
        this.pathToVariable = id;
    }

    public String getPathToVariable() {
        return this.pathToVariable;
    }

    public boolean getConfigured() {
        return this.hasBeenConfigured;
    }

    public void setScript(Script script) {
        this.personalScript = script;
    }

    public Script getScript() {
        return this.personalScript;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return this.order;
    }
}
