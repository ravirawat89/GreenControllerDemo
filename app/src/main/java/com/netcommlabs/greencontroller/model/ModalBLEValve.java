package com.netcommlabs.greencontroller.model;

import java.util.List;

/**
 * Created by Android on 11/23/2017.
 */

public class ModalBLEValve {

    int id;
    String valveName;
    String dvcMacAddrs;
    List<DataTransferModel> listValveData;

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

    public List<DataTransferModel> getListValveData() {
        return listValveData;
    }

    public void setListValveData(List<DataTransferModel> listValveData) {
        this.listValveData = listValveData;
    }

    // Empty constructor
    public ModalBLEValve() {

    }

    // constructor
    public ModalBLEValve(int id, String dvcMacAddrs, String valveName, List<DataTransferModel> listValveData) {
        this.id = id;
        this.dvcMacAddrs = dvcMacAddrs;
        this.valveName = valveName;
        this.listValveData = listValveData;
    }

    // constructor
    public ModalBLEValve(String dvcMacAddrs, String valveName, List<DataTransferModel> listValveData) {
        this.dvcMacAddrs = dvcMacAddrs;
        this.valveName = valveName;
        this.listValveData = listValveData;
    }

}