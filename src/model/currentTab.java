/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.scene.control.Tab;

/**
 * Class defining the object currentTab
 *
 * @author Martinez Thibault
 */
public class currentTab {

    /**
     * Unique ID of a database object( Test campaign, test case, test step ...)
     */
    private int ID;

    /**
     * Instance of the tab who display the object (test campaign, test case)
     * linked to the ID.
     */
    private Tab tab;

    /**
     * COnstructor of currentTab
     *
     * @param objectID Unique id of the object.
     * @param tab      reference to the tab.
     */
    public currentTab(int objectID, Tab tab) {
        this.tab = tab;
        this.ID = objectID;
    }

    /**
     * return the id.
     *
     * @return ID id stored in this object.
     */
    public int getID() {
        return this.ID;
    }

    /**
     * return the tab instance.
     *
     * @return Tab, tab instance stored in currentTab.
     */
    public Tab getTab() {
        return this.tab;
    }
}
