package com.netcommlabs.greencontroller.model;

import java.util.List;

/**
 * Created by Android on 11/22/2017.
 */

public class ModalBLEDevice {

    //private variables
    int id;
    String dvcName;
    String dvcMacAddrs;
    int valveNum;
    MdlLocationAddress mdlLocationAddress;

    // Empty constructor
    public ModalBLEDevice() {

    }

    // constructor
    public ModalBLEDevice(int id, String dvcName, String dvcMacAddrs, MdlLocationAddress mdlLocationAddress, int valveNum) {
        this.id = id;
        this.dvcName = dvcName;
        this.dvcMacAddrs = dvcMacAddrs;
        this.mdlLocationAddress = mdlLocationAddress;
        this.valveNum = valveNum;
    }

    // constructor
    public ModalBLEDevice(String dvcName, String dvcMacAddrs, MdlLocationAddress mdlLocationAddress, int valveNum) {
        this.dvcName = dvcName;
        this.dvcMacAddrs = dvcMacAddrs;
        this.mdlLocationAddress = mdlLocationAddress;
        this.valveNum = valveNum;
    }

    // getting ID
    public int getID() {
        return this.id;
    }

    // setting id
    public void setID(int id) {
        this.id = id;
    }

    // getting dvcName
    public String getName() {
        return this.dvcName;
    }

    // setting dvcName
    public void setName(String dvcName) {
        this.dvcName = dvcName;
    }

    // getting dvc MAC
    public String getDvcMacAddrs() {
        return this.dvcMacAddrs;
    }

    // setting dvc MAC
    public void setDvcMacAddrs(String dvcMacAddrs) {
        this.dvcMacAddrs = dvcMacAddrs;
    }

    public MdlLocationAddress getMdlLocationAddress() {
        return mdlLocationAddress;
    }

    public void setMdlLocationAddress(MdlLocationAddress mdlLocationAddress) {
        this.mdlLocationAddress = mdlLocationAddress;
    }

    public int getValvesNum() {
        return valveNum;
    }

    public void setValvesNum(int valveNum) {
        this.valveNum = valveNum;
    }
}