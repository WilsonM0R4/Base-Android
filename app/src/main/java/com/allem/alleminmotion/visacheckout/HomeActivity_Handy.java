package com.allem.alleminmotion.visacheckout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.MainCategoryData;
import com.allegra.handysdk.responsebean.CategoryCallInterface;
import com.allegra.handysdk.utilsclasses.Const;
import com.allem.alleminmotion.visacheckout.utils.MyTextView;
import com.allem.alleminmotion.visacheckout.utils.SlideHolder;

import java.util.ArrayList;

/**
 * Created by jsandoval on 20/06/16.
 */
public class HomeActivity_Handy extends FragmentActivity implements CategoryCallInterface {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SlideHolder slideHolder;
    ImageView iv_navi_heder,iv_logo_heder,iv_search_heder;
    EditText ed_search_header;
    MyTextView tv_go_header,tv_logotext_heder;
    MyTextView tabOne,tabTwo,tabThree;
    ImageView tabImg;
    static int selectedpos=0,sizeofmain=0;
    ArrayList<MainCategoryData> mainCategory;
    ArrayList<ImageView> arrayofimg=new ArrayList<>();
    static String servicename="",serviceid="";
    static int height,width, speci,speciwidth,h;
    DisplayMetrics display;
    private LocationManager locationManager;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private Location location;
    Boolean intentflag=false;
    String SerString="";
    MyTextView tv_service_homefra_new;
    Fragment home;
    Boolean search_flag=false;
    ImageView leftNav,rightNav;
    TextView headerTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity_handy);
        display =HomeActivity_Handy.this.getResources().getDisplayMetrics();
        width = display.widthPixels;
        height = display.heightPixels;
        speci = (height * 23) / 100;
        speciwidth = (width * 23) / 100;
        h = (height * 13) / 100;

        init();
        CallApi();
        getLocation();


    }

    private void CallApi() {
        servicename="";
        if(Const.IsInternetConnectionFound(this)){
            BeanConstants.userBeen.setEmail(BeanConstants.loginData.getUser_Email());
            BeanConstants.userBeen.setUser_ID(BeanConstants.loginData.getUser_ID());
            BeanConstants.service.Categorycall(HomeActivity_Handy.this, SerString);

        }else{
            Toast.makeText(HomeActivity_Handy.this, getResources().getString(R.string.nointernet), Toast.LENGTH_LONG).show();
        }

    }

    public Location getLocation() {
        try {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                buildAlertMessageNoGps();
            } else {
                if (intentflag){
                    Intent intent=new Intent(HomeActivity_Handy.this,MapActivity.class);
                    intent.putExtra("serviceid",serviceid);
                    startActivity(intent);
                }
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                "Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void init() {

        headerTxt = (TextView)findViewById(R.id.txt_handy);
        Intent i = getIntent();
        String strTaxi = i.getStringExtra("Taxi");
        String strRestaurants=i.getStringExtra("Restaurantes");;
        String strServices=i.getStringExtra("Servicios");;

        boolean booltaxi, boolrestaurant, boolservice;

        booltaxi = Boolean.parseBoolean(strTaxi);
        boolrestaurant = Boolean.parseBoolean(strRestaurants);
        boolservice = Boolean.parseBoolean(strServices);

        if(booltaxi){
                headerTxt.setText(strTaxi);
        }else if(boolrestaurant){
                headerTxt.setText(strRestaurants);
        }else if(boolservice){
                headerTxt.setText(strServices);
        }

        BeanConstants.userBeen.setEmail("user@demo.com");
        BeanConstants.userBeen.setPassword("123456");
        BeanConstants.service.Categorycall(HomeActivity_Handy.this,SerString);
        tv_service_homefra_new = (MyTextView) findViewById(R.id.tv_service_homefra_new);

        leftNav = (ImageView) findViewById(R.id.left_nav);
        rightNav = (ImageView) findViewById(R.id.right_nav);

        leftNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager != null) {
                    int tab = viewPager.getCurrentItem();
                    if (tab > 0) {
                        leftNav.setVisibility(View.VISIBLE);
                        tab--;
                        viewPager.setCurrentItem(tab);
                    } else if (tab == 0) {
                        leftNav.setVisibility(View.INVISIBLE);
                        viewPager.setCurrentItem(tab);
                    }
                }

            }
        });

        rightNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager!=null) {
                    int tab = viewPager.getCurrentItem();
                    tab++;
                    viewPager.setCurrentItem(tab);
                    rightNav.setVisibility(View.VISIBLE);
                    if (sizeofmain<=tab){
                        rightNav.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void CategoryData(ArrayList<MainCategoryData> mainCategoryDatas) {

    }

    @Override
    public void setdata(String selected, ArrayList<String> Sub_id) {

    }

    public void onMenu(View view) {
    }
}
