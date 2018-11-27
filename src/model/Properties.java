/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author tmartinez
 */
public class Properties implements java.io.Serializable{

    /**
     *
     */
    public enum Type {

        /**
         *
         */
        string, 

        /**
         *
         */
        integer, 

        /**
         *
         */
        color, 

        /**
         *
         */
        image
    }

    private StringProperty  name;

    private Type type;
    private StringProperty  value;

    /**
     *
     * @param Name
     * @param type
     * @param value
     */
    public Properties(String Name, Type type, String value) {
        this.name = new SimpleStringProperty(Name);
        this.type = type;
        this.value = new SimpleStringProperty(value);
    }
    
    /**
     *
     * @return
     */
    public String getName(){
        return this.name.get();
    }
    
    /**
     *
     * @param Name
     */
    public void setName(String Name){
        this.name = new SimpleStringProperty();
    }
    
    /**
     *
     * @param Type
     */
    public void setType(Type Type){
        this.type = Type;
    }
    
    /**
     *
     * @return
     */
    public Type getType(){
        return this.type;
    }
    
    /**
     *
     * @param value
     */
    public void setValue(String value){
        this.value.set(value);
    }
    
    /**
     *
     * @return
     */
    public String getValue(){
        return this.value.get();
    }

}
