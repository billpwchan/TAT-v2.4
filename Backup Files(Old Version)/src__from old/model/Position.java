/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.file.Path;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.sikuli.api.ScreenRegion;

/**
 *
 * @author tmartinez
 */
public class Position {

    private ScreenRegion sc;

    private Path emplacement;

    private BooleanProperty checked = new SimpleBooleanProperty(false);

    ;

    public Position(ScreenRegion sr, Path loc) {
        this.sc = sr;
        this.emplacement = loc;
        //checked.set(false);
    }

    public Path getPath() {
        return this.emplacement;
    }

    public void setPath(Path loc) {
        this.emplacement = loc;
    }

    public ScreenRegion getScreenRegion() {
        return this.sc;
    }

    public void setScreenRegion(ScreenRegion sr) {
        this.sc = sr;
    }

    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    public BooleanProperty checkedProperty() {
        return checked;
    }

    public boolean isChecked() {
        return checked.get();
    }
}
