/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class defining the object used to display the tree table test step and script
 *
 * @author Martinez Thibault
 */
public class testCaseTreeTableViewObject {

    /**
     * String to define the check of test step or script.
     */
    private SimpleStringProperty check;

    /**
     * String to define the stimuli of test step or script.
     */
    private SimpleStringProperty stimuli;

    /**
     * Boolean to define if this object is a test step (true) or a
     * script(false).
     */
    private boolean isTestStep;

    /**
     * Integer to implement the script check id if this object is a script.
     */
    private int idCheckScript;
    /**
     * Integer to implement the script stimuli id if this object is a script.
     */
    private int idStimuliScript;

    /**
     * Boolean to define if this object has been configured for test step.
     */
    private boolean hasBeenConfiguredStep;

    /**
     * Boolean to define if this object has been configured for stimuli of
     * script.
     */
    private boolean hasBeenConfiguredStimuli;

    /**
     * Boolean to define if this object has been configured for check of script.
     */
    private boolean hasBeenConfiguredCheck;

    /**
     * Constructor of this class has been configured false by default, if this
     * object is a step, isTestStep true, else false.
     *
     * @param isTestStep
     */
    public testCaseTreeTableViewObject(boolean isTestStep) {

        this.hasBeenConfiguredStep = false;
        this.hasBeenConfiguredStimuli = false;
        this.hasBeenConfiguredCheck = false;
        this.isTestStep = isTestStep;
        this.stimuli = new SimpleStringProperty();
        this.check = new SimpleStringProperty();

    }

    /**
     *
     * @return
     */
    public SimpleStringProperty checkProperty() {
        if (check == null) {
            check = new SimpleStringProperty(this, "check");
        }
        return check;
    }

    /**
     *
     * @return
     */
    public SimpleStringProperty stimuliProperty() {
        if (stimuli == null) {
            stimuli = new SimpleStringProperty(this, "stimuli");
        }
        return stimuli;
    }

//     /**
//      * 
//      * @param stimuli
//      * @param check
//      * @param isBlocking
//      * @param isTestStep 
//      */
//    public testCaseTreeTableViewObject(String stimuli, String check, boolean isBlocking, boolean isTestStep) {
//
//        this.hasBeenConfiguredStep = false;
//        this.stimuli = new SimpleStringProperty(stimuli);
//        this.check = new SimpleStringProperty(check);
//        // this.isBolcking  = isBlocking;
//        this.isTestStep = isTestStep;
//    }
    /**
     * set the script check id of this object
     *
     * @param id id to be set with this object
     */
    public void setIDCheckScript(int id) {
        this.idCheckScript = id;
    }

    /**
     * set the script stimuli id of this object
     *
     * @param id id to be set with this object
     */
    public void setIDStimuliScript(int id) {
        this.idStimuliScript = id;
    }

    /**
     * Set the string check of this object
     *
     * @param check string of check to set to this object
     */
    public void setCheck(String check) {
        this.check.set(check);
    }

    /**
     * Set the string stimuli of this object
     *
     * @param stimuli string of stimuli to set to this object
     */
    public void setStimuli(String stimuli) {
        //System.out.println("Stimuli externe :" + stimuli);
       // System.out.println("Stimuli interne :" + this.stimuli);
        this.stimuli.set(stimuli);
    }

    /**
     * Set the boolean for test step configured, to know if the test step of
     * this object has been configured.
     *
     * @param configured boolean for test step
     */
    public void setHasBeenConfiguredStep(boolean configured) {
        this.hasBeenConfiguredStep = configured;
    }

    /**
     * Set the boolean for script check configured, to know if the script check
     * of this object has been configured.
     *
     * @param configured boolean for test step
     */
    public void setHasBeenConfiguredCheck(boolean configured) {
        this.hasBeenConfiguredCheck = configured;
    }

    /**
     * Set the boolean for script stimuli configured, to know if the script
     * stimuli of this object has been configured.
     *
     * @param configured boolean for test step
     */
    public void setHasBeenConfiguredStimuli(boolean configured) {
        this.hasBeenConfiguredStimuli = configured;
    }

    /**
     * Return the type of this object
     *
     * @return the type of this object (if true test step, else script)
     */
    public boolean getType() {
        return this.isTestStep;
    }

    /**
     * return the check string of this object
     *
     * @return
     */
    public String getCheck() {
        return this.check.get();
    }

    /**
     * return the string stimuli of this object
     *
     * @return
     */
    public String getStimuli() {
        return this.stimuli.get();
    }

    /**
     * return the script check id of this object
     *
     * @return
     */
    public int getIDCheckScript() {
        return this.idCheckScript;
    }

    /**
     * return the script stimuli id of this object.
     *
     * @return
     */
    public int getIDStimuliScript() {
        return this.idStimuliScript;
    }

    /**
     * return is the test step has been configured
     *
     * @return
     */
    public boolean getHasBeenConfiguredStep() {
        return this.hasBeenConfiguredStep;
    }

    /**
     * return if the script check has been configured
     *
     * @return
     */
    public boolean getHasBeenConfiguredCheck() {
        return this.hasBeenConfiguredCheck;
    }

    /**
     * return if the script stimuli has been configured.
     *
     * @return
     */
    public boolean getHasBeenConfiguredStimuli() {
        return this.hasBeenConfiguredStimuli;
    }
}
