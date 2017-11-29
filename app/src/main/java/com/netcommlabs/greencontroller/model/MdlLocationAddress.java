package com.netcommlabs.greencontroller.model;

import java.io.Serializable;

/**
 * Created by Android on 11/27/2017.
 */

public class MdlLocationAddress implements Serializable {

    String flat_num;
    String locality_landmark;
    String streetName;
    String pincode;
    String city;
    String state;
    String address_name;

    public MdlLocationAddress(String flat_num,
                              String locality_landmark,
                              String streetName,
                              String pincode,
                              String city,
                              String state,
                              String address_name) {

        this.flat_num = flat_num;
        this.locality_landmark = locality_landmark;
        this.streetName = streetName;
        this.pincode = pincode;
        this.city = city;
        this.state = state;
        this.address_name = address_name;

    }

    public String getFlat_num() {
        return flat_num;
    }

    public void setFlat_num(String flat_num) {
        this.flat_num = flat_num;
    }

    public String getLocality_landmark() {
        return locality_landmark;
    }

    public void setLocality_landmark(String locality_landmark) {
        this.locality_landmark = locality_landmark;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

}
