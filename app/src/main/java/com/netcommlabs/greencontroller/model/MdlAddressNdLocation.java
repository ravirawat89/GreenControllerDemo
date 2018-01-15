package com.netcommlabs.greencontroller.model;

import java.io.Serializable;

/**
 * Created by Android on 11/27/2017.
 */

public class MdlAddressNdLocation implements Serializable {

    private String flat_num;
    private String streetName;
    private String locality_landmark;
    private String pinCode;
    private String city;
    private String state;
    private Object objPlaceLatLong;
    private String placeWellKnownName;
    private String address_name;

    public Object getObjPlaceLatLong() {
        return objPlaceLatLong;
    }

    public void setObjPlaceLatLong(Object objPlaceLatLong) {
        this.objPlaceLatLong = objPlaceLatLong;
    }

    public String getPlaceWellKnownName() {
        return placeWellKnownName;
    }

    public void setPlaceWellKnownName(String placeWellKnownName) {
        this.placeWellKnownName = placeWellKnownName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    private String placeAddress;

    public MdlAddressNdLocation(String flat_num,
                                String streetName,
                                String locality_landmark,
                                String pinCode,
                                String city,
                                String state,
                                String address_name, Object objPlaceLatLong, String placeWellKnownName, String placeAddress) {

        this.flat_num = flat_num;
        this.streetName = streetName;
        this.locality_landmark = locality_landmark;
        this.pinCode = pinCode;
        this.city = city;
        this.state = state;
        this.address_name = address_name;
        this.objPlaceLatLong = objPlaceLatLong;
        this.placeWellKnownName = placeWellKnownName;
        this.placeAddress = placeAddress;

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

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
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
