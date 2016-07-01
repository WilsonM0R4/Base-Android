package com.allegra.handysdk.bean;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class MyBookingData {
    String Service_ID,Service_Name,User_First_Name,User_Last_Name,Booking_Time,Booking_Status,Payment_Status,Booking_Date,Lead_Time,Booking_ID,Provider_ID;

    public String getService_ID() {
        return Service_ID;
    }

    public String getBooking_ID() {
        return Booking_ID;
    }

    public void setBooking_ID(String booking_ID) {
        Booking_ID = booking_ID;
    }

    public String getProvider_ID() {
        return Provider_ID;
    }

    public void setProvider_ID(String provider_ID) {
        Provider_ID = provider_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
    }

    public String getService_Name() {
        return Service_Name;
    }

    public void setService_Name(String service_Name) {
        Service_Name = service_Name;
    }

    public String getUser_First_Name() {
        return User_First_Name;
    }

    public void setUser_First_Name(String user_First_Name) {
        User_First_Name = user_First_Name;
    }

    public String getUser_Last_Name() {
        return User_Last_Name;
    }

    public void setUser_Last_Name(String user_Last_Name) {
        User_Last_Name = user_Last_Name;
    }

    public String getBooking_Time() {
        return Booking_Time;
    }

    public void setBooking_Time(String booking_Time) {
        Booking_Time = booking_Time;
    }

    public String getBooking_Status() {
        return Booking_Status;
    }

    public void setBooking_Status(String booking_Status) {
        Booking_Status = booking_Status;
    }

    public String getPayment_Status() {
        return Payment_Status;
    }

    public void setPayment_Status(String payment_Status) {
        Payment_Status = payment_Status;
    }

    public String getBooking_Date() {
        return Booking_Date;
    }

    public void setBooking_Date(String booking_Date) {
        Booking_Date = booking_Date;
    }

    public String getLead_Time() {
        return Lead_Time;
    }

    public void setLead_Time(String lead_Time) {
        Lead_Time = lead_Time;
    }
}
