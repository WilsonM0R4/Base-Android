package com.allegra.handyuvisa;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.allegra.handyuvisa.utils.Util;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelsPaymentActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.activity_hotels_payment, this);

    }
    public void onMenu(View view) {
        animate();
    }


    @Override
    public void initViews(View root) {
        TextView hotelName= (TextView) root.findViewById(R.id.payment_hotel_name);
        TextView hotelAddressT= (TextView) root.findViewById(R.id.payment_hotel_address);
        TextView roomsN= (TextView) root.findViewById(R.id.payment_hotel_room_number);
        TextView value= (TextView) root.findViewById(R.id.payment_hotel_value);
        TextView taxes_fees= (TextView) root.findViewById(R.id.payment_hotel_taxes_fees_values);
        TextView totalT= (TextView) root.findViewById(R.id.payment_hotel_total_value);
        TextView chDay= (TextView) root.findViewById(R.id.payment_hotel_day);
        TextView chDayN= (TextView) root.findViewById(R.id.payment_hotel_day_number);
        TextView chMonth= (TextView) root.findViewById(R.id.payment_hotel_month);
        TextView chYear= (TextView) root.findViewById(R.id.payment_hotel_year);
        TextView choDay = (TextView) root.findViewById(R.id.payment_hotel_checkout_day);
        TextView choDayN = (TextView) root.findViewById(R.id.payment_hotel_checkout_day_number);
        TextView choMonth = (TextView) root.findViewById(R.id.payment_hotel_checkout_month);
        TextView choYear = (TextView) root.findViewById(R.id.payment_hotel_checkout_year);
        TextView descriptionT = (TextView) root.findViewById(R.id.payment_hotel_description);


        String name= getIntent().getStringExtra("hotelName");
        String hotelAddress= getIntent().getStringExtra("hotelAddress");
        String roomType= getIntent().getStringExtra("roomType");
        String amount= getIntent().getStringExtra("amount");
        String tax= getIntent().getStringExtra("tax");
        String fees= getIntent().getStringExtra("fees");
        String total= getIntent().getStringExtra("total");
        String currency= getIntent().getStringExtra("currency");
        String merchantId= getIntent().getStringExtra("merchantId");
        String reference= getIntent().getStringExtra("reference");

        String checkin= PreferenceManager.getDefaultSharedPreferences(this).getString("checkinDate", "-");
        String checkout= PreferenceManager.getDefaultSharedPreferences(this).getString("checkoutDate", "-");
        int adult1= PreferenceManager.getDefaultSharedPreferences(this).getInt("adultHotel1", 0);
        int child1= PreferenceManager.getDefaultSharedPreferences(this).getInt("childHotel1", 0);
        int rooms= PreferenceManager.getDefaultSharedPreferences(this).getInt("roomsNumber", 0);
        int guest= adult1+child1;

        Log.d("Fecha ci", checkin);
        Log.d("Fecha co", checkout);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date checkinDate = new Date();
        Date checkoutDate = new Date();
        try {

            checkinDate = dateFormat.parse(checkin);
            checkoutDate= dateFormat.parse(checkout);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days=Days.daysBetween(new DateTime(checkinDate), new DateTime(checkoutDate)).getDays();

        String roomsDescription=rooms+" "+getResources().getString(R.string.payment_hotel_room);
        String description=guest+" "+getResources().getString(R.string.payment_hotel_guest)+" | "+roomsDescription+" | "+days+" "+getString(R.string.payment_hotel_nights) ;

        descriptionT.setText(description);
        Double taxD= Double.parseDouble(tax);
        Double feesD= Double.parseDouble(fees);
        Double tax_fees_t=taxD+feesD;
        roomsN.setText(roomsDescription);


        chDay.setText(Util.DOW_Formatter.format(checkinDate).toUpperCase());
        chDayN.setText(Util.D_Formatter.format(checkinDate).toUpperCase());
        chMonth.setText(Util.M_Formatter.format(checkinDate).toUpperCase());
        chYear.setText("/ "+Util.Y_Formatter.format(checkinDate).toUpperCase());

        choDay.setText(Util.DOW_Formatter.format(checkoutDate).toUpperCase());
        choDayN.setText(Util.D_Formatter.format(checkoutDate).toUpperCase());
        choMonth.setText(Util.M_Formatter.format(checkoutDate).toUpperCase());
        choYear.setText("/ "+Util.Y_Formatter.format(checkoutDate).toUpperCase());

        value.setText(currency+" "+amount);
        totalT.setText(currency+" "+total);
        taxes_fees.setText(currency+" "+tax_fees_t);
        hotelName.setText(name);
        hotelAddressT.setText(hotelAddress);
    }
}
