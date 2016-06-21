package com.allegra.handysdk.service;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.allegra.handysdk.algoritham.AESCrypt;
import com.allegra.handysdk.bean.AddressResponseBean;
import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.CustomerData;
import com.allegra.handysdk.bean.InvoiceBean;
import com.allegra.handysdk.bean.MainCategoryData;
import com.allegra.handysdk.bean.MyBookingData;
import com.allegra.handysdk.bean.ServiceProviderData;
import com.allegra.handysdk.bean.SubService;
import com.allegra.handysdk.bean.TimeSlotBean;
import com.allegra.handysdk.bean.TrackingData;
import com.allegra.handysdk.responsebean.AddresslistInterface;
import com.allegra.handysdk.responsebean.BookingInteface;
import com.allegra.handysdk.responsebean.CategoryCallInterface;
import com.allegra.handysdk.responsebean.InvoiceDetailInterface;
import com.allegra.handysdk.responsebean.MyBokkingInterface;
import com.allegra.handysdk.responsebean.ServiceProviderCallInterface;
import com.allegra.handysdk.responsebean.TrackingDetailInterface;
import com.allegra.handysdk.utilsclasses.IsNetworkConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class ApiCall {
    static  Activity activity;
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


//==========================================Home page========================================================


    public void Categorycall(Activity activity,String service){
        this.activity=activity;
        String enc="";
        try {
            String encyptiontag= BeanConstants.userBeen.getEmail()+BeanConstants.userBeen.getPassword();
            AESCrypt aeccrypt=new AESCrypt(BeanConstants.encryptkey);
            enc=aeccrypt.encrypt(encyptiontag);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }

        String user_data="";
        user_data = "[{\"User_Email\":\"" + BeanConstants.userBeen.getEmail()
                + "\",\"User_Password\":\"" + BeanConstants.userBeen.getPassword()
                + "\"}]";

        String url=BeanConstants.base_url+"action=GetCategory";
        String json = "[{\"User_ID\":\"" + "9"
                + "\",\"page\":\"" + "1"
                + "\",\"Service_ID\":\"" + ""
                + "\",\"Service_Name\":\"" + service
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                + "\"}]";
        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "GetCategory").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }


    }


    public static void CategoryData(String resultString) {
        try {
            MainService=new ArrayList<>();
            JSONArray jaara=new JSONArray(resultString);

            JSONObject jsonObject=jaara.optJSONObject(0);

            if (jsonObject.has("status") && jsonObject.getBoolean("status")){
                Log.d("System out", "in if");
                JSONArray jaaradata=jsonObject.getJSONArray("data");

                for (int i=0;i<jaaradata.length();i++){

                    JSONObject mainserviceobject=jaaradata.optJSONObject(i);
                    Log.d("System out"," lenth: "+i+mainserviceobject.optString("Service_Name"));
                    mainCategoryData=new MainCategoryData();
                    mainCategoryData.setService_Name(mainserviceobject.optString("Service_Name"));
                    mainCategoryData.setService_ID(mainserviceobject.optString("Service_ID"));
                    mainCategoryData.setLead_Time(mainserviceobject.optString("Lead_Time"));
                    mainCategoryData.setService_Image1(mainserviceobject.optString("Service_Image1"));
                    mainCategoryData.setService_Image_thumb(mainserviceobject.optString("Service_Image_thumb"));
                    arraySubService =new ArrayList<>();
                    if (mainserviceobject.has("subServices")){
                        JSONArray subarray=mainserviceobject.optJSONArray("subServices");
                        Log.d("System out","Subarray size: "+subarray.length());
                        for (int j=0;j<subarray.length();j++){
                            JSONObject Subserviceobj=subarray.getJSONObject(j);
                            subservice=new SubService();
                            subservice.setSub_Service_ID(Subserviceobj.optString("Sub_Service_ID"));
                            subservice.setSub_Service_Name(Subserviceobj.optString("Sub_Service_Name"));
                            subservice.setNeed_Specification(Subserviceobj.optString("Need_Specification"));
                            subservice.setServiceName(Subserviceobj.optString("serviceName"));
                            subservice.setSub_Service_Code(Subserviceobj.optString("Sub_Service_Code"));
                            subservice.setSub_Service_Description(Subserviceobj.optString("Sub_Service_Description"));
                            subservice.setSub_Service_Status(Subserviceobj.optString("Sub_Service_Status"));
                            subservice.setService_ID(Subserviceobj.optString("Service_ID"));
                            subservice.setParent_ID(Subserviceobj.optString("Parent_ID"));
                            subservice.setMin_Order_Value(Subserviceobj.optString("Min_Order_Value"));
                            subservice.setSub_Service_Type(Subserviceobj.optString("Sub_Service_Type"));
                            subservice.setPayment(Subserviceobj.optString("Payment"));
                            subservice.setSub_Service_Image(Subserviceobj.optString("Sub_Service_Image"));
                            subservice.setSub_Service_Thumb_Image(Subserviceobj.optString("Sub_Service_Thumb_Image"));
                            subservice.setSub_cat_flag("false");
                            subservice.setSub_cat_selected("false");
                            subservice.setMain_cat_name(mainserviceobject.optString("Service_Name"));
                            arraySubService.add(subservice);

                        }
                        mainCategoryData.setSubServices(arraySubService);
                    }
                    Log.e("System out", "::" + mainCategoryData.getSubServices());
                    BeanConstants.CategoryData.add(mainCategoryData);
                    MainService.add(mainCategoryData);
                }
                for (int i=0;i<MainService.size();i++){
                    Log.d("System out","name::"+MainService.get(i).getService_Name());
                }

                CategoryCallInterface CI=(CategoryCallInterface)activity;
                CI.CategoryData(MainService);

            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }
    }

//=====================================mapactivity============================================


    public void ServiceProviderCall(Activity activity){
        this.activity=activity;
        String enc="";

        if (BeanConstants.userBeen.getService_ID().length()==0){
            Toast.makeText(activity, "Please select atleast one service", Toast.LENGTH_SHORT).show();
        }else{
            try {
                String encyptiontag=BeanConstants.userBeen.getEmail()+BeanConstants.userBeen.getPassword();
                AESCrypt aeccrypt=new AESCrypt(BeanConstants.encryptkey);
                enc=aeccrypt.encrypt(encyptiontag);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
            }
            String url= BeanConstants.base_url+"action=findProviderFromServiceId";
            String json = "[{\"User_ID\":\"" +"9" + "\"" + ","
                    + "\"Service_ID\":\"" + BeanConstants.userBeen.getService_ID()+ "\"" + ","
                    + "\"SubservicesArray\":[";

            for (int i = 0; i < BeanConstants.BookingData.getSelected_Sub_Service_ids().size(); i++) {
                if (i == BeanConstants.BookingData.getSelected_Sub_Service_ids().size() - 1) {
                    json += "{\"Service_ID\":\"" + BeanConstants.BookingData.getUser_Selected_ServiceId() + "\""
                            + "," + "\"Sub_Service_ID\":\"" +BeanConstants.BookingData.getSelected_Sub_Service_ids().get(i) + "\""
                            + "}";
                } else {
                    json += "{\"Service_ID\":\"" + BeanConstants.BookingData.getUser_Selected_ServiceId() + "\""
                            + "," + "\"Sub_Service_ID\":\"" +BeanConstants.BookingData.getSelected_Sub_Service_ids().get(i) + "\""+ "},";
                }

            }
            json += "]"+ ","  +"\"User_Latitude\":\"" + BeanConstants.userBeen.getLatitude()
                    + "\",\"User_Longitude\":\"" + BeanConstants.userBeen.getLongitude()
                    + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                    + "\"}]";
            latitude=Double.parseDouble(BeanConstants.userBeen.getLatitude());
            longitude=Double.parseDouble(BeanConstants.userBeen.getLongitude());

            if (IsNetworkConnection.checkNetworkConnection(activity)){
                new post_async(activity, "GetCustomer").execute(url, json);
            }else{
                Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
            }


        }

    }

    public static void ServiceProviderData(String resultString) {
        try {
            ServiceProviderList=new ArrayList<>();
            CustomerList=new ArrayList<>();
            JSONArray jsonArray=new JSONArray(resultString);
            JSONObject jsonObject=jsonArray.getJSONObject(0);
            if (jsonObject.has("status") && jsonObject.optBoolean("status")){
                Log.d("System out","1");
                JSONArray Dataarray=jsonObject.getJSONArray("data");
                for (int i=0;i<Dataarray.length();i++){
                    Log.d("System out","2");
                    JSONObject dataobj=Dataarray.optJSONObject(i);

                    customerData=new CustomerData();
                    customerData.setCustomer_Booking_End_Time(dataobj.optString("End_Time"));
                    customerData.setCustomer_Booking_Start_Time(dataobj.optString("Start_Time"));
                    customerData.setCustomer_Latitude(dataobj.optString("User_Latitude"));
                    customerData.setCustomer_Longitude(dataobj.optString("User_Longitude"));
                    customerData.setService_Name(dataobj.optString("Service_Name"));
                    customerData.setLead_Time(dataobj.optString("Lead_Time"));
                    customerData.setService_ID(dataobj.optString("Service_ID"));
                    if (dataobj.has("Provider") &&  dataobj.optJSONArray("Provider")!=null){
                        Log.d("System out","3");
                        JSONArray providersrray=dataobj.optJSONArray("Provider");
                        JSONObject providerobj1=providersrray.optJSONObject(0);
                        //  if (providerobj1.has("status") && providerobj1.optBoolean("status")){
                        if (!providerobj1.has("status")){
                            Log.d("System out","providersrray size:"+providersrray.length());
                            for (int j=0;j<providersrray.length();j++){
                                Log.d("System out","in for api call");
                                JSONObject providerobj=providersrray.optJSONObject(j);
                                JSONObject providerobjatzero=providersrray.optJSONObject(0);
                                serviceProviderData=new ServiceProviderData();
                                serviceProviderData.setUser_ID(providerobj.optString("Provider_ID"));
                                serviceProviderData.setUser_First_Name(providerobj.optString("Provider_FirstName"));
                                serviceProviderData.setUser_Last_Name(providerobj.optString("Provider_LastName"));
                                serviceProviderData.setUser_Mobile(providerobj.optString("Provider_Mobile"));
                                serviceProviderData.setUser_Latitude(providerobj.optString("Provider_Latitude"));
                                serviceProviderData.setUser_Longitude(providerobj.optString("Provider_Longitutde"));
                                serviceProviderData.setSub_Service_Image(providerobjatzero.optString("Sub_Service_Image"));
                                serviceProviderData.setSub_Service_Name(providerobjatzero.optString("Sub_Service_Name"));


                                Double lat1 = Double.parseDouble(providerobjatzero.optString("Provider_Latitude"));
                                Double lng1 = Double.parseDouble(providerobjatzero.optString("Provider_Longitutde"));

                                Double lat2 = latitude;
                                Double lng2 = longitude;

                                Location locationA = new Location("");
                                locationA.setLatitude(lat1);
                                locationA.setLongitude(lng1);

                                Location locationB = new Location("");
                                locationB.setLatitude(lat2);
                                locationB.setLongitude(lng2);

                                double newdis = locationA.distanceTo(locationB);
                                double dismile = newdis / 1000;
                                double time = (dismile/20)*60;

                                Log.e("System out", "new dist " + (dismile / 20) + " dismile " + dismile + "Time " + time);

                                if(Math.round(time)>=60){
                                    serviceProviderData.setEstimate_Time((Math.round(time)) / 60 + " Hours");
                                }else{
                                    serviceProviderData.setEstimate_Time(Math.round(time) + " Min");
                                }
                                ServiceProviderList.add(serviceProviderData);
                            }
                            customerData.setServiceProviderDatas(ServiceProviderList);
                        }
                        CustomerList.add(customerData);

                        if (CustomerList.size()>0){
                            ServiceProviderCallInterface SI=(ServiceProviderCallInterface)activity;
                            SI.ServiceProviderresponse(CustomerList);
                        }else{
                            Toast.makeText(activity,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(activity,"Sorry! Currently provider is not available.",Toast.LENGTH_SHORT).show();
                        ServiceProviderCallInterface SI=(ServiceProviderCallInterface)activity;
                        SI.ServiceProviderresponse(CustomerList);
                    }


                }

            }else{
                Toast.makeText(activity,jsonObject.optString("message"),Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }
    }


    public void GetTimeSlot(Activity activity,String date){
        this.activity=activity;
        String enc="";
        try {
            String encyptiontag=BeanConstants.userBeen.getEmail()+BeanConstants.userBeen.getPassword();
            AESCrypt aeccrypt=new AESCrypt(BeanConstants.encryptkey);
            enc=aeccrypt.encrypt(encyptiontag);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }


        String url= BeanConstants.base_url+"action=timesloat";
        String json = "[{\"User_ID\":\"" + "9"
                + "\",\"date\":\"" + date
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                + "\"}]";
        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "timesloat").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }


    }

    public static void TimeslotData(String resultString) {
        try {
            TimeSlotList=new ArrayList<>();
            JSONArray jaara=new JSONArray(resultString);
            JSONObject jobj=jaara.getJSONObject(0);
            if (jobj.has("status") && jobj.optBoolean("status")){
                Log.d("System out","in if time: ");
                JSONArray dataarray=jobj.optJSONArray("data");
                for (int i=0;i<dataarray.length();i++){

                    JSONObject dataobj=dataarray.optJSONObject(i);
                    timeSlotBean=new TimeSlotBean();
                    timeSlotBean.setDay(dataobj.optString("Day"));
                    timeSlotBean.setTime_Slot_Id(dataobj.optString("Time_Slot_Id"));
                    timeSlotBean.setFrom_Time(dataobj.optString("From_Time"));
                    timeSlotBean.setTo_Time(dataobj.optString("To_Time"));
                    TimeSlotList.add(timeSlotBean);
                }
                ServiceProviderCallInterface TI=(ServiceProviderCallInterface)activity;
                TI.Timeslotresponse(TimeSlotList);
            }else{
                Log.d("System out","message: "+jobj.optString("message"));
                Toast.makeText(activity,"No Time slots available",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }


    }
    //=============================AddAddress=======================================================


    public void AddAddress(Activity activity){
        this.activity=null;
        this.activity=activity;
        try {
            String encyptiontag= BeanConstants.userBeen.getEmail()+BeanConstants.userBeen.getPassword();
            AESCrypt aeccrypt=new AESCrypt(BeanConstants.encryptkey);
            enc=aeccrypt.encrypt(encyptiontag);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }


        String url=BeanConstants.base_url+"action=addAddress";
        String json = "[{\"User_ID\":\"" + "9"
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                + "\",\"User_Address\":\"" + BeanConstants.addressBean.getAddressline()
                + "\",\"User_City\":\"" + BeanConstants.addressBean.getCity()
                + "\",\"User_Latitude\":\"" + BeanConstants.addressBean.getLatitude()
                + "\",\"User_Longitutde\":\"" + BeanConstants.addressBean.getLongitude()
                + "\",\"User_ZipCode\":\"" + BeanConstants.addressBean.getZipcode()
                + "\",\"User_Address_Title\":\"" + BeanConstants.addressBean.getAddrestitile()
                + "\"}]";

        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "addAddress").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }



    }

    public static void addAddressResponse(String resultString) {


        try {
            JSONArray mainarray=new JSONArray(resultString);
            JSONObject mainobj=mainarray.optJSONObject(0);
            Log.d("System out", "activity: " + activity);

            if (mainobj.optBoolean("status")&& mainobj.optBoolean("status") ){
                ServiceProviderCallInterface SI=(ServiceProviderCallInterface)activity;
                SI.AddAddressResponse("true");
            }else{
                // Toast.makeText(activity, mainobj.optString("message"), Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }

    }

    //==========================================MyBookingActivity==========================================

    public void GetBookingList(Activity activity,String BookingStatus){
        this.activity=activity;
        try {
            String encyptiontag= BeanConstants.userBeen.getEmail()+BeanConstants.userBeen.getPassword();
            AESCrypt aeccrypt=new AESCrypt(BeanConstants.encryptkey);
            enc=aeccrypt.encrypt(encyptiontag);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }
        String url=BeanConstants.base_url+"action=getBookingHistoryByCustomer";
        String json = "[{\"User_ID\":\"" + "9"
                + "\",\"Booking_Status\":\"" + BookingStatus
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                + "\"}]";

        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "getBookingListByCustomerId").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }




    }

    public static void BookingListData(String resultString) {
        try{
            BookingList=new ArrayList<>();
            JSONArray mainarray=new JSONArray(resultString);
            JSONObject mainobj=mainarray.getJSONObject(0);
            if (mainobj.has("status") && mainobj.optBoolean("status")){
                JSONArray dataarray=mainobj.optJSONArray("data");
                for (int i=0;i<dataarray.length();i++){
                    JSONObject dataobj=dataarray.optJSONObject(i);
                    MyBookingData bookingData=new MyBookingData();
                    bookingData.setService_ID(dataobj.optString("Service_ID"));
                    bookingData.setService_Name(dataobj.optString("Service_Name"));
                    bookingData.setUser_First_Name(dataobj.optString("Provider_FirstName"));
                    bookingData.setUser_Last_Name(dataobj.optString("Provider_LastName"));
                    bookingData.setBooking_ID(dataobj.optString("Booking_ID"));
                    bookingData.setBooking_Time(dataobj.optString("Booking_Time"));
                    bookingData.setBooking_Status(dataobj.optString("Booking_Status"));
                    bookingData.setPayment_Status(dataobj.optString("Payment_Status"));
                    bookingData.setBooking_Date(dataobj.optString("Booking_Date"));
                    bookingData.setLead_Time(dataobj.optString("Lead_Time"));
                    bookingData.setProvider_ID(dataobj.optString("Provider_ID"));
                    BookingList.add(bookingData);
                }
//                    MyBokkingInterface BI=(MyBokkingInterface)activity;
//                    BI.BookingResponse(BookingList);
            }else{
                Toast.makeText(activity,"No Booking found",Toast.LENGTH_SHORT).show();
            }

            MyBokkingInterface BI=(MyBokkingInterface)activity;
            BI.BookingResponse(BookingList);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }

    }


    //============================================invoice datail=====================================================
    public void GetInvoiceDetail(Activity activity,String BookingID){
        this.activity=activity;
        String url=BeanConstants.base_url+"action=getInvoiceDetails";
        String json = "[{\"User_ID\":\"" + "9"
                + "\",\"Booking_ID\":\"" + BookingID
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                + "\"}]";

        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "getInvoiceDetails").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }



    }

    public static void InvoiceDetailResponse(String resultString) {
        try{
            //BookingList=new ArrayList<>();
            JSONArray mainarray=new JSONArray(resultString);
            JSONObject mainobj=mainarray.getJSONObject(0);
            if (mainobj.has("status") && mainobj.optBoolean("status")){
                JSONArray dataarray=mainobj.optJSONArray("data");
                for (int i=0;i<dataarray.length();i++){
                    JSONObject dataobj=dataarray.optJSONObject(i);
                    InvoiceBean InvoiceData=new InvoiceBean();
                    InvoiceData.setSub_Service_Name(dataobj.optString("Sub_Service_Name"));
                    InvoiceData.setService_Name(dataobj.optString("Service_Name"));
                    InvoiceData.setBooking_ID(dataobj.optString("Booking_ID"));
                    InvoiceData.setCustomer_FirstName(dataobj.optString("Customer_FirstName"));
                    InvoiceData.setCustomer_LastName(dataobj.optString("Customer_LastName"));
                    InvoiceData.setInvoice_ID(dataobj.optString("Invoice_ID"));
                    InvoiceData.setInvoice_Start_Date(dataobj.optString("Invoice_Start_Date"));
                    InvoiceData.setInvoice_End_Date(dataobj.optString("Invoice_End_Date"));
                    InvoiceData.setInvoice_Start_Time(dataobj.optString("Invoice_Start_Time"));
                    InvoiceData.setInvoice_End_Time(dataobj.optString("Invoice_End_Time"));
                    InvoiceData.setInvoice_Feedback(dataobj.optString("Invoice_Feedback"));
                    InvoiceData.setInvoice_Amount(dataobj.optString("Invoice_Amount"));
                    InvoiceData.setInvoice_Date(dataobj.optString("Invoice_Date"));
                    InvoiceData.setPayment_Mode(dataobj.optString("Payment_Mode"));
                    InvoiceData.setProvider_FIrstName(dataobj.optString("Provider_FIrstName"));
                    InvoiceData.setProvider_LastName(dataobj.optString("Provider_LastName"));
                    InvoiceData.setProvider_Booking_Start_Time(dataobj.optString("Provider_Booking_Start_Time"));
                    InvoiceData.setProvider_Booking_End_Time(dataobj.optString("Provider_Booking_End_Time"));
                    InvoiceData.setInvoice_Quantity(dataobj.optString("Invoice_Quantity"));
                    InvoiceDetailInterface BI=(InvoiceDetailInterface)activity;
                    BI.ResponseOfInvoiceDetail(InvoiceData);
                    break;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }
    }


    public void InsertFeedback(Activity activity,String Invoice_Id){
        this.activity=activity;
        if (BeanConstants.feedback_bean.getBooking_id().trim().length()==0){
            Toast.makeText(activity,"BookingId missing",Toast.LENGTH_SHORT).show();
        }else if (BeanConstants.feedback_bean.getProvider_id().trim().length()==0){
            Toast.makeText(activity,"ProviderId missing",Toast.LENGTH_SHORT).show();
        }else if (BeanConstants.feedback_bean.getRating_date().trim().length()==0){
            Toast.makeText(activity,"Rating Date missing",Toast.LENGTH_SHORT).show();
        }else{
            String url=BeanConstants.base_url+"action=insertfeedback";
            String json = "[{\"User_ID\":\"" + "9"
                    + "\",\"Invoice_ID\":\"" + Invoice_Id
                    + "\",\"Invoice_Feedback\":\"" + BeanConstants.feedback_bean.getComments()
                    + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                    + "\"}]";

            if (IsNetworkConnection.checkNetworkConnection(activity)){
                new post_async(activity, "insertfeedback").execute(url, json);
            }else{
                Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
            }


        }

    }

    public static void InsertFeedbackResponse(String resultString) {
        try{
            //BookingList=new ArrayList<>();
            JSONArray mainarray=new JSONArray(resultString);
            JSONObject mainobj=mainarray.getJSONObject(0);
            if (mainobj.has("status") && mainobj.optBoolean("status")){
                Toast.makeText(activity,"Feedback has been inserted successfully",Toast.LENGTH_SHORT).show();
                InvoiceDetailInterface BI=(InvoiceDetailInterface)activity;
                BI.ResponseOfInvoiceDetailfeedback();
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }
    }

    //==============================================GooglePlaceSearch==========================================


    public void GetAddressList(Activity activity){
        this.activity=null;
        this.activity=activity;
        try {
            String encyptiontag= BeanConstants.userBeen.getEmail()+BeanConstants.userBeen.getPassword();
            AESCrypt aeccrypt=new AESCrypt(BeanConstants.encryptkey);
            enc=aeccrypt.encrypt(encyptiontag);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }


        String url=BeanConstants.base_url+"action=addressList";
        String json = "[{\"User_ID\":\"" + "9"
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4=" + "\",\"Address_Name\":\"" + ""
                + "\"}]";

        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "addressList").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }

    }



    public static void addressListResponse(String resultString) {

        try {
            ArrayList<AddressResponseBean> addresslist=new ArrayList<>();
            JSONArray mainarray=new JSONArray(resultString);
            JSONObject mainobj=mainarray.optJSONObject(0);
            if (mainobj.optBoolean("status") && mainobj.optBoolean("status") ){
                JSONArray dataarray=mainobj.optJSONArray("data");
                for (int i=0;i<dataarray.length();i++){
                    JSONObject dataobj=dataarray.optJSONObject(i);
                    AddressResponseBean responsebean=new AddressResponseBean();
                    responsebean.setUser_ID(dataobj.optString("User_ID"));
                    responsebean.setUser_Address_ID(dataobj.optString("User_Address_ID"));
                    responsebean.setUser_Address(dataobj.optString("User_Address"));
                    responsebean.setUser_City(dataobj.optString("User_City"));
                    responsebean.setUser_Latitude(dataobj.optString("User_Latitude"));
                    responsebean.setUser_Longitude(dataobj.optString("User_Longitude"));
                    responsebean.setUser_ZipCode(dataobj.optString("User_ZipCode"));
                    responsebean.setUser_Address_Title(dataobj.optString("User_Address_Title"));
                    addresslist.add(responsebean);
                }
                AddresslistInterface AI=(AddresslistInterface)activity;
                AI.AddressListresponse(addresslist);
            }else{
                Toast.makeText(activity, mainobj.optString("message"), Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }

    }


    public void Detelteaddress(Activity activity,String addresid){
        this.activity=activity;
        try {
            String encyptiontag= BeanConstants.userBeen.getEmail()+BeanConstants.userBeen.getPassword();
            AESCrypt aeccrypt=new AESCrypt(BeanConstants.encryptkey);
            enc=aeccrypt.encrypt(encyptiontag);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }


        String url=BeanConstants.base_url+"action=deleteaddress";
        String json = "[{\"User_ID\":\"" + "9"
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4=" + "\",\"User_Address_ID\":\"" + addresid
                + "\"}]";

        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "deleteaddress").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }


    }

    public static void DeleteResponse(String resultString) {
        //  [{"status":true,"message":"Delete Job Successfully"}]
        try {
            JSONArray mainarray=new JSONArray(resultString);
            JSONObject mainobj=mainarray.optJSONObject(0);
            Log.d("System out", "activity: " + activity);

            if (mainobj.optBoolean("status")&& mainobj.optBoolean("status") ){
                AddresslistInterface AI=(AddresslistInterface)activity;
                AI.Deleteresponse("true");

            }else{
                Toast.makeText(activity, mainobj.optString("message"), Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }

    }

    //-==============================================BookNowActivity======================================================

    public void Booking(Activity activity){
        this.activity=activity;
        String enc="";
        try {
            String encyptiontag = BeanConstants.userBeen.getEmail() + BeanConstants.userBeen.getPassword();
            String encryptkey = "AssistPrjctxyzqforencyptqwalgkey";
            AESCrypt aeccrypt = new AESCrypt(encryptkey);
            enc = aeccrypt.encrypt(encyptiontag);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }
        String url=BeanConstants.base_url+"action=AddServiceBookingDetails";

        String jsonedit = "[{\"User_ID\":\"" +"9" + "\"" + ","
                + "\"SubservicesArray\":[";

        for (int i = 0; i < BeanConstants.BookingData.getSelected_Sub_Service_ids().size(); i++) {
            if (i == BeanConstants.BookingData.getSelected_Sub_Service_ids().size() - 1) {
                jsonedit += "{\"Service_ID\":\"" + BeanConstants.BookingData.getUser_Selected_ServiceId() + "\""
                        + "," + "\"Sub_Service_ID\":\"" +BeanConstants.BookingData.getSelected_Sub_Service_ids().get(i) + "\""
                        + "}";
            } else {
                jsonedit += "{\"Service_ID\":\"" + BeanConstants.BookingData.getUser_Selected_ServiceId() + "\""
                        + "," + "\"Sub_Service_ID\":\"" +BeanConstants.BookingData.getSelected_Sub_Service_ids().get(i) + "\""+ "},";
            }

        }

        jsonedit += "]"+ "," + "\"Booking_Type\":\"" + BeanConstants.BookingData.getBooking_Type() + "\"" + ","
                + "\"Customer_Latitude\":\"" + BeanConstants.BookingData.getCustomer_Latitude() + "\"" + ","
                + "\"Customer_Longitude\":\"" + BeanConstants.BookingData.getCustomer_Longitude() + "\"" + ","
                + "\"Booking_Address\":\"" + BeanConstants.BookingData.getBooking_Address() + "\"" + ","
                + "\"Booking_Date\":\"" + BeanConstants.BookingData.getBooking_Date() + "\"" + ","
                + "\"Booking_Time\":\"" + BeanConstants.BookingData.getBooking_Time() + "\"" + ","
                + "\"Customer_Booking_Start_Time\":\"" + BeanConstants.BookingData.getCustomer_Booking_Start_Time() + "\"" + ","
                + "\"Customer_Booking_End_Time\":\"" + BeanConstants.BookingData.getCustomer_Booking_End_Time() + "\"" + ","
                + "\"Booking_Remarks\":\"" + BeanConstants.BookingData.getBooking_Remarks() + "\"" + ","
                + "\"Required_Estimation\":\"" + BeanConstants.BookingData.getRequired_Estimation() + "\"" + ","
                + "\"Booking_Quote\":\"" + ""+ "\"" + ","
                + "\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="  + "\"}]";

        Log.d("System out", "url: " + url + "\n json: " + jsonedit);

        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "AddServiceBookingDetails").execute(url, jsonedit);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }



    }

    public static void BookingResponse(String resultString) {

        try{
            JSONArray jsonArray=new JSONArray(resultString);
            JSONObject jobj=jsonArray.getJSONObject(0);
            if (jobj.has("status") && jobj.optBoolean("status")){
                BookingInteface BI=(BookingInteface)activity;
                BI.BookingResponse("true");

            }

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
        }

    }


    public void apicall(){

        String user_data="";
        user_data = "[{\"User_Email\":\"" + BeanConstants.userBeen.getEmail()
                + "\",\"User_Password\":\"" + BeanConstants.userBeen.getPassword()
                + "\"}]";


        String url=BeanConstants.base_url+"action=EditProfile";
        String json = "[{\"User_First_Name\":\"" + BeanConstants.userBeen.getUser_First_Name()
                + "\",\"User_Last_Name\":\"" + BeanConstants.userBeen.getUser_Last_Name()
                + "\",\"User_ID\":\"" + "9"
                + "\",\"User_Email\":\"" + BeanConstants.userBeen.getEmail()
                + "\",\"User_Mobile\":\"" + BeanConstants.userBeen.getPhoneNumber()
                + "\",\"User_Password\":\"" + BeanConstants.userBeen.getPassword()
                + "\",\"User_Change_Password\":\"" + BeanConstants.userBeen.getUser_Change_Password()
                + "\",\"User_Company\":\"" + BeanConstants.userBeen.getUser_Company()
                + "\",\"User_Address_Home\":\"" + BeanConstants.userBeen.getUser_Address1()
                + "\",\"User_Address_Office\":\"" + BeanConstants.userBeen.getUser_Address2()
                + "\",\"User_ZipCode_Office\":\"" + BeanConstants.userBeen.getUser_ZipCode()
                + "\",\"User_City\":\"" + BeanConstants.userBeen.getUser_City()
                + "\",\"User_DeviceType\":\"" + "android"
                + "\",\"User_Type\":\"" + "customer"
                + "\",\"User_Gender\":\"" + BeanConstants.userBeen.getUser_gender()
                + "\",\"User_Profile_Image\":\"" +imgname
                + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                + "\"}]";

        if (IsNetworkConnection.checkNetworkConnection(activity)){
            new post_async(activity, "EditProfile").execute(url, json);
        }else{
            Toast.makeText(activity,"Please check your internet",Toast.LENGTH_SHORT).show();
        }




    }

    class RetrieveFeedTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {

            dialog = new Dialog(activity,
                    android.R.style.Theme_Translucent_NoTitleBar);

            progressbar = new ProgressBar(activity);
            // progressbar.setBackgroundResource(R.drawable.progress_background);
            dialog.addContentView(progressbar, new LinearLayout.LayoutParams(50, 50));
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);

            dialog.show();

        }

        protected String doInBackground(String... urls) {
            try {

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                long timeForImgname = System.currentTimeMillis();
                imgname ="img" + timeForImgname + ".jpg";

                BeanConstants.userBeen.getImg_bitmap().compress(Bitmap.CompressFormat.JPEG, 75, bos);
                byte[] data = bos.toByteArray();
                HttpClient httpClient = new DefaultHttpClient();
                ByteArrayBody bab = new ByteArrayBody(data, imgname);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                String user_data = "";
                user_data = "[{\"User_ID\":\"" + "9"
                        + "\",\"OauthToken\":\"" + "DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="
                        + "\"}]";

                reqEntity.addPart("action", new StringBody("imageUpload"));
                reqEntity.addPart("imagePath", new StringBody("customer"));
                reqEntity.addPart("imageField", bab);
                reqEntity.addPart("json", new StringBody(user_data));

                HttpPost postRequest = new HttpPost(BeanConstants.customer_img_upload);

                postRequest.setEntity(reqEntity);
                Log.e("System out", "uper side "+reqEntity.toString()+"\n userdata: "+user_data);

                HttpResponse response = httpClient.execute(postRequest);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                response.getEntity().getContent(), "UTF-8"));

                String sResponse;
                StringBuilder s = new StringBuilder();
                Log.e("System out", "string builder ");
                while ((sResponse = reader.readLine()) != null) {
                    s = s.append(sResponse);
                }

                Log.e("System out", "image upload response: " + s.toString());
                //[{"fileName":"img1450507332556.jpg","Status":true}]

                JSONArray jsonArray=new JSONArray(s.toString());
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                if (jsonObject.getString("Status").equalsIgnoreCase("true")){
                    img=true;
                    imgname=(jsonObject.getString("fileName").trim());
                }

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        protected void onPostExecute(String result) {

            dialog.dismiss();
            if (img){
                apicall();
            }

        }
    }


    //======================================tracking==============================================


    public void GetBookingTracking(Activity activity,String BookingId) {
        this.activity=activity;
        if (IsNetworkConnection.checkNetworkConnection(activity)) {

            String enc="";
            try {
                String encyptiontag = BeanConstants.userBeen.getEmail() + BeanConstants.userBeen.getPassword();
                String encryptkey = "AssistPrjctxyzqforencyptqwalgkey";
                AESCrypt aeccrypt = new AESCrypt(encryptkey);
                enc = aeccrypt.encrypt(encyptiontag);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(activity,"error in fetching data, Please try again.",Toast.LENGTH_SHORT).show();
            }


            String url = BeanConstants.base_url+ "action=getBookingTrackingLastRecord";
            String json = "[{\"Customer_ID\":\"" + BeanConstants.userBeen.getUser_ID() +"\"," +
                    "\"User_ID\":\""+ "9"+"\"," +
                    "\"Booking_ID\":\""+BookingId+"\"," +
                    "\"OauthToken\":\""+"DIQqemi1CyHjob76T8Nd9KaSNhtcfH1W5H/3R4zNeQ4="+"\"}]";
            String url1 = url + "json=" + json;
            Log.d("System out", "     " + url1);
            new post_async(activity, "getBookingTrackingLastRecord").execute(url,json);
        } else {
            Toast.makeText(activity, "No Internet connection!", Toast.LENGTH_SHORT).show();
        }

    }

    public void ResponseTracking(String result) {
        Log.d("System out", "" + result);
        if (result.length() > 2) {

            try {
                array_trackingdata=new ArrayList<>();
                JSONArray mainarry = new JSONArray(result);
                JSONObject jobj=mainarry.getJSONObject(0);
                if (jobj.has("status") && jobj.optBoolean("status") ) {
                    if (jobj.has("data")){
                        JSONArray jar=jobj.getJSONArray("data");
                        for (int i = 0; i < jar.length(); i++) {
                            JSONObject json = jar.optJSONObject(i);
                            TrackingData trackingData = new TrackingData();
                            trackingData.setProviderId(json.optString("Provider_ID"));
                            trackingData.setProviderLatitude(json.optString("Provider_Lattitude"));
                            trackingData.setProviderLongitude(json.optString("Provider_Longitutde"));
                            trackingData.setCustomerLatitude(json.optString("Customer_Lattitude"));
                            trackingData.setCustomerLongitude(json.optString("Customer_Longitutde"));
                            trackingData.setServiceId(json.optString("Service_ID"));
                            trackingData.setServiceName(json.optString("Service_Name"));
                            trackingData.setProviderName(json.optString("Provider_FirstName") + " " + json.optString("Provider_LastName"));
                            trackingData.setBookingId(json.optString("Booking_ID"));
                            array_trackingdata.add(trackingData);
                            TrackingDetailInterface LI = (TrackingDetailInterface) activity;
                            LI.TrackingInfo(array_trackingdata);
                        }
                    }
                }else{
                    Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show();
        }

    }

}
