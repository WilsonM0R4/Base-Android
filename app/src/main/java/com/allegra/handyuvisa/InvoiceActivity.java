package com.allegra.handyuvisa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;


import com.allegra.handyuvisa.utils.SlideHolder;
import com.allegra.handyuvisa.utils.MyTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jsandoval on 20/06/16.
 */
public class InvoiceActivity extends Activity implements View.OnClickListener {
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

        }else if (v==iv_back_heder){
            Keybiardupdate();
           // Intent intent=new Intent(InvoiceActivity.this,MyBookingActivity.class);
           // startActivity(intent);
        }else if (v==iv_navi_heder){
            Keybiardupdate();
            slideHolder.toggle();
        }
    }

    private void GetInvoiceDetail() {


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



    public void ResponseOfInvoiceDetailfeedback() {
      // Intent intent=new Intent(InvoiceActivity.this,MyBookingActivity.class);
       // startActivity(intent);
    }

    public static String formatDate (String date, String initDateFormat, String endDateFormat) throws ParseException {
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);
        return parsedDate;
    }


}
