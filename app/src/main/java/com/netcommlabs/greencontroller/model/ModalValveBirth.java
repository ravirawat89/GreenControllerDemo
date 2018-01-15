package com.netcommlabs.greencontroller.model;

import java.util.ArrayList;

/**
 * Created by Android on 11/23/2017.
 */

public class ModalValveBirth {

    private int id;
    private String valveName;
    private String dvcMacAddrs;
    private ArrayList<DataTransferModel> listValveData;
    private String valveSelected;
    private String valveState;
    private String flushStatus;

    public String getValveSelected() {
        return valveSelected;
    }

    public void setValveSelected(String valveSelected) {
        this.valveSelected = valveSelected;
    }

    public String getValveState() {
        return valveState;
    }

    public void setValveState(String valveState) {
        this.valveState = valveState;
    }

    public String getFlushStatus() {
        return flushStatus;
    }

    public void setFlushStatus(String flushStatus) {
        this.flushStatus = flushStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValveName() {
        return valveName;
    }

    public void setValveName(String valveName) {
        this.valveName = valveName;
    }

    public String getDvcMacAddrs() {
        return dvcMacAddrs;
    }

    public void setDvcMacAddrs(String dvcMacAddrs) {
        this.dvcMacAddrs = dvcMacAddrs;
    }

    public ArrayList<DataTransferModel> getListValveData() {
        return listValveData;
    }

    public void setListValveData(ArrayList<DataTransferModel> listValveData) {
        this.listValveData = listValveData;
    }

    // Empty constructor
    public ModalValveBirth() {

    }

    // constructor
    public ModalValveBirth(int id, String dvcMacAddrs, String valveName, ArrayList<DataTransferModel> listValveData, String valveState, String flushStatus) {
        this.id = id;
        this.dvcMacAddrs = dvcMacAddrs;
        this.valveName = valveName;
        this.listValveData = listValveData;
        this.valveState = valveState;
        this.flushStatus = flushStatus;
    }

    // constructor
    public ModalValveBirth(String dvcMacAddrs, String valveName, ArrayList<DataTransferModel> listValveData, String valveSelected, String valveState, String flushStatus) {
        this.dvcMacAddrs = dvcMacAddrs;
        this.valveName = valveName;
        this.listValveData = listValveData;
        this.flushStatus = flushStatus;
        this.valveSelected = valveSelected;
        this.valveState = valveState;
    }

    // constructor
    public ModalValveBirth(ArrayList<DataTransferModel> listValveData, /*String valveSelected, */String valveState, String flushStatus) {
        this.listValveData = listValveData;
        //this.valveName = valveSelected;
        this.valveState = valveState;
        this.flushStatus = flushStatus;
    }

}