

package com.allem.alleminmotion.visacheckout;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import org.apache.http.util.EncodingUtils;

public class HotelSearchActivity extends LoadAnimate implements LoadAnimate.InflateReadyListener  {

    private static final String TAG = "HotelSearchActivity";
    public static final int REQUEST_ONEPOCKET_RETURN = 10001;
    private ActionBar actionBar;
    private String paramDestination;
    private String paramDestinationName;
    private String paramCheckIn;
    private String paramCheckOut;
    private int hotel = 1;
    private int paramRooms;
    private WebView webView;
    ProgressBar progressBar;
    private int paramAdults = 0, paramAdults2 = 0, paramAdults3 = 0, paramAdults4 = 0;
    private int paramChildren = 0, paramChildren2 = 0, paramChildren3 = 0, paramChildren4 = 0;
    private int paramInfant = 0, paramInfant2 = 0, paramInfant3 = 0,paramInfant4 = 0;
    private String paramDepartDate = "";
    private String paramArriveDate = "";
    private String paramDepartFrom;
    private String paramDepartFromName;
    private String paramArriveAt;
    private String paramArriveAtName;
    private String url,urlWebView;
    private ImageButton arrowBack;//, arrowF
    public String onePocketmessage;
    private String returnURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setView(R.layout.fragment_search_in_progress, R.drawable.load__hotel, R.string.txt_lbl_searchFlightsWait, this);
        setActionbar(true);

        paramChildren = getIntent().getIntExtra("&childHotel1", 0);
        paramAdults = getIntent().getIntExtra("&adultHotel1", 1);
        paramRooms = getIntent().getIntExtra("&roomHotel", 1);

        switch (paramRooms){
            case 2:
                paramChildren2 = getIntent().getIntExtra("&childHotel2", 0);
                paramAdults2 = getIntent().getIntExtra("&adultHotel2", 0);
                break;
            case 3:
                paramChildren2 = getIntent().getIntExtra("&childHotel2", 0);
                paramAdults2 = getIntent().getIntExtra("&adultHotel2", 0);
                paramChildren3 = getIntent().getIntExtra("&childHotel3", 0);
                paramAdults3 = getIntent().getIntExtra("&adultHotel3", 0);
                break;
            case 4:
                paramChildren2 = getIntent().getIntExtra("&childHotel2", 0);
                paramAdults2 = getIntent().getIntExtra("&adultHotel2", 0);
                paramChildren3 = getIntent().getIntExtra("&childHotel3", 0);
                paramAdults3 = getIntent().getIntExtra("&adultHotel3", 0);
                paramChildren4 = getIntent().getIntExtra("&childHotel4", 0);
                paramAdults4 = getIntent().getIntExtra("&adultHotel4", 0);
                break;
        }

        paramCheckIn = getIntent().getStringExtra("&arrivalHotel");
        paramCheckOut = getIntent().getStringExtra("&departureHotel");
        paramDestinationName = getIntent().getStringExtra("&cityHotel");
        paramDestination = getIntent().getStringExtra("&cityHotelHidden");

        Log.e("paramAdult",String.valueOf(paramAdults));
        Log.e("paramChildren",String.valueOf(paramChildren));
        Log.e("paramAdult2",String.valueOf(paramAdults2));
        Log.e("paramChildren2",String.valueOf(paramChildren2));
        Log.e("paramAdult3",String.valueOf(paramAdults3));
        Log.e("paramChildren3",String.valueOf(paramChildren3));
        Log.e("paramAdult4",String.valueOf(paramAdults4));
        Log.e("paramChildren4",String.valueOf(paramChildren4));
        Log.e("paramInfant",String.valueOf(paramInfant));
        Log.e("paramCheckIn",String.valueOf(paramCheckIn));
        Log.e("paramCheckOut",String.valueOf(paramCheckOut));
        Log.e("roomHotel", String.valueOf(paramRooms));
        Log.e("cityHotel", String.valueOf(paramDestinationName));
        Log.e("cityHotelHidden", String.valueOf(paramDestination));
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }
    private void setActionbar(boolean hide) {

        actionBar = getActionBar();
        if(actionBar!=null){
            if (hide) {
                actionBar.hide();
            } else {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeButtonEnabled(true);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.stopLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadWebView();
    }


    /**
     * Now is requesting by GET method
     */
    private void loadWebView() {

        url="http://alegra.dracobots.com/Hotel/Flow/Availability?";
        webView.addJavascriptInterface(new AppJavaScriptProxyHotels(this), "androidProxy");
        String postData = "";

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
                        "&language=es-CO&PaymentMethod=OnePocket";
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
                        "&language=es-CO&PaymentMethod=OnePocket";
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
                        "&language=es-CO&PaymentMethod=OnePocket";
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
                        "&language=es-CO&PaymentMethod=OnePocket";
                break;
        }
        //webView.postUrl(url, EncodingUtils.getBytes(postData, "BASE64"));
        url = url + postData;
        Log.d("murl",url);
        webView.loadUrl(url);
    }

    public void onMenu(View view) {
        animate();
    }

    @Override
    public void initViews(View root) {
        //url = getIntent().getStringExtra("url");
        //Log.d("URL HOTELS",url);
        webView = (WebView)root.findViewById(R.id.webView);
        progressBar= (ProgressBar) root.findViewById(R.id.pb_search);
        //btn_buy = (Button) findViewById(R.id.btn_buy);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        TextView title = (TextView) root.findViewById(R.id.tv_title_secc);
        title.setText(R.string.title_hotel_results);
        //progressBar.setVisibility(View.VISIBLE);
        arrowBack= (ImageButton)root.findViewById(R.id.arrow_back);
        //arrowF= (ImageButton)root.findViewById(R.id.arrow_forward);
        webView.addJavascriptInterface(new AppJavaScriptProxyHotels(this), "androidProxy");
    }

    @Override
    public void onCancelLoading() {
        finish();
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "url: " + url);
            if (Util.hasInternetConnectivity(HotelSearchActivity.this)){
                if (url.contains("http://")||url.contains("https://")){
                    view.loadUrl(url);
                    urlWebView=url;
                }else if(url.contains("detalleventaht:")&& Util.hasInternetConnectivity(HotelSearchActivity.this)){
                //urlWebView=HotelsActivity.this.url;

                    // TODO replace with Onepocket pay activity
                }else{
                    view.loadUrl(url);
                    urlWebView=url;
                }
            }else{
                Toast.makeText(HotelSearchActivity.this, R.string.err_no_internet, Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            if (url.equals("about:blank")) {
                webView.loadUrl(returnURL);
            }
            loadArrows();
              animate();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            //progressBar.setVisibility(View.VISIBLE);
            //showProgress(true);
        }
    }

    public void onBackButton(View view){
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.ACTIVITY_LOGIN) {
            if(resultCode == RESULT_OK){
                Log.d(TAG,"usuario logueado");
                HotelSearchActivity.this.invalidateOptionsMenu();
                webView.loadUrl("about:blank");
                loadWebView();
            }
            if (resultCode == RESULT_CANCELED) {

            }

        }else if (requestCode == Constants.ONE_POCKET_NEEDS_LOGIN){
            openOnePocket();
        } else if (requestCode == Constants.REQUEST_ONEPOCKET_RETURN) {
            if (data != null) {
                returnURL = data.getStringExtra("RESULT");
                webView.clearHistory();
                webView.loadUrl("about:blank");
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    public void openOnePocket(){

        Intent intent = new Intent(HotelSearchActivity.this, OnepocketPurchaseActivity.class);
        Bundle bundle = new Bundle();
        AllemUser user = Constants.getUser(this);
        VisaCheckoutApp app = (VisaCheckoutApp) getApplication();
        OneTransaction transaction = new OneTransaction(onePocketmessage,
                OPKConstants.TYPE_HOTEL,
                app.getIdSession(),
                user.nombre,
                user.apellido,
                user.email,
                app.getRawPassword(),
                app.getIdCuenta());

        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, transaction);
        intent.putExtras(bundle);
        startActivityForResult(intent, Constants.REQUEST_ONEPOCKET_RETURN);
    }

    private void loadArrows(){

        if(webView.canGoBack()){
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl));
        }else{
            arrowBack.setImageDrawable(getResources().getDrawable(R.drawable.navigation__backurl_2));
        }

      /*  if(webView.canGoForward()){
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl_2));
        }else{
            arrowF.setImageDrawable(getResources().getDrawable(R.drawable.navigation__fwdurl));
        }*/
    }

    public void onGoBack(View view){
        if(webView.canGoBack()) {
            webView.goBack();
        }

    }

    public void onGoForward(View view){
        if(webView.canGoForward()) {
            webView.goForward();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        urlWebView=this.url;
    }

}
