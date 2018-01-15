package com.netcommlabs.greencontroller.model;

/**
 * Created by Android on 11/22/2017.
 */

public class ModalBLEDevice {

    //private variables
    int id;
    String dvcName;
    String dvcMacAddrs;
    int valveNum;
    MdlAddressNdLocation mdlAddressNdLocation;

    // Empty constructor
    public ModalBLEDevice() {

    }

    // constructor
    public ModalBLEDevice(int id, String dvcName, String dvcMacAddrs, MdlAddressNdLocation mdlAddressNdLocation, int valveNum) {
        this.id = id;
        this.dvcName = dvcName;
        this.dvcMacAddrs = dvcMacAddrs;
        this.mdlAddressNdLocation = mdlAddressNdLocation;
        this.valveNum = valveNum;
    }

    // constructor
    public ModalBLEDevice(String dvcName, String dvcMacAddrs, MdlAddressNdLocation mdlAddressNdLocation, int valveNum) {
        this.dvcName = dvcName;
        this.dvcMacAddrs = dvcMacAddrs;
        this.mdlAddressNdLocation = mdlAddressNdLocation;
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

    public MdlAddressNdLocation getMdlLocationAddress() {
        return mdlAddressNdLocation;
    }

    public void setMdlLocationAddress(MdlAddressNdLocation mdlLocationAddress) {
        this.mdlAddressNdLocation = mdlLocationAddress;
    }

    public int getValvesNum() {
        return valveNum;
    }

    public void setValvesNum(int valveNum) {
        this.valveNum = valveNum;
    }
}