package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.InvoiceBean;
import com.allegra.handysdk.responsebean.InvoiceDetailInterface;
import com.allegra.handyuvisa.utils.SlideHolder;
import com.allegra.handyuvisa.utils.MyTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jsandoval on 20/06/16.
 */
public class InvoiceActivity extends Activity implements InvoiceDetailInterface, View.OnClickListener {
    SlideHolder slideHolder;
    ImageView iv_search_heder,iv_logo_heder,iv_navi_heder,iv_back_heder;
    MyTextView tv_logotext_heder;
    EditText ed_feedback_invoice;
    String BookingId="",provider_id="",rating_date="",Invoice_Id="";
    MyTextView provider_name,starttime,endtime,service_name,service_rate,quantity,totalprice;
    MyTextView submit_feedback;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_activity);

        init();
        BookingId =getIntent().getStringExtra("BookingId");
        provider_id=getIntent().getStringExtra("ProviderId");
        GetInvoiceDetail();


    }

    @Override
    public void onClick(View v) {
        if (v==submit_feedback){
            Keybiardupdate();
            Date date=new Date();
            rating_date=sdf.format(date);
            BeanConstants.feedback_bean.setBooking_id(BookingId);
            BeanConstants.feedback_bean.setProvider_id(provider_id);
            BeanConstants.feedback_bean.setComments(ed_feedback_invoice.getText().toString().trim());
            BeanConstants.feedback_bean.setRate("4");
            BeanConstants.feedback_bean.setRating_date(rating_date);
            BeanConstants.feedback_bean.setUser_id(BeanConstants.loginData.getUser_ID());
            BeanConstants.service.InsertFeedback(InvoiceActivity.this,Invoice_Id);
        }else if (v==iv_back_heder){
            Keybiardupdate();
            Intent intent=new Intent(InvoiceActivity.this,MyBookingActivity.class);
            startActivity(intent);
        }else if (v==iv_navi_heder){
            Keybiardupdate();
            slideHolder.toggle();
        }
    }

    private void GetInvoiceDetail() {
        BeanConstants.service.GetInvoiceDetail(InvoiceActivity.this, BookingId);

    }
    public void Keybiardupdate(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void init() {
        ed_feedback_invoice=(EditText)findViewById(R.id.ed_feedback_invoice);
        ed_feedback_invoice.clearFocus();


        submit_feedback=(MyTextView)findViewById(R.id.tv_accept_subbooking);
        submit_feedback.setOnClickListener(this);

        provider_name=(MyTextView)findViewById(R.id.Tv_providername);
        starttime=(MyTextView)findViewById(R.id.Tv_serviceStarttime);
        endtime=(MyTextView)findViewById(R.id.Tv_serviceendtime);
        service_name=(MyTextView)findViewById(R.id.tv_sevice_name);
        service_rate=(MyTextView)findViewById(R.id.tv_service_price);
        quantity=(MyTextView)findViewById(R.id.tv_quantity);
        totalprice=(MyTextView)findViewById(R.id.tv_total_serviceprice);

    }

    @Override
    public void ResponseOfInvoiceDetail(InvoiceBean invoiceBean) {
        provider_name.setText(invoiceBean.getProvider_FIrstName()+" "+invoiceBean.getProvider_FIrstName());


        //======================


        if(invoiceBean.getProvider_Booking_End_Time().length() > 0 &&invoiceBean.getProvider_Booking_Start_Time().length() > 0){

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date Start_Time = dateFormat.parse(invoiceBean.getProvider_Booking_Start_Time());
                Date End_Time = dateFormat.parse(invoiceBean.getProvider_Booking_End_Time());
                long different = End_Time.getTime() - Start_Time.getTime();
                System.out.println("startDate : " + Start_Time);
                System.out.println("endDate : "+ End_Time);
                System.out.println("different : " + different);

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                long elapsedSeconds = different / secondsInMilli;

                System.out.printf(
                        "%d days, %d hours, %d minutes, %d seconds%n",
                        elapsedDays,
                        elapsedHours, elapsedMinutes, elapsedSeconds);

                if (elapsedHours > 0){

                    if(elapsedMinutes > 0 && elapsedMinutes < 60){
                        quantity.setText(elapsedHours+":"+ elapsedMinutes + " hours");
                    }else{
                        quantity.setText(elapsedHours + " hours");
                    }

                }else{
                    quantity.setText("0 hour");
                }
            }catch (Exception e){
                e.printStackTrace();
            }




        }else {
            quantity.setText("0 hour");
        }

        service_name.setText(invoiceBean.getService_Name());
        service_rate.setText("");

        totalprice.setText("$"+invoiceBean.getInvoice_Amount());
        Invoice_Id=invoiceBean.getInvoice_ID();
        ed_feedback_invoice.setText(invoiceBean.getInvoice_Feedback());
    }

    @Override
    public void ResponseOfInvoiceDetailfeedback() {
        Intent intent=new Intent(InvoiceActivity.this,MyBookingActivity.class);
        startActivity(intent);
    }

    public static String formatDate (String date, String initDateFormat, String endDateFormat) throws ParseException {
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);
        return parsedDate;
    }


}
