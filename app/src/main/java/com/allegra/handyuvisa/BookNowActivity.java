package com.allegra.handyuvisa;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.allegra.handyuvisa.utils.MyEditText;
import com.allegra.handyuvisa.utils.MyTextView;

/**
 * Created by jsandoval on 20/06/16.
 */
public class BookNowActivity extends Activity implements View.OnClickListener{

    MyTextView tvConfirmBooking,ed_name,ed_service,ed_estimatetime,ed_address;
    ImageView iv_navi_heder,iv_logo_heder,iv_search_heder,iv_back_heder;
    MyTextView tv_logotext_heder;
    LinearLayout llmain;
    MyEditText ed_extra_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booknow_activity);

        init();


        ed_estimatetime.setText(getIntent().getStringExtra("Estimate_Time"));

    }

    private void init() {

        ed_name=(MyTextView)findViewById(R.id.ed_name);
        ed_service=(MyTextView)findViewById(R.id.ed_service);
        ed_estimatetime=(MyTextView)findViewById(R.id.ed_estimatetime);
        ed_address=(MyTextView)findViewById(R.id.ed_address);
        ed_extra_notes=(MyEditText)findViewById(R.id.ed_extra_notes);

        tvConfirmBooking=(MyTextView)findViewById(R.id.tv_confirm_booking);
        tvConfirmBooking.setOnClickListener(this);


        ed_extra_notes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.ed_extra_notes) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(tvConfirmBooking==v){
            Keybiardupdate();
            selectPaymentOption();
        }
    }

    public void Keybiardupdate(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void selectPaymentOption() {
        final Dialog dialog = new Dialog(BookNowActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.paynowdialogue);

        final Button btn_payment_Now = (Button) dialog.findViewById(R.id.btn_payment_nnn);
        final LinearLayout btn_payment_later = (LinearLayout) dialog.findViewById(R.id.btn_payment_lll);
        final Button btn_later = (Button) dialog.findViewById(R.id.btn_later);
        final ImageView img_close_payment=(ImageView)dialog.findViewById(R.id.img_close_payment);


        img_close_payment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        /*btn_payment_Now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });*/
        btn_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }



}

