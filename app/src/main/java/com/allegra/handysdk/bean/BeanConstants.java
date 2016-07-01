package com.allegra.handysdk.bean;

import com.allegra.handysdk.responsebean.UserLoginData;
import com.allegra.handysdk.service.ApiCall;

import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class BeanConstants {
    public static String base_url="http://23.227.133.210/assist/json.php?";
    public static String cms_url="http://23.227.133.210/assist/cmsdetail.php?pagename=About";
    public static String customer_img_upload="http://23.227.133.210/assist/json.php/";
    public static String customer_img_get="http://23.227.133.210/assist/timthumb.php?src=uploads/customer/";
    public static String customer_image_category="http://23.227.133.210/assist/timthumb.php?src=uploads/category1/";
    public static String customer_image_category2="http://23.227.133.210/assist/timthumb.php?src=uploads/category2/";
    public static String customer_image_subservice="http://23.227.133.210/assist/timthumb.php?src=uploads/SubServiceImage/";
    public static String encryptkey="AssistPrjctxyzqforencyptqwalgkey";
    public static ApiCall service=new ApiCall();
    public static UserDataBean userBeen=new UserDataBean();
    public static UserLoginData loginData=new UserLoginData();
    public static AddressBean addressBean=new AddressBean();
    public static BookingDataBean BookingData=new BookingDataBean();
    public static MainCategoryData mainCategoryData=new MainCategoryData();
    public static Feedback_Bean feedback_bean=new Feedback_Bean();
    public static ArrayList<MainCategoryData> CategoryData=new ArrayList<>();
}
