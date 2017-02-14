package com.allegra.handyuvisa;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
//import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.allegra.handyuvisa.utils.MyTextView;
import com.allegra.handyuvisa.utils.SlideHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by jsandoval on 20/06/16.
 */

public class HomeActivity_Handy extends FrontBackAnimate implements  FrontBackAnimate.InflateReadyListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SlideHolder slideHolder;
    MyTextView tabOne,tabTwo,tabThree;
    ImageView tabImg;
    ImageButton booking;
    Context context;
    static int selectedpos=0,sizeofmain=0, totalAddFrag, countTaxi = 0;
    ArrayList<ImageView> arrayofimg=new ArrayList<>();
    static String servicename="",serviceid="";
    static int height,width, speci,speciwidth,h;
    DisplayMetrics display;
    private LocationManager locationManager;
    private boolean isGPSEnabled, isEnterToTaxi = true;
    private boolean isNetworkEnabled;
    private Location location;
    Boolean intentflag=false;
    String SerString="", strOptionSelected = "", strTaxi = "", strRestaurants ="", strServices="" ;
    MyTextView tv_service_homefra_new;
    Fragment home;
    Boolean search_flag=false;
    ImageView leftNav,rightNav;

    //**************   OVERRIDE METHODS  **************
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.homeactivity_handy,this);
        context = this;
        display =HomeActivity_Handy.this.getResources().getDisplayMetrics();
        Intent i = getIntent();
        //getResources().getString(R.string.mystring);
        strTaxi = getResources().getString(R.string.taxi);
        strRestaurants = getResources().getString(R.string.title_restaurants);
        strServices = getResources().getString(R.string.services);

        strOptionSelected = i.getStringExtra("option");
        Log.d("strTaxiprueba", strTaxi);
        Log.d("strOptionSelectedprueba", strOptionSelected);
        if (strOptionSelected.equals(strTaxi))Log.d("strTaxi", strTaxi);
        if (strOptionSelected.equals(strRestaurants))Log.d("strRestaurants", strRestaurants);
        if (strOptionSelected.equals(strServices))Log.d("strServices", strServices);

        width = display.widthPixels;
        height = display.heightPixels;
        speci = (height * 23) / 100;
        speciwidth = (width * 23) / 100;
        h = (height * 13) / 100;
        //init();

        getLocation();
    }





    @Override
    public void initViews(View root) {

        slideHolder=(SlideHolder)root.findViewById(R.id.drawer_home_layout);
        tv_service_homefra_new=(MyTextView)root.findViewById(R.id.tv_service_homefra_new);
        booking = (ImageButton)root.findViewById(R.id.btn_booking);



        tv_service_homefra_new = (MyTextView)root.findViewById(R.id.tv_service_homefra_new);

        leftNav = (ImageView) root.findViewById(R.id.left_nav);
        rightNav = (ImageView) root.findViewById(R.id.right_nav);

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

    //**************   PROPER METHODS  **************


    public Location getLocation() {
        try {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                buildAlertMessageNoGps();
            } else {
                if (intentflag){

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

    public void Keybiardupdate(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }





    public void onMenu(View view) {
        animate();
    }

    //****************************  INNER CLASSES  ************************

    /** This class manage ViewPager's behavior, which contains sub services for each category */
        class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        //private  ArrayList<MainCategoryData> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
            // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            Log.d("Sergio getItem",String.valueOf(mFragmentList.get(position)));
                Log.d("tambien llega",String.valueOf(mFragmentList.get(position)));
                return mFragmentList.get(position);
        }

            // Returns total number of pages
        @Override
        public int getCount() {
            Log.d("Sergio getCount",String.valueOf(mFragmentList.size()));
            return mFragmentList.size();
        }



    }

}

