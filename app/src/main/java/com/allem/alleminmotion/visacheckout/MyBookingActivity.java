package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.MyBookingData;
import com.allegra.handysdk.responsebean.MyBokkingInterface;
import com.allem.alleminmotion.visacheckout.utils.MyTextView;
import com.allem.alleminmotion.visacheckout.utils.SlideHolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jsandoval on 20/06/16.
 */
public class MyBookingActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener , MyBokkingInterface {

    SlideHolder slideHolder;
    FrontBackAnimate object;
    ImageView iv_navi_heder, iv_logo_heder, iv_search_heder;
    ImageButton back;
    MyTextView tv_logotext_heder;
    RecyclerView rv_mybooking_adapter;
    RecyclerAdapter recyclerAdapter;
    public ArrayList<MyBookingData> arrayList=new ArrayList<>();
    MyTextView tv_currentbook_mybooking,tv_compltebook_mybooking;
    String Booking_status="Current";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.mybooking_activity, this);
        //init();
        ApiCall();

    }

    private void ApiCall() {
        BeanConstants.userBeen.setUser_ID(BeanConstants.loginData.getUser_ID());
        BeanConstants.service.GetBookingList(MyBookingActivity.this, Booking_status);
    }

    @Override
    public void BookingResponse(ArrayList<MyBookingData> myBookingDatas) {
        arrayList=new ArrayList<>();
        arrayList.addAll(myBookingDatas);
        recyclerAdapter = new RecyclerAdapter(MyBookingActivity.this);
        rv_mybooking_adapter.setAdapter(recyclerAdapter);
        recyclerAdapter.notifyDataSetChanged();
    }





    public void onClick(View v) {


        if(v==tv_currentbook_mybooking)
        {
            Booking_status="Current";
            ApiCall();
            tv_currentbook_mybooking.setBackgroundResource(R.drawable.tab_booking_selected);
            tv_compltebook_mybooking.setBackgroundResource(R.drawable.tab_booking_unselected);

            tv_currentbook_mybooking.setTextColor(getResources().getColor(R.color.orange));
            tv_compltebook_mybooking.setTextColor(getResources().getColor(R.color.white));
//            recyclerAdapter = new RecyclerAdapter(MyBookingActivity.this);
//            rv_mybooking_adapter.setAdapter(recyclerAdapter);
        }
        else if(v==tv_compltebook_mybooking)
        {
            Booking_status="Completed";
            ApiCall();
            tv_compltebook_mybooking.setBackgroundResource(R.drawable.tab_booking_selectedcopy);
            tv_currentbook_mybooking.setBackgroundResource(R.drawable.tab_booking_unselectedcopy);
            tv_currentbook_mybooking.setTextColor(getResources().getColor(R.color.white));
            tv_compltebook_mybooking.setTextColor(getResources().getColor(R.color.orange));
//            recyclerAdapter = new RecyclerAdapter(MyBookingActivity.this);
//            rv_mybooking_adapter.setAdapter(recyclerAdapter);
        }else if (v==iv_navi_heder){
            slideHolder.toggle();
        }
    }
    public static String formatDate (String date, String initDateFormat, String endDateFormat) throws ParseException {
        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);
        return parsedDate;
    }

    public void onMenu(View view) {
        animate();
    }

    @Override
    public void initViews(View root) {

        back = (ImageButton)root.findViewById(R.id.back_booking);
        rv_mybooking_adapter = (RecyclerView)root.findViewById(R.id.rv_mybooking_adapter);
        rv_mybooking_adapter.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyBookingActivity.this);
        rv_mybooking_adapter.setLayoutManager(layoutManager);


        tv_currentbook_mybooking=(MyTextView)root.findViewById(R.id.tv_currentbook_mybooking);
        //tv_currentbook_mybooking.setOnClickListener(this);
        tv_compltebook_mybooking=(MyTextView)root.findViewById(R.id.tv_compltebook_mybooking);
        //tv_compltebook_mybooking.setOnClickListener(this);
        onClick(root);



    }


    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
        private Activity activity;
        public ViewHolder viewHolder;
        private int lastPostion = -1;

        public RecyclerAdapter(Activity activity) {
            this.activity = activity;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            //inflate your layout and pass it to view holder
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.mybooking_cv_adapter, viewGroup, false);
            viewHolder = new ViewHolder(view);
            return viewHolder;
        }



        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {

            for (int i = 0; i < getItemCount(); i++) {
                animate_book(viewHolder.cv_mybooking_adapter, i);
            }
            if (Booking_status.equalsIgnoreCase("Current")){
                viewHolder.tv_invoice_mybooking.setVisibility(View.GONE);
                if (arrayList.get(position).getService_Name().equalsIgnoreCase("Taxi")) {
                    viewHolder.tv_detail_mybooking.setVisibility(View.VISIBLE);
                }
            }
            viewHolder.clientname.setText(arrayList.get(position).getUser_First_Name()+" "+arrayList.get(position).getUser_Last_Name());
            viewHolder.servicename.setText(arrayList.get(position).getService_Name());
            if(viewHolder.clientname.getText().toString().contains("null")){
                viewHolder.clientname.setText("No provider Assigned");
            }
            try {
                viewHolder.bookingdate.setText(formatDate(arrayList.get(position).getBooking_Date(),"yyyy-MM-dd hh:mm:ss","dd/MM/yyyy"));
                viewHolder.bookingtime.setText(formatDate(arrayList.get(position).getBooking_Time(),"yyyy-MM-dd hh:mm:ss","hh:mm:ss"));
            }catch (Exception e){
                e.printStackTrace();
            }

            viewHolder.tv_invoice_mybooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyBookingActivity.this, InvoiceActivity.class);
                    intent.putExtra("BookingId", arrayList.get(position).getBooking_ID());
                    intent.putExtra("ProviderId", arrayList.get(position).getProvider_ID());
                    startActivity(intent);
                }
            });
            viewHolder.tv_detail_mybooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MyBookingActivity.this, TrackingActivity.class);
                    intent.putExtra("bookingId", arrayList.get(position).getBooking_ID());
                    startActivity(intent);
                }
            });
        }



        private void animate_book(final View view, final int position) {
            Animation animation = AnimationUtils.loadAnimation(activity, R.anim.slidebottom);
            view.startAnimation(animation);
            lastPostion = position;
        }
        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View cv_mybooking_adapter;
            private LinearLayout ll_paymentsecond_mybooking;
            public  MyTextView tv_paymentmode_mypayment,tv_detail_mybooking,tv_invoice_mybooking,clientname,servicename,bookingdate,bookingtime;

            public ViewHolder(View convertView) {
                super(convertView);
                ll_paymentsecond_mybooking = (LinearLayout) convertView.findViewById(R.id.ll_paymentsecond_mybooking);
                tv_detail_mybooking=(MyTextView)convertView.findViewById(R.id.tv_detail_mybooking);
                tv_invoice_mybooking=(MyTextView)convertView.findViewById(R.id.tv_invoice_mybooking);
                clientname=(MyTextView)convertView.findViewById(R.id.tv_name);
                servicename=(MyTextView)convertView.findViewById(R.id.tv_service_name);
                bookingdate=(MyTextView)convertView.findViewById(R.id.tv_bookingdate);
                bookingtime=(MyTextView)convertView.findViewById(R.id.tv_bookingtime);

                cv_mybooking_adapter = convertView.findViewById(R.id.cv_mybooking_adapter);



            }
        }
    }
}