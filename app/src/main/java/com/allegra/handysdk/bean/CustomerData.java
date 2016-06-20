package com.allegra.handysdk.bean;

import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class CustomerData {
    String Customer_ID,Customer_Booking_End_Time,Customer_Booking_Start_Time,Customer_Latitude,
            Customer_Longitude,Service_Name,Lead_Time,Service_ID;
    ArrayList<ServiceProviderData> serviceProviderDatas=new ArrayList<>();

    public ArrayList<ServiceProviderData> getServiceProviderDatas() {
        return serviceProviderDatas;
    }

    public void setServiceProviderDatas(ArrayList<ServiceProviderData> serviceProviderDatas) {
        this.serviceProviderDatas = serviceProviderDatas;
    }

    public String getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getCustomer_Booking_End_Time() {
        return Customer_Booking_End_Time;
    }

    public void setCustomer_Booking_End_Time(String customer_Booking_End_Time) {
        Customer_Booking_End_Time = customer_Booking_End_Time;
    }

    public String getCustomer_Booking_Start_Time() {
        return Customer_Booking_Start_Time;
    }

    public void setCustomer_Booking_Start_Time(String customer_Booking_Start_Time) {
        Customer_Booking_Start_Time = customer_Booking_Start_Time;
    }

    public String getCustomer_Latitude() {
        return Customer_Latitude;
    }

    public void setCustomer_Latitude(String customer_Latitude) {
        Customer_Latitude = customer_Latitude;
    }

    public String getCustomer_Longitude() {
        return Customer_Longitude;
    }

    public void setCustomer_Longitude(String customer_Longitude) {
        Customer_Longitude = customer_Longitude;
    }

    public String getService_Name() {
        return Service_Name;
    }

    public void setService_Name(String service_Name) {
        Service_Name = service_Name;
    }

    public String getLead_Time() {
        return Lead_Time;
    }

    public void setLead_Time(String lead_Time) {
        Lead_Time = lead_Time;
    }

    public String getService_ID() {
        return Service_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
    }
}
