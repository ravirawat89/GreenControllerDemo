package com.netcommlabs.greencontroller.model;

/**
 * Created by Android on 12/13/2017.
 */

public class ModalVlNameSelect {

    private String valveName;
    private boolean selected;

    public ModalVlNameSelect(String valveName, boolean selected) {
        this.valveName = valveName;
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getValveName() {
        return valveName;
    }

    public void setValveName(String valveName) {
        this.valveName = valveName;
    }
}
