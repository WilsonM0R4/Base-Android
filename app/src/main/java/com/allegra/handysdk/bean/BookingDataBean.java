package com.allegra.handysdk.bean;

import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class BookingDataBean {
    public String getBooking_Time() {
        return Booking_Time;
    }

    public void setBooking_Time(String booking_Time) {
        Booking_Time = booking_Time;
    }

    public String getBooking_Date() {
        return Booking_Date;
    }

    public String getRequired_Estimation() {
        return Required_Estimation;
    }

    public void setRequired_Estimation(String required_Estimation) {
        Required_Estimation = required_Estimation;
    }

    public void setBooking_Date(String booking_Date) {
        Booking_Date = booking_Date;
    }

    String User_ID,Booking_Type,Customer_Longitude,Booking_Address,
            Customer_Booking_End_Time,Customer_Booking_Start_Time,
            User_Selected_Service,User_Selected_ServiceId,Customer_Latitude,Booking_Remarks,Booking_Time,Booking_Date,Required_Estimation;
    ArrayList<String> Selected_Sub_Service_ids;

    public String getBooking_Remarks() {
        return Booking_Remarks;
    }

    public void setBooking_Remarks(String booking_Remarks) {
        Booking_Remarks = booking_Remarks;
    }

    public String getCustomer_Latitude() {
        return Customer_Latitude;
    }

    public void setCustomer_Latitude(String customer_Latitude) {
        Customer_Latitude = customer_Latitude;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getBooking_Type() {
        return Booking_Type;
    }

    public void setBooking_Type(String booking_Type) {
        Booking_Type = booking_Type;
    }

    public String getCustomer_Longitude() {
        return Customer_Longitude;
    }

    public void setCustomer_Longitude(String customer_Longitude) {
        Customer_Longitude = customer_Longitude;
    }

    public String getBooking_Address() {
        return Booking_Address;
    }

    public void setBooking_Address(String booking_Address) {
        Booking_Address = booking_Address;
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

    public String getUser_Selected_Service() {
        return User_Selected_Service;
    }

    public void setUser_Selected_Service(String user_Selected_Service) {
        User_Selected_Service = user_Selected_Service;
    }

    public String getUser_Selected_ServiceId() {
        return User_Selected_ServiceId;
    }

    public void setUser_Selected_ServiceId(String user_Selected_ServiceId) {
        User_Selected_ServiceId = user_Selected_ServiceId;
    }

    public ArrayList<String> getSelected_Sub_Service_ids() {
        return Selected_Sub_Service_ids;
    }

    public void setSelected_Sub_Service_ids(ArrayList<String> selected_Sub_Service_ids) {
        Selected_Sub_Service_ids = selected_Sub_Service_ids;
    }
}
