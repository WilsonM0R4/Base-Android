package com.allegra.handysdk.bean;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class AddressBean {
    String Addressline,Addrestitile,city,zipcode,Latitude,Longitude;

    public String getAddressline() {
        return Addressline;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public void setAddressline(String addressline) {

        Addressline = addressline;
    }

    public String getAddrestitile() {
        return Addrestitile;
    }

    public void setAddrestitile(String addrestitile) {
        Addrestitile = addrestitile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
