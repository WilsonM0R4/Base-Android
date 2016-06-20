package com.allegra.handysdk.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class MainCategoryData implements Serializable {

    String Service_Name,Service_ID,Lead_Time,Service_Image1,Service_Image_thumb;
    public ArrayList<SubService> subServices = new ArrayList<SubService>();


    public String getService_Name() {
        return Service_Name;
    }

    public void setService_Name(String service_Name) {
        Service_Name = service_Name;
    }

    public String getService_ID() {
        return Service_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
    }


    public String getLead_Time() {
        return Lead_Time;
    }

    public void setLead_Time(String lead_Time) {
        Lead_Time = lead_Time;
    }

    public String getService_Image1() {
        return Service_Image1;
    }

    public void setService_Image1(String service_Image1) {
        Service_Image1 = service_Image1;
    }

    public String getService_Image_thumb() {
        return Service_Image_thumb;
    }

    public void setService_Image_thumb(String service_Image_thumb) {
        Service_Image_thumb = service_Image_thumb;
    }

    public ArrayList<SubService> getSubServices() {
        return subServices;
    }

    public void setSubServices(ArrayList<SubService> subServices) {
        this.subServices = subServices;
    }
}
