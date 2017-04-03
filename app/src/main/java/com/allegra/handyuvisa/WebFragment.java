package com.allegra.handyuvisa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
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
import com.allegra.handyuvisa.utils.WebNavigationCallBack;
import com.allegra.handyuvisa.utils.WebNavigationEnablingCallback;
import com.allem.onepocket.utils.OPKConstants;

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
    public String onePocketmessage;

    RelativeLayout navigationPanelLayout, toolbarLayout;
    ImageButton arrowBack, arrowF, menuButton, backButton;
    ProgressBar progressBar;
    TextView screenTitle;
    private String returnURL;

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

        /** remember to add always a starter view string in bundle **/
        if(getArguments().getString(STARTER_VIEW).contains("FlightsActivity")){
           url = flightsParams();
        } else if(getArguments().getString(STARTER_VIEW).contains("HotelsActivity")){
            url = hotelsParams();
        } else if(getArguments().getString(STARTER_VIEW).contains("ConciergeActivity")){
            url = conciergeParams();
        } else if(getArguments().getString(STARTER_VIEW).contains(BackFragment.VIEW_TYPE_MCARD)){
            url = url+userEmail;
        }

        canReturn = getArguments().getBoolean(CAN_RETURN);

        Log.e("Web", "url: "+url);
        Log.e("Web", "starter view "+getArguments().getString(STARTER_VIEW));
        Log.e("Web", "can return: "+canReturn);

        initViews(view);
        configToolbar(canReturn);
        setListeners();
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
        loadWebNavigation();
        enablingCallback.canGoBack(view.canGoBack());
        enablingCallback.canGoForward(view.canGoForward());
    }

    @Override
    public boolean onShouldOverrideUrlLoading(WebView view, String url){

        if (url.contains("http://") || url.contains("https://")) {
            if (!url.contains(".pdf")) URL_OPEN_PDF = "";
            view.loadUrl(URL_OPEN_PDF + url);
        }

        return true;
    }

    /**
     * Proper methods
     * **/
    private void initViews(View view){

        mWebView = (WebView) view.findViewById(R.id.web_view);
        navigationPanelLayout = (RelativeLayout) view.findViewById(R.id.navigation_panel);
        toolbarLayout = (RelativeLayout) view.findViewById(R.id.ll_header);
        arrowBack = (ImageButton) view.findViewById(R.id.arrow_back);
        arrowF = (ImageButton) view.findViewById(R.id.arrow_forward);
        menuButton = (ImageButton) view.findViewById(R.id.menu_image);
        backButton = (ImageButton) view.findViewById(R.id.ib_up);
        progressBar = (ProgressBar) view.findViewById(R.id.web_progressBar);

        screenTitle = (TextView) view.findViewById(R.id.tv_title);
        screenTitle.setText(getArguments().getString("web_title"));

        setupLoadingView(view);

        setupWebView();
    }

    private void configToolbar(boolean canReturn){

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                navigationPanelLayout.getLayoutParams();

        if(canReturn){
            layoutParams.width = (int) getResources().getDimension(R.dimen.web_buttons_width);
            layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            /*layoutParams.addRule(RelativeLayout.END_OF, R.id.tv_title);
            layoutParams.addRule(RelativeLayout.RIGHT_OF, R.id.tv_title);*/
            menuButton.setVisibility(View.GONE);
            navigationPanelLayout.setLayoutParams(layoutParams);

            backButton.setVisibility(View.VISIBLE);
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

    private void setListeners(){
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
    }

    private void loadPage(String url){
        progressBar.setVisibility(View.VISIBLE);
        mWebView.loadUrl(url);
    }

    private void loadWebNavigation(){
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
    }

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
                OPKConstants.TYPE_MARKETPLACE,
                (com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication());

        OnepocketPurchaseActivity fragmentOPKPurchase = new OnepocketPurchaseActivity();
        fragmentOPKPurchase.setArguments(bundle);

        ((MainActivity) getActivity()).replaceLayout(fragmentOPKPurchase, false);
        //intent.putExtras(bundle);
        //startActivityForResult(intent, MarketPlaceActivity.REQUEST_ONEPOCKET_RETURN);*/

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
