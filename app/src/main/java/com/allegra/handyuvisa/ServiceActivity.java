package com.allegra.handyuvisa;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.CustomizedTextView;
import com.allegra.handyuvisa.utils.GPSTracker;
import com.allem.onepocket.utils.OPKConstants;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * Created by Sergio Farfan on 6/06/16.
 */
public class ServiceActivity extends  FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webServices;
    private ProgressBar progressBar;
    private ImageButton arrowBack, arrowF;
    private ImageButton menu;
    private String returnURL, userNombre, mcard, userMail;
    public String onePocketmessage, TAG = "ServiceActivity";
    private String url;
    private LocationManager locationManager;
    private boolean isGPSEnabled, isNetworkEnabled, enterToGetLocation;
    private Location location;
    GPSTracker gp;
    Double latitude, longitude;
    String mLat, mLong;
    Dialog dialog;
    //GoogleApiClient mGoogleApiClient;
    public static final int REQUEST_LOCATION_CODE = 1234, MY_PERMISSIONS_REQUEST_LOCATION = 4563;
    String[] PERMISSIONS = { Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    //************************************OVERRIDE METHODS*****************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.activity_services, this);
        AllemUser user = Constants.getUser(this);
        userNombre = user.nombre;
        userMail = user.email;
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);
        mcard = prefs.getString("numMcard", "NO_TIENE");
        if(mcard.equals("NO_TIENE")){
            mcard = "no";
        }
        else {
            mcard = "yes";
        }
        //Log.d(TAG, mcard);
    }

    @Override
    public void initViews(View root) {

        url = Constants.getServiceUrl()+"&email="+userMail+"&v=1"+"&idPortal="+Constants.ID_PORTAL;
        menu = (ImageButton) root.findViewById(R.id.menu_image);
        webServices = (WebView) root.findViewById(R.id.webView3);
        webServices.getSettings().setJavaScriptEnabled(true);
        webServices.getSettings().setBuiltInZoomControls(true);
        webServices.getSettings().setGeolocationEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webServices.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webServices.setWebChromeClient(new GeoWebChromeClient());
        webServices.loadUrl(url);
        webServices.setWebViewClient(new MyBrowser(this));
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_services);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_services);
        progressBar = (ProgressBar) root.findViewById(R.id.progressBarServices);

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            onGoBack(v);
            }
        });
        arrowF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoForward(v);
            }
        });
        getLocation();
       // Log.d(TAG, url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            //Validate permissions
            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermissions(this, PERMISSIONS)){
                ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                getCurrentLocation();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted, yay! Do the camera-related task you need to do.
                   // Log.e(TAG, "El permiso fue dado");
                    getCurrentLocation();
                } else {
                    // Permission denied, boo! Disable the functionality that depends on this permission.
                   // Log.e(TAG, "Sin el permiso no podemos seguir");
                }
                return;
            }

            // Other 'case' lines to check for other permissions this app might request
        }
    }

    //************************************PROPER METHODS**************************************

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                R.string.location_permision)
                .setCancelable(false)
                .setPositiveButton(R.string.location_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),REQUEST_LOCATION_CODE);
                            }
                        })
                .setNegativeButton(R.string.location_no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if ( context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

   public Location getLocation() {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
               // Log.d(TAG, "ESTOY ACA");
                buildAlertMessageNoGps();
            } else {
                //Validate permissions
                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermissions(this, PERMISSIONS)){
                    ActivityCompat.requestPermissions(this, PERMISSIONS, MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    getCurrentLocation();
                }

            }
        return location;
    }

    private void getCurrentLocation () {

        gp = new GPSTracker(this);
        latitude = gp.getLatitude();
        longitude = gp.getLongitude();

        if (latitude != 0.0) {

            try {
                enterToGetLocation = true;
                Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                String city = "";
                //Split special characters
                if (stateName.contains(", ")) {
                    String[] parts = stateName.split(", ");
                    city = parts[0];
                   // Log.d("City split", city);
                } else {
                    if (stateName.contains(" ")) {
                        String[] parts = stateName.split(" ");
                        city = parts[0];
                     //   Log.d("City split space", city);
                    }
                }
                String query = URLEncoder.encode(city, "utf-8");
              //  Log.d("cityName", cityName);
               // Log.d("stateName", stateName);
               // Log.d("countryName", countryName);
               // Log.d("LATITUDE", latitude.toString());
               // Log.d("LONGITUDE", longitude.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadWebView() {
        webServices.addJavascriptInterface(new AppJavaScriptProxyServices(this), "androidProxy");
        webServices.loadUrl(url);
    }

    public void openOnePocket(){

        Intent intent = new Intent(ServiceActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(this), onePocketmessage, OPKConstants.TYPE_MCARD, (com.allegra.handyuvisa.VisaCheckoutApp) getApplication());
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);
    }

    public void onMenu(View view) {
        animate();
    }

    public void onUp(View view) {
        if (webServices.canGoBack()) {
            webServices.goBack();
        }
    }


    private void loadArrows() {

        if (webServices.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webServices.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webServices.canGoBack()) {
            webServices.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webServices.canGoForward()) {
            webServices.goForward();
        }
    }
    //****************************INNER CLASSES****************************

    private class MyBrowser extends WebViewClient {

        private Context context;

        public MyBrowser(Context context) {
            this.context = context;
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url.equals("allegra:mcard")) {
                Intent i = new Intent(context, com.allegra.handyuvisa.Mcardhtml.class);
                context.startActivity(i);
                return true;
            }
            return super.shouldOverrideUrlLoading(webView, url);
            }
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webServices.loadUrl(returnURL);
            }
            //*******************Send location's parameters to myGeoLocation JavaScript Function***************************

            if (latitude!=null && longitude!= null) {
                webServices.evaluateJavascript("javascript:myGeoLocation(" + latitude.toString() + "," + longitude.toString() + ");", null);
            } else {//Hardcode Other location in Bogotá
                webServices.evaluateJavascript("javascript:myGeoLocation(4.665417, -74.077237);",null);
            }
            loadArrows();
        }
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private class JsInterface {

        private Context mContext;
        JsInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void myGeoLocation(String mlat, String mLon) {

            mlat = latitude.toString();
            mLon = longitude.toString();
           // Log.d(TAG, "Lat: "+mlat+" Long:"+ mLon);
        }

        @JavascriptInterface
        public void postMessage(String message){

           // Log.e(TAG, "mesage: "+message);

            if (message.contains("showLoader")){
                //Show native loader
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView pb_search_loader = (ImageView)findViewById(R.id.pb_search_loader);
                        ImageView imgProgress = (ImageView)findViewById(R.id.imgProgress);
                        CustomizedTextView textView = (CustomizedTextView)findViewById(R.id.txtLoading);
                        pb_search_loader.setVisibility(View.VISIBLE);
                        imgProgress.setVisibility(View.VISIBLE);
                        webServices.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                    }
                });

            }
            if (message.contains("hideLoader")){
                //Hide native loader
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView pb_search_loader = (ImageView)findViewById(R.id.pb_search_loader);
                        ImageView imgProgress = (ImageView)findViewById(R.id.imgProgress);
                        CustomizedTextView textView = (CustomizedTextView)findViewById(R.id.txtLoading);

                        pb_search_loader.setVisibility(View.GONE);
                        imgProgress.setVisibility(View.GONE);
                        webServices.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.GONE);
                    }
                });
            }
        }

    }

    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,
                                                       GeolocationPermissions.Callback callback) {
            // Always grant permission since the app itself requires location
            // permission and the user has therefore already granted it
            callback.invoke(origin, true, false);
        }
    }

}
