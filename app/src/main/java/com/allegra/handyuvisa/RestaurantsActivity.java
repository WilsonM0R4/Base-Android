package com.allegra.handyuvisa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.allegra.handyuvisa.utils.GPSTracker;
import com.urbanairship.location.LocationService;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class RestaurantsActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    private WebView webView;
    private String url = "http://52.203.29.124/allemrestaurant/#!/brand/restaurantes/map";
    private ImageButton arrowBack, arrowF;
    private LocationManager locationManager;
    private String returnURL;
    public String stringLatitude, stringLongitude;
    private boolean isGPSEnabled, isNetworkEnabled, enterToGetLocation;
    private Location location;
    GPSTracker gp;
    Double latitude, longitude;
    String mLat, mLong;
    Dialog dialog;

    //TODO: Get location and send : ?lon=111&lat=222  at finish of the url

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.activity_restaurants,this);
    }

    @Override
    public void initViews(View root) {

        webView = (WebView)root.findViewById(R.id.webView2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        webView.setWebChromeClient(new GeoWebChromeClient());
        webView.setWebViewClient(new MyBrowser());
        webView.loadUrl(url);
        arrowBack = (ImageButton) root.findViewById(R.id.arrow_back_restaurants);
        arrowF = (ImageButton) root.findViewById(R.id.arrow_foward_restaurants);
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
    }

    private class MyBrowser extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView webView, String url){
            webView.loadUrl(url);
            return true;
        }

        public void onPageFinished(WebView view, String url) {
            if (url.equals("about:blank")) {
                webView.loadUrl(returnURL);
            }
            view.loadUrl("javascript:myCurrentLocation();void(0);");
            JsInterface jsInterface = new JsInterface(getApplicationContext());
            loadArrows();
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
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, mLat,Toast.LENGTH_LONG);
                }
            });

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

    public Location getLocation() {
        try {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                Log.d("JUANCHO ", "ESTOY ACA");
                buildAlertMessageNoGps();
            } else {
                Log.d("JUANCHO ","ESTOY ACA 2");
                gp = new GPSTracker(this);
                latitude = gp.getLatitude();
                longitude = gp.getLongitude();
                if (latitude != 0.0) {
                    enterToGetLocation = true;
                    Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    String countryName = addresses.get(0).getAddressLine(2);
                    String city = "";
                    //Split special characters
                    if (stateName.contains(", ")){
                        String[] parts = stateName.split(", ");
                        city = parts[0];
                        Log.d("City split",city);
                    }else {
                        if (stateName.contains(" ")) {
                            String[] parts = stateName.split(" ");
                            city = parts[0];
                            Log.d("City split space", city);
                        }
                    }
                    String query = URLEncoder.encode(city, "utf-8");
                    Log.d("cityName",cityName);
                    Log.d("stateName",stateName);
                    Log.d("countryName",countryName);
                    Log.d("LATITUDE", latitude.toString());
                    Log.d("LONGITUDE", longitude.toString());

                } else {
                    Toast.makeText(RestaurantsActivity.this, R.string.location_failed,
                            Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void showCurrentLocation(){

        Log.e("JUAN ", "ENTRE ACA");
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION },
                    Integer.parseInt(LocationService.LOCATION_SERVICE));
        }

        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        latitude = location.getLatitude();
        longitude = location.getLongitude();


    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                R.string.location_permision)
                .setCancelable(false)
                .setPositiveButton(R.string.location_yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton(R.string.location_no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        //buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    public void onMenu(View view) {
        animate();
    }

    private void loadArrows() {

        if (webView.canGoBack()) {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        } else {
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if (webView.canGoForward()) {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        } else {
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }


    public void onGoBack(View view) {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    public void onGoForward(View view) {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }
}
