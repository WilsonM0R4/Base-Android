package com.allegra.handysdk.bean;

import android.graphics.Bitmap;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class UserDataBean {
    String Email="user@demo.com";
    String Password="123456";
    String UserName;
    String PhoneNumber;
    String AccessToken,Service_ID;
    String User_ID,User_First_Name,User_Last_Name,User_Company,User_Address1,User_Address2,User_ZipCode,User_City,user_gender,User_image,User_Change_Password,Latitude,Longitude;
    Bitmap img_bitmap;

    public String getUser_image() {
        return User_image;
    }

    public String getUser_Change_Password() {
        return User_Change_Password;
    }

    public String getService_ID() {
        return Service_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
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

    public void setUser_Change_Password(String user_Change_Password) {

        User_Change_Password = user_Change_Password;
    }

    public void setUser_image(String user_image) {

        User_image = user_image;
    }

    public Bitmap getImg_bitmap() {
        return img_bitmap;

    }

    public void setImg_bitmap(Bitmap img_bitmap) {
        this.img_bitmap = img_bitmap;
    }

    public String getUser_ID() {
        return User_ID="9";
    }

    public String getUser_gender() {
        return user_gender;
    }

    public void setUser_gender(String user_gender) {
        this.user_gender = user_gender;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
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

    public String getUser_Company() {
        return User_Company;
    }

    public void setUser_Company(String user_Company) {
        User_Company = user_Company;
    }

    public String getUser_Address1() {
        return User_Address1;
    }

    public void setUser_Address1(String user_Address1) {
        User_Address1 = user_Address1;
    }

    public String getUser_Address2() {
        return User_Address2;
    }

    public void setUser_Address2(String user_Address2) {
        User_Address2 = user_Address2;
    }

    public String getUser_ZipCode() {
        return User_ZipCode;
    }

    public void setUser_ZipCode(String user_ZipCode) {
        User_ZipCode = user_ZipCode;
    }

    public String getUser_City() {
        return User_City;
    }

    public void setUser_City(String user_City) {
        User_City = user_City;
    }

    public String getAccessToken() {
        return AccessToken;
    }

    public void setAccessToken(String accessToken) {
        AccessToken = "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4=";
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
