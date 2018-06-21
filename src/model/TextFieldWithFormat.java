/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.regex.Pattern;
import javafx.scene.control.TextField;

/**
 *
 * @author tmartinez
 */
public class TextFieldWithFormat {

    private String id;

    private TextField textfield = new TextField();

    private final String pattern;

    boolean needToCheck = false;

    /**
     *
     * @param pattern
     * @param b
     */
    public TextFieldWithFormat(String pattern, boolean b) {
        this.pattern = pattern;
        this.needToCheck = b;
    }

    /**
     *
     * @param pattern
     * @param b
     * @param id
     */
    public TextFieldWithFormat(String pattern, boolean b, String id) {
        this.pattern = pattern;
        this.needToCheck = b;
        this.id = id;
    }

    /**
     *
     * @return
     */
    public TextField getTextField() {
        return this.textfield;
    }

    /**
     *
     * @param test
     */
    public void setText(String test) {
        this.textfield.setText(test);
    }

    /**
     *
     * @param textField
     */
    public void setTextField(TextField textField) {
        this.textfield = textField;
    }

    /**
     *
     * @return
     */
    public String getText() {
        return this.textfield.getText();
    }

    /**
     *
     * @return
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     *
     * @return
     */
    public Boolean isFormat() {
        if (!needToCheck) {
            return true;
        } else {
            return Pattern.matches(pattern, this.textfield.getText());
        }
    }

    /**
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     *
     * @param Id
     */
    public void setId(String Id){
        this.id=Id;
    }
}
