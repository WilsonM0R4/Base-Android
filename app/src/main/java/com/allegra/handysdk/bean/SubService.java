package com.allegra.handysdk.bean;

import java.io.Serializable;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class SubService implements Serializable {
    String Sub_Service_ID,Sub_Service_Name,Need_Specification,serviceName,Site_Inspection,Sub_Service_Code,
            Sub_Service_Description,Sub_Service_Status,Service_ID,Parent_ID,Min_Order_Value,Sub_Service_Type,
            Payment,Sub_Service_Image,Sub_Service_Thumb_Image,Sub_cat_flag,Main_cat_name,Sub_cat_selected;

    public String getSub_cat_flag() {
        return Sub_cat_flag;
    }

    public String getMain_cat_name() {
        return Main_cat_name;
    }

    public void setMain_cat_name(String main_cat_name) {
        Main_cat_name = main_cat_name;
    }

    public String getSub_cat_selected() {
        return Sub_cat_selected;
    }

    public void setSub_cat_selected(String sub_cat_selected) {
        Sub_cat_selected = sub_cat_selected;
    }

    public void setSub_cat_flag(String sub_cat_flag) {

        Sub_cat_flag = sub_cat_flag;

    }

    public String getSub_Service_ID() {
        return Sub_Service_ID;
    }

    public void setSub_Service_ID(String sub_Service_ID) {
        Sub_Service_ID = sub_Service_ID;
    }

    public String getSub_Service_Name() {
        return Sub_Service_Name;
    }

    public void setSub_Service_Name(String sub_Service_Name) {
        Sub_Service_Name = sub_Service_Name;
    }

    public String getNeed_Specification() {
        return Need_Specification;
    }

    public void setNeed_Specification(String need_Specification) {
        Need_Specification = need_Specification;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSite_Inspection() {
        return Site_Inspection;
    }

    public void setSite_Inspection(String site_Inspection) {
        Site_Inspection = site_Inspection;
    }

    public String getSub_Service_Code() {
        return Sub_Service_Code;
    }

    public void setSub_Service_Code(String sub_Service_Code) {
        Sub_Service_Code = sub_Service_Code;
    }

    public String getSub_Service_Description() {
        return Sub_Service_Description;
    }

    public void setSub_Service_Description(String sub_Service_Description) {
        Sub_Service_Description = sub_Service_Description;
    }

    public String getSub_Service_Status() {
        return Sub_Service_Status;
    }

    public void setSub_Service_Status(String sub_Service_Status) {
        Sub_Service_Status = sub_Service_Status;
    }

    public String getService_ID() {
        return Service_ID;
    }

    public void setService_ID(String service_ID) {
        Service_ID = service_ID;
    }

    public String getParent_ID() {
        return Parent_ID;
    }

    public void setParent_ID(String parent_ID) {
        Parent_ID = parent_ID;
    }

    public String getMin_Order_Value() {
        return Min_Order_Value;
    }

    public void setMin_Order_Value(String min_Order_Value) {
        Min_Order_Value = min_Order_Value;
    }

    public String getSub_Service_Type() {
        return Sub_Service_Type;
    }

    public void setSub_Service_Type(String sub_Service_Type) {
        Sub_Service_Type = sub_Service_Type;
    }

    public String getPayment() {
        return Payment;
    }

    public void setPayment(String payment) {
        Payment = payment;
    }

    public String getSub_Service_Image() {
        return Sub_Service_Image;
    }

    public void setSub_Service_Image(String sub_Service_Image) {
        Sub_Service_Image = sub_Service_Image;
    }

    public String getSub_Service_Thumb_Image() {
        return Sub_Service_Thumb_Image;
    }

    public void setSub_Service_Thumb_Image(String sub_Service_Thumb_Image) {
        Sub_Service_Thumb_Image = sub_Service_Thumb_Image;
    }
}
