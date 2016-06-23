package com.allem.alleminmotion.visacheckout;

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
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.MainCategoryData;
import com.allegra.handysdk.responsebean.CategoryCallInterface;
import com.allegra.handysdk.utilsclasses.Const;
import com.allem.alleminmotion.visacheckout.utils.MyTextView;
import com.allem.alleminmotion.visacheckout.utils.SlideHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by jsandoval on 20/06/16.
 */

public class HomeActivity_Handy extends FrontBackAnimate implements CategoryCallInterface, FrontBackAnimate.InflateReadyListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SlideHolder slideHolder;
    MyTextView tabOne,tabTwo,tabThree;
    ImageView tabImg;
    ImageButton booking;
    Context context;
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
    String SerString="", strOptionSelected = "";
    MyTextView tv_service_homefra_new;
    Fragment home;
    Boolean search_flag=false;
    ImageView leftNav,rightNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.homeactivity_handy,this);
        context = this;
        display =HomeActivity_Handy.this.getResources().getDisplayMetrics();
        Intent i = getIntent();
        String strTaxi = String.valueOf(R.string.taxi);
        String strRestaurants = String.valueOf(R.string.title_restaurants);
        String strServices = String.valueOf(R.string.services);

        strOptionSelected = i.getStringExtra("option");

        if (strOptionSelected.equals(strTaxi))Log.d("strTaxi", strTaxi);
        if (strOptionSelected.equals(strRestaurants))Log.d("strRestaurants", strRestaurants);
        if (strOptionSelected.equals(strServices))Log.d("strServices", strServices);

        width = display.widthPixels;
        height = display.heightPixels;
        speci = (height * 23) / 100;
        speciwidth = (width * 23) / 100;
        h = (height * 13) / 100;
        //init();
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

    @Override
    public void CategoryData(ArrayList<MainCategoryData> mainCategoryData) {

        sizeofmain=0;
        mainCategory=new ArrayList<>();
        if (mainCategoryData.size()>0){
            sizeofmain=mainCategoryData.size();
            mainCategory=mainCategoryData;

            viewPager = (ViewPager) findViewById(R.id.pager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons(mainCategoryData);
        }

    }

    public void Keybiardupdate(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void setdata(String totalnum,final ArrayList<String> Childservicelist) {
        tv_service_homefra_new.setText(totalnum);

        if (totalnum.trim().length()>0 && Integer.parseInt(totalnum.replace(" Service(s) selected",""))>0){
            tv_service_homefra_new.setVisibility(View.VISIBLE);
        }else{
            tv_service_homefra_new.setVisibility(View.GONE);
        }
        tv_service_homefra_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Keybiardupdate();
                if (Childservicelist.size()>0){
                    intentflag=true;
                    serviceid=mainCategory.get(selectedpos).getService_ID();
                    BeanConstants.BookingData.setUser_Selected_Service(mainCategory.get(selectedpos).getService_Name());
                    BeanConstants.BookingData.setUser_Selected_ServiceId(mainCategory.get(selectedpos).getService_ID());
                    BeanConstants.BookingData.setUser_ID(BeanConstants.loginData.getUser_ID());
                    BeanConstants.BookingData.setSelected_Sub_Service_ids(Childservicelist);
                    getLocation();
                }else{
                    Toast.makeText(HomeActivity_Handy.this,"Please select atlest one service",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void setupTabIcons(ArrayList<MainCategoryData> mainCategoryDatas) {

        arrayofimg.clear();
        for (int i=0;i<mainCategoryDatas.size();i++){

            tabImg=(ImageView) LayoutInflater.from(this).inflate(R.layout.custom_img, null);
            if (i==selectedpos){
                Picasso.with(HomeActivity_Handy.this).load(BeanConstants.customer_image_category2+mainCategoryDatas.get(i).getService_Image_thumb()+"&h="+h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(tabImg);
            }else{
                Picasso.with(HomeActivity_Handy.this).load(BeanConstants.customer_image_category+mainCategoryDatas.get(i).getService_Image1()+"&h="+h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(tabImg);
            }
            arrayofimg.add(tabImg);
            tabLayout.getTabAt(i).setCustomView(tabImg);
            tabOne = (MyTextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabOne.setText(mainCategoryDatas.get(i).getService_Name()+"\n");
            tabOne.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tabOne.setCompoundDrawablePadding(15);
            tabOne.setId(i);
            tabLayout.getTabAt(i).setCustomView(tabOne);
        }
    }

    private void setupViewPager(final ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        for (int i=0;i<sizeofmain;i++){
            adapter.addFrag(new HomeFragment(),mainCategory);
        }
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectedpos = position;
                Log.d("Size arrayofimg", String.valueOf(arrayofimg.size()));
                for (int i = 0; i < arrayofimg.size(); i++) {
                    if (i == position) {
                        Picasso.with(HomeActivity_Handy.this).load(BeanConstants.customer_image_category2 + mainCategory.get(i).getService_Image_thumb() + "&h=" + h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(arrayofimg.get(i));
                        Log.d("Sergio", "Entra a i==position");
                    } else {
                        Picasso.with(HomeActivity_Handy.this).load(BeanConstants.customer_image_category + mainCategory.get(i).getService_Image1() + "&h=" + h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(arrayofimg.get(i));
                        Log.d("Sergio", "No entra a i==position");
                        Log.d("Sergio", String.valueOf(BeanConstants.customer_image_category + mainCategory.get(i).getService_Image1() + "&h=" + h));
                    }
                }
                viewPager.getAdapter().notifyDataSetChanged();
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                viewPager.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void onMenu(View view) {
        animate();
    }

    @Override
    public void initViews(View root) {
        slideHolder=(SlideHolder)root.findViewById(R.id.drawer_home_layout);
        tv_service_homefra_new=(MyTextView)root.findViewById(R.id.tv_service_homefra_new);
        booking = (ImageButton)root.findViewById(R.id.btn_booking);
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MyBookingActivity.class);
                context.startActivity(intent);
            }
        });


       /* boolean booltaxi, boolrestaurant, boolservice;

        booltaxi = Boolean.parseBoolean(strTaxi);
        boolrestaurant = Boolean.parseBoolean(strRestaurants);
        boolservice = Boolean.parseBoolean(strServices);

        if(booltaxi){
                headerTxt.setText(strTaxi);
        }else if(boolrestaurant){
                headerTxt.setText(strRestaurants);
        }else if(boolservice){
                headerTxt.setText(strServices);
        }*/

        BeanConstants.userBeen.setEmail("user@demo.com");
        BeanConstants.userBeen.setPassword("123456");
        BeanConstants.service.Categorycall(HomeActivity_Handy.this,SerString);
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


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private  ArrayList<MainCategoryData> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment,ArrayList<MainCategoryData> title) {
            home=fragment;
            mFragmentList.add(fragment);
            mFragmentTitleList=title;

        }

        @Override
        public int getItemPosition(Object object) {
            if (search_flag){
                search_flag=false;
                for (int i = 0; i < arrayofimg.size(); i++) {
                    if (i == selectedpos) {
                        Picasso.with(HomeActivity_Handy.this).load(BeanConstants.customer_image_category2 + mainCategory.get(i).getService_Image_thumb() + "&h=" + h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(arrayofimg.get(i));
                    } else {
                        Picasso.with(HomeActivity_Handy.this).load(BeanConstants.customer_image_category + mainCategory.get(i).getService_Image1() + "&h=" + h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(arrayofimg.get(i));
                    }
                }
            }
            if (object instanceof HomeFragment) {
                ((HomeFragment) object).Refreshadapter(HomeActivity_Handy.this,selectedpos,servicename);
            }

            leftNav.setVisibility(View.VISIBLE);
            rightNav.setVisibility(View.VISIBLE);
            if (selectedpos<=0){
                leftNav.setVisibility(View.INVISIBLE);
            }
            if ((selectedpos+1)==sizeofmain){
                rightNav.setVisibility(View.INVISIBLE);
            }

            return super.getItemPosition(object);
        }

    }




}

