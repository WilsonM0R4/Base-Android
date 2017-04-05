package com.allegra.handyuvisa;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.GPSTracker;
import com.allegra.handyuvisa.utils.WebNavigationCallBack;
import com.allegra.handyuvisa.utils.WebNavigationEnablingCallback;
import com.allem.onepocket.utils.OPKConstants;

import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

/**
 * Created by wmora on 24/03/17.
 */

public class WebFragment extends WebViewActivity implements WebNavigationCallBack{


    /******* GLOBAL ATTRIBUTES ******/
    public static final int REQUEST_ONEPOCKET_RETURN = 10001;
    public static String URL_OPEN_PDF = "https://docs.google.com/viewer?url=";
    public static final String LOADING_URL = "loading_url";
    public static final String WEB_TITLE = "web_title";
    public static final String STARTER_VIEW = "starter_view";
    public static final String CAN_RETURN = "can_return";

    //user email get from AllemUser
    private String userEmail;

    //flights bundle attributes
    private int paramTrip = 0; // 0 - round trip; 1 - one way
    private int paramCabin = 0; // 0 - economy; 1 - business; 2 - first
    private int paramAdult = 0;
    private int paramChildren = 0;
    private int paramInfant = 0;
    private String paramDepartDate = "";
    private String paramArriveDate = "";
    private String paramDepartFrom;
    private String paramDepartFromName;
    private String paramArriveAt;
    private String paramArriveAtName;
    private String flightType = "";

    //hotels bundle attributes
    private String paramDestination;
    private String paramDestinationName;
    private String paramCheckIn;
    private String paramCheckOut;
    private int hotel = 1;
    private int paramRooms, onlyOnceProgressBar = 0;
    private WebView webView;
    private String mcard;
    private int paramAdults = 0, paramAdults2 = 0, paramAdults3 = 0, paramAdults4 = 0;
    private int paramChildren2 = 0, paramChildren3 = 0, paramChildren4 = 0;

    //concierge bundle params
    private String paramNameCity = "";
    private String paramIdCity = "";

    private String url;
    private String callUrl;
    public String onePocketmessage;
    private String opkConstant = "";
    GPSTracker gp;
    Double latitude, longitude;
    private boolean isGPSEnabled, isNetworkEnabled, enterToGetLocation;

    RelativeLayout navigationPanelLayout, toolbarLayout;
    ImageButton arrowBack, arrowF, menuButton, backButton;
    ProgressBar progressBar;
    TextView screenTitle;
    private String returnURL;
    public static final int REQUEST_LOCATION_CODE = 1234, MY_PERMISSIONS_REQUEST_LOCATION = 4563;
    public static final int MY_PERMISSIONS_REQUEST_CALL = 8888;
    String[] MY_PERMISSIONS_CALL = { android.Manifest.permission.CALL_PHONE};
    String[] PERMISSIONS = { android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};

    private boolean canReturn = false;

    WebNavigationEnablingCallback enablingCallback;

    /**
     * Fragment override methods
     * **/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.web_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        ((MainActivity) getActivity()).statusBarVisibility(false);

        AllemUser user = Constants.getUser(getActivity());

        enablingCallback = ((FragmentMain) getParentFragment());

        if(user != null){
            userEmail = user.email;
        }

        url = getArguments().getString(LOADING_URL);
        Log.e("Web", getArguments().getString(STARTER_VIEW));

        canReturn = getArguments().getBoolean(CAN_RETURN);

        initViews(view);

        configToolbar(canReturn);

        /** remember to add always a starter view string in bundle **/
        if(getArguments().getString(STARTER_VIEW).contains("FlightsActivity")){
            url = flightsParams();
            opkConstant = OPKConstants.TYPE_FLIGHT;
        } else if (getArguments().getString(STARTER_VIEW).contains("LegalActivity")){
            ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_BACK_MENU,
                    getArguments().getString(WEB_TITLE));
        } else if(getArguments().getString(STARTER_VIEW).contains("HotelsActivity")){
            url = hotelsParams();
            opkConstant = OPKConstants.TYPE_HOTEL;
        } else if(getArguments().getString(STARTER_VIEW).contains("ConciergeActivity")){
            url = conciergeParams();
            opkConstant = OPKConstants.TYPE_HOTEL;
        } else if(getArguments().getString(STARTER_VIEW).contains(BackFragment.VIEW_TYPE_MCARD)){
            url = url+userEmail;
            opkConstant = OPKConstants.TYPE_MCARD;
        } else if(getArguments().getString(STARTER_VIEW).contains(BackFragment.VIEW_TYPE_SERVICES)){
            url = Constants.getServiceUrl()+"&email="+userEmail+"&v=1"+"&idPortal="+Constants.ID_PORTAL;
            opkConstant = OPKConstants.TYPE_MCARD;
        } else if(getArguments().getString(STARTER_VIEW).contains(BackFragment.VIEW_TYPE_RESTAURANTS)){
            url = Constants.getRestaurantUrl();
            opkConstant = OPKConstants.TYPE_MCARD;
        } else if(getArguments().getString(STARTER_VIEW).contains(BackFragment.VIEW_TYPE_STORE)){
            url = Constants.getStoreUrl();
            opkConstant = OPKConstants.TYPE_MCARD;
        }



        Log.e("Web", "url: "+url);
        Log.e("Web", "starter view "+getArguments().getString(STARTER_VIEW));
        Log.e("Web", "can return: "+canReturn);



        //setListeners();
        loadPage(url);
    }

    @Override
    public void onPause(){
        super.onPause();
        mWebView.stopLoading();
    }

    @Override
    public void onResume(){
        super.onResume();
        loadPage(url);
    }

    //Review importance and usage for this method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        Log.d("Receiving","Receiving Activity ONE");
        if (requestCode == REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = data.getStringExtra("RESULT");
                mWebView.clearHistory();
                mWebView.loadUrl("about:blank");
            }
        }else if (requestCode == Constants.ONE_POCKET_NEEDS_LOGIN){
            openOnePocket();
        }

        //Validate permissions
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            getCurrentLocation();
        }

    }

    /**
     * WebViewActivity override methods
     * **/
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon){
        super.onPageStarted(view, url, favicon);
        progressBar.setVisibility(View.VISIBLE);
        enablingCallback.canGoBack(view.canGoBack());
        enablingCallback.canGoForward(view.canGoForward());
    }

    @Override
    public void onPageFinished(WebView view, String url){
        super.onPageFinished(view, url);
        progressBar.setVisibility(View.GONE);
        if (url.equals("about:blank")) {
            mWebView.loadUrl(returnURL);
        }
        //INTERFACE WebNavigationCallback
        //loadWebNavigation();
        enablingCallback.canGoBack(view.canGoBack());
        enablingCallback.canGoForward(view.canGoForward());
    }

    @Override
    public boolean onShouldOverrideUrlLoading(WebView view, String url){

        if (url.contains("http://") || url.contains("https://")) {
            if (!url.contains(".pdf")) URL_OPEN_PDF = "";
            view.loadUrl(URL_OPEN_PDF + url);
        } else if (url.startsWith("tel:")) {
            callUrl = url;
            startCall(callUrl);
            return true;
        }

        return true;
    }

    /**
     * Proper methods
     * **/
    private void initViews(View view){


        /*navigationPanelLayout = (RelativeLayout) view.findViewById(R.id.navigation_panel);
        toolbarLayout = (RelativeLayout) view.findViewById(R.id.ll_header);
        arrowBack = (ImageButton) view.findViewById(R.id.arrow_back);
        arrowF = (ImageButton) view.findViewById(R.id.arrow_forward);
        menuButton = (ImageButton) view.findViewById(R.id.menu_image);
        backButton = (ImageButton) view.findViewById(R.id.ib_up);


        screenTitle = (TextView) view.findViewById(R.id.tv_title);
        screenTitle.setText(getArguments().getString("web_title"));*/

        progressBar = (ProgressBar) view.findViewById(R.id.web_progressBar);
        mWebView = (WebView) view.findViewById(R.id.web_view);

        setupLoadingView(view);

        setupWebView();
    }

    private void configToolbar(boolean canReturn){

        if(canReturn){
            ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_BACK_WEB,
                    getArguments().getString(WEB_TITLE));
        } else {
            ((FragmentMain) getParentFragment()).configToolbar(false, Constants.TYPE_WEB_MENU,
                    getArguments().getString(WEB_TITLE));
        }
    }

    public void setupLoadingView(View root){
        mLoadingView = (FrameLayout) root.findViewById(R.id.loading_view);
        mLoadingBar = (ImageView) root.findViewById(R.id.pb_search_loader);
    }

    private void setupWebView(){

        mWebView.setWebViewClient(new SecureBrowser(this));
        mWebView.addJavascriptInterface(new AppJavaScriptProxy(this), "androidProxy"); //getActivity() is temporal
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
    }

    /*private void setListeners(){
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoBack();
            }
        });

        arrowF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoForward();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceLayout(new MyAccountMenuActivity(), true);
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });
    }*/

    private void loadPage(String url){
        progressBar.setVisibility(View.VISIBLE);
        mWebView.loadUrl(url);
    }

    /*private void loadWebNavigation(){
        if(mWebView.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

        if(mWebView.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }
    }*/

    public void onGoBack(){
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public void onGoForward(){
        if(mWebView.canGoForward()) {
            mWebView.goForward();
        }
    }


    private String flightsParams(){

        paramTrip = getArguments().getInt("trip", 0);
        if (paramTrip == 1){//One Way
            flightType = "OW";
        } else {
            flightType = "RT";
        }

        String flightClass ="";
        switch (paramCabin){
            case 0:
                flightClass="Economy";
                break;
            case 1:
                flightClass="Business";
                break;
            case 2:
                flightClass="First";
                break;
        }

        paramCabin = getArguments().getInt("cabin", 0);// 0 - economy; 1 - business; 2 - first
        paramAdult = getArguments().getInt("adult", 0);
        paramChildren = getArguments().getInt("children", 0);
        paramInfant = getArguments().getInt("infant", 0);
        paramDepartDate = getArguments().getString("departOn");
        paramArriveDate = getArguments().getString("arriveOn");
        paramDepartFrom = getArguments().getString("departFrom");//IATA CODE DEPARTURE
        paramDepartFromName = getArguments().getString("departFromName");//LARGE NAME DEPARTURE
        paramArriveAt = getArguments().getString("arriveAt");//IATA CODE ARRIVAL
        paramArriveAtName = getArguments().getString("arriveAtName");//LARGE NAME ARRIVAL


        String postData = "AirFlightType="+flightType+"&AirOriginLocation="+paramDepartFromName+
                "&AirOriginLocationIATA="+paramDepartFrom+"&AirDestinationLocation="+paramArriveAtName+
                "&AirReturnDateTime="+paramArriveDate+"&AirDestinationLocationIATA="+paramArriveAt+
                "&AirDepartureDateTime="+paramDepartDate+"&AirTravelAvailAdt="+paramAdult+
                "&AirTravelAvailChd="+paramChildren+"&AirTravelAvailInf="+paramInfant+
                "&AirBookingClassPref="+flightClass+"&AirlineCode=*&AirFlexDateDeparture=*&AirTravelTimeStart=" +
                "*&AirFlexDatesReturn=*&AirTravelTimeEnd=*&AirTravelDic=*&AirRestricTar=*&AirSeg=1&Payment=3";

        return url + postData;

    }

    private String hotelsParams(){

        String postData = "";

        paramDestinationName = getArguments().getString("&cityHotel");
        paramDestination = getArguments().getString("&cityHotelHidden");
        paramCheckIn = getArguments().getString("&arrivalHotel");
        paramCheckOut = getArguments().getString("&departureHotel");
        paramRooms = getArguments().getInt("&roomHotel");
        paramAdult = getArguments().getInt("&adultHotel1");
        paramChildren = getArguments().getInt("&childHotel1");

        //TODO: Siempre enviar "es-CO" cuando la locale sea "es", cuando sea "en" se envía "en-US"
        switch(paramRooms) {
            case 1:
                postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;
            case 2:
                postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&adultHotel2=" +paramAdults2+
                        "&childHotel2="+paramChildren2+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;

            case 3:
                postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&adultHotel2=" +paramAdults2+
                        "&childHotel2="+paramChildren2+
                        "&adultHotel3=" +paramAdults3+
                        "&childHotel3="+paramChildren3+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;

            case 4:
                postData = "cityHotel="+paramDestinationName+
                        "&cityHotelHidden="+paramDestination+
                        "&arrivalHotel="+paramCheckIn+
                        "&departureHotel="+ paramCheckOut+
                        "&roomHotel="+ paramRooms+
                        "&adultHotel1=" +paramAdults+
                        "&childHotel1="+paramChildren+
                        "&adultHotel2=" +paramAdults2+
                        "&childHotel2="+paramChildren2+
                        "&adultHotel3=" +paramAdults3+
                        "&childHotel3="+paramChildren3+
                        "&adultHotel4=" +paramAdults4+
                        "&childHotel4="+paramChildren4+
                        "&language=es-CO&PaymentMethod=OnePocket"+
                        "&Group="+mcard;
                break;
        }

        return url+postData;
    }

    private String conciergeParams(){

        paramNameCity = getArguments().getString("&labelCity");
        paramIdCity = getArguments().getString("&idCity");

        return url + paramIdCity + "&nombre_destino_ser1=" + paramNameCity +
                "&Payment=3"+"&Group="+mcard;
    }

    public void openOnePocket(){
        //Intent intent = new Intent(MarketPlaceActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = Constants.createPurchaseBundle(Constants.getUser(getActivity()),
                onePocketmessage,
                opkConstant,
                (com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication());

        OnepocketPurchaseActivity fragmentOPKPurchase = new OnepocketPurchaseActivity();
        fragmentOPKPurchase.setArguments(bundle);

        ((FragmentMain) getParentFragment()).replaceLayout(fragmentOPKPurchase, false);
        //intent.putExtras(bundle);
        //startActivityForResult(intent, MarketPlaceActivity.REQUEST_ONEPOCKET_RETURN);*/

    }

    private void startCall(String url) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && hasPermission(getActivity(),MY_PERMISSIONS_CALL[0])) {
            ActivityCompat.requestPermissions(getActivity(), MY_PERMISSIONS_CALL, MY_PERMISSIONS_REQUEST_CALL);
        } else {
            Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse(url));
            if (ActivityCompat.checkSelfPermission(getActivity(), MY_PERMISSIONS_CALL[0]) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            this.startActivity(intentCall);
        }
    }

    public static boolean hasPermission(Context context, String permission) {
        if (context != null && permission != null) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void getCurrentLocation() {

        //TODO: Need to fix. Strong reference. Working on UI Thread
        gp = new GPSTracker(getActivity());
        latitude = gp.getLatitude();
        longitude = gp.getLongitude();

        if (latitude != 0.0) {

            try {
                enterToGetLocation = true;
                Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);
                String city = "";
                //Split special characters
                if (stateName.contains(", ")) {
                    String[] parts = stateName.split(", ");
                    city = parts[0];
                    //  Log.d("City split", city);
                } else {
                    if (stateName.contains(" ")) {
                        String[] parts = stateName.split(" ");
                        city = parts[0];
                        //  Log.d("City split space", city);
                    }
                }
                String query = URLEncoder.encode(city, "utf-8");
                // Log.d("cityName", cityName);
                // Log.d("stateName", stateName);
                // Log.d("countryName", countryName);
                // Log.d("LATITUDE", latitude.toString());
                // Log.d("LONGITUDE", longitude.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //INTERFACE WebNavigationCallback
    @Override
    public boolean onWebBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        }
        return false;
    }

    //INTERFACE WebNavigationCallback
    @Override
    public boolean onWebForwardPressed() {

        if(mWebView.canGoForward()){
            mWebView.goForward();
        }

        return false;
    }
}
