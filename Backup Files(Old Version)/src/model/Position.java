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

    /**
     *
     * @param sr
     * @param loc
     */
    public Position(ScreenRegion sr, Path loc) {
        this.sc = sr;
        this.emplacement = loc;
        //checked.set(false);
    }

    /**
     *
     * @return
     */
    public Path getPath() {
        return this.emplacement;
    }

    /**
     *
     * @param loc
     */
    public void setPath(Path loc) {
        this.emplacement = loc;
    }

    /**
     *
     * @return
     */
    public ScreenRegion getScreenRegion() {
        return this.sc;
    }

    /**
     *
     * @param sr
     */
    public void setScreenRegion(ScreenRegion sr) {
        this.sc = sr;
    }

    /**
     *
     * @param checked
     */
    public void setChecked(boolean checked) {
        this.checked.set(checked);
    }

    /**
     *
     * @return
     */
    public BooleanProperty checkedProperty() {
        return checked;
    }

    /**
     *
     * @return
     */
    public boolean isChecked() {
        return checked.get();
    }
}
