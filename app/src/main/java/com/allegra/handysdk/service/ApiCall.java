package com.allegra.handysdk.service;

import android.app.Activity;
import android.app.Dialog;
import android.widget.ProgressBar;

import com.allegra.handysdk.bean.CustomerData;
import com.allegra.handysdk.bean.MainCategoryData;
import com.allegra.handysdk.bean.MyBookingData;
import com.allegra.handysdk.bean.ServiceProviderData;
import com.allegra.handysdk.bean.SubService;
import com.allegra.handysdk.bean.TimeSlotBean;
import com.allegra.handysdk.bean.TrackingData;

import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class ApiCall {

    static Activity activity;
    static  String enc="";
    static ArrayList<MyBookingData> BookingList=new ArrayList<>();
    static ArrayList<TrackingData> array_trackingdata=new ArrayList<>();
    static SubService subservice;
    static  ArrayList<SubService> arraySubService=new ArrayList<>();
    static  ArrayList<MainCategoryData> MainService=new ArrayList<>();
    public static MainCategoryData mainCategoryData;

    static ServiceProviderData serviceProviderData;
    public static ArrayList<ServiceProviderData> ServiceProviderList=new ArrayList<>();
    public static ArrayList<CustomerData> CustomerList=new ArrayList<>();
    public static ArrayList<TimeSlotBean> TimeSlotList=new ArrayList<>();
    public static  TimeSlotBean timeSlotBean;
    static CustomerData customerData;
    static Double latitude,longitude;

    static Boolean upload,img=false;
    Dialog dialog;
    ProgressBar progressbar;
    String imgname="";

}
