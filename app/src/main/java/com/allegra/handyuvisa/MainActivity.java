package com.allegra.handyuvisa;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.Util;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements com.allegra.handyuvisa.BackFragment.MenuSelectListener {

    private static final String TAG = "MainActivity";
    public static final String WEB_FRAGMENT_INDICATOR = "web_fragment";
    private com.allegra.handyuvisa.FrontFragment frontFragment;
    private com.allegra.handyuvisa.BackFragment backFragment;
    private Fragment currentFrontFragment; //will manage all fragments in the app
    public static final String TAG_FRONT = "front_fragment";
    private boolean isLogin;
    private boolean tutorial=true;
    private CustomizedTextView login, register;
    public int state = 0; // 0 - front open + back exposed; // 1 - front close + back hidden;
    public static final int menu_exposed = 1;
    public static final int menu_hidden = 0;
    public FragmentMain fragmentMain;



    //OVERRIDE METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(null);

        /* APPSFLYER */
        AppsFlyerLib.getInstance().startTracking(this.getApplication(),"55GLA2WaeHkqjapkFYTmZY");//AppFlyer Dev Key jsandoval@iatai.com
        AppsFlyerLib.getInstance().setCollectIMEI(true);//Get de Imei of the Phone
        AppsFlyerLib.getInstance().setCollectAndroidID(true);//Get the Android ID of the OS Phone.
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        /* APPSFLYER */

        //int  what  = Build.VERSION.SDK_INT;
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            //int uiOptions = (toShow) ? View.SYSTEM_UI_FLAG_VISIBLE : View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/



            //frontFragment = (com.allegra.handyuvisa.FrontFragment) getFragmentManager().findFragmentById(R.id.fragment_top);
            backFragment = (com.allegra.handyuvisa.BackFragment) getFragmentManager().findFragmentById(R.id.fragment_bottom);
            backFragment.menulistener = this;
            state = 0;

            fragmentMain = new FragmentMain();

            currentFrontFragment = fragmentMain;

            state = menu_hidden;

            /*getFragmentManager().beginTransaction()
                    .remove(getFragmentManager().findFragmentById(R.id.fragment_top))
                    .commit();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_top, currentFrontFragment, TAG_FRONT)
                    .commit();*/

            getFragmentManager().beginTransaction().
                    add(R.id.container_main, fragmentMain, TAG_FRONT)
                    .commit();

            getSupportActionBar().hide();

            /*if (!isAuthenticated()) {
                login = (CustomizedTextView) findViewById(R.id.login);
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        //startActivityForResult(intent, Constants.ACTIVITY_LOGIN);

                        LoginActivity fragmentLogin = new LoginActivity();

                        replaceLayout(fragmentLogin, false);
                    }
                });

                register = (CustomizedTextView) frontFragment.getView().findViewById(R.id.register);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, com.allegra.handyuvisa.LoginNewUser.class);
                        startActivity(intent);
                    }
                });
            }*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        View guest = findViewById(R.id.guest_controls_group);
        View user = findViewById(R.id.login_controls_group);
        if (Util.isAuthenticated(this) && guest != null ||
                !Util.isAuthenticated(this) && user != null) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            state = 1;
            //animate();
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*if (resultCode == RESULT_OK) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }*/
    //}


    //Interface MenuSelectListener's OVERRIDE METHODS
    @Override
    public void getStartActivity(Intent intent) {
        startActivity(intent);
        overridePendingTransition(R.animator.front_slide_in, R.animator.back_slide_out);
        state = 1;
    }

    //PROPER METHODS

    public void ClickMe(View view) {
        animate();
    }

    public void onSkipIntro(View view) {
        animate();
    }

    private boolean isAuthenticated() {
        return (((com.allegra.handyuvisa.VisaCheckoutApp)getApplication()).getIdSession() != null);
    }

    public void replaceLayout(final Fragment fragment, boolean goBack){

        /*Log.e(TAG, "replaceLayout: currentFront: " + currentFrontFragment.toString()
                + " fragment: " + fragment.toString() + " isBack: " + goBack);*/

        Log.e(TAG,"before if");

        if(state == menu_exposed){
            animate();
            Log.e(TAG, "menu is exposed");
            if(!isSameFragment(this.currentFrontFragment, fragment)){
                currentFrontFragment = fragment;

                Log.e(TAG, "is same fragment");

                Log.e(TAG, "currentFront:"+currentFrontFragment.toString()
                        +" theOtherFragment: "+fragment);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {

                        getFragmentManager().beginTransaction()
                                .setCustomAnimations(R.animator.slide_in_left,
                                        R.animator.slide_out_right)
                                .remove(getFragmentManager().findFragmentByTag(TAG_FRONT))
                                .add(R.id.container_main, fragment, TAG_FRONT)
                                .commit();

                        //
                    }
                }, 100);
            }

        } else {
            Log.e(TAG, "menu is hidden");
            Log.e(TAG, "is not same fragment");

            Log.e(TAG, "currentFront:"+currentFrontFragment.toString()
                    +" theOtherFragment: "+fragment);

            int first_animation = R.animator.slide_in_left,
                    second_animation = R.animator.slide_out_right;

            if(goBack){
                first_animation = R.animator.slide_in_right;
                second_animation = R.animator.slide_out_left;
            }

            getFragmentManager().beginTransaction()
                    .setCustomAnimations(first_animation, second_animation)
                    .remove(getFragmentManager().findFragmentByTag(TAG_FRONT))
                    .add(R.id.container_main, fragment, TAG_FRONT)
                    .commit();


            currentFrontFragment = fragment;

            Log.e(TAG, "currentFront:"+currentFrontFragment.toString()
                    +" theOtherFragment: "+fragment);

        }

    }

    public void animate() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        int dens=dm.densityDpi;
        double wi=(double)width/(double)dens;
        double hi=(double)height/(double)dens;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi, 2);
        //double screenInches = Math.sqrt(x + y);
        //Toast.makeText(this,  "w: " + width + " h: " + height, Toast.LENGTH_SHORT).show();

        //Log.d("Width",width+"");
        int resId = R.animator.front_open;
        if (width > 800) {
            resId = R.animator.front_open_xlarge;
        }

        getFragmentManager().beginTransaction().hide(currentFrontFragment).hide(backFragment).commit();

        if(currentFrontFragment.getView() != null){
            View frontParentView = currentFrontFragment.getView().findViewById(R.id.container_main);

            if(frontParentView != null){
                frontParentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animate();
                    }
                });
            }else{
                currentFrontFragment.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animate();
                    }
                });
            }

        }

        if (state == menu_hidden) {//Open
            state = menu_exposed;
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(resId, 0)
                    .show(currentFrontFragment)
                    .setCustomAnimations(R.animator.back_exposed, 0)
                    .show(backFragment)
                    .commit();
        } else {//Close
            state = menu_hidden;
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.front_close, 0)
                    .show(currentFrontFragment)
                    .setCustomAnimations(R.animator.back_hidden, 0)
                    .show(backFragment)
                    .commit();
        }

    }

    private boolean isSameFragment(Fragment current, Fragment next) {
        String currentName = "", nextName = "";
        String[] groups = current.toString().split("\\{");
        String currentIndicator = "", secondIndicator = "";

        //work with tags

        if (groups.length > 0) {
            currentName = groups[0];
        }

        groups = next.toString().split("\\{");
        if (groups.length > 0) {
            nextName = groups[0];
        }

        /*if (currentName.equals(nextName)) {
            return true;
        } else {
            return false;
        }*/

        if(currentName.equals("WebFragment")){
            currentIndicator = current.getArguments().getString(WebFragment.WEB_TITLE);
        }

        if(nextName.equals("WebFragment")){
            secondIndicator = next.getArguments().getString(WebFragment.WEB_TITLE);
        }

        return (currentName.equals(nextName) && currentIndicator.equals(secondIndicator)) ;
    }


    public void onHome() {
        fragmentMain.replaceLayout(frontFragment, true);
    }

    public void onBack(){
        super.onBackPressed();
    }

    public void statusBarVisibility(boolean isInitialView){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            //int uiOptions = (toShow) ? View.SYSTEM_UI_FLAG_VISIBLE : View.SYSTEM_UI_FLAG_FULLSCREEN;

            if(isInitialView){
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }else{
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
            }

        }

    }


}
