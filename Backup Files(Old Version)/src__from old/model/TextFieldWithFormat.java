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

    public TextFieldWithFormat(String pattern, boolean b) {
        this.pattern = pattern;
        this.needToCheck = b;
    }

    public TextFieldWithFormat(String pattern, boolean b, String id) {
        this.pattern = pattern;
        this.needToCheck = b;
        this.id = id;
    }

    public TextField getTextField() {
        return this.textfield;
    }

    public void setText(String test) {
        this.textfield.setText(test);
    }

    public void setTextField(TextField textField) {
        this.textfield = textField;
    }

    public String getText() {
        return this.textfield.getText();
    }

    public String getPattern() {
        return this.pattern;
    }

    public Boolean isFormat() {
        if (!needToCheck) {
            return true;
        } else {
            return Pattern.matches(pattern, this.textfield.getText());
        }
    }

    public String getId() {
        return this.id;
    }

    public void setId(String Id){
        this.id=Id;
    }
}
