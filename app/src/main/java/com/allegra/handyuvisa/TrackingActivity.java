package com.allegra.handyuvisa;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.TrackingData;
import com.allegra.handysdk.responsebean.TrackingDetailInterface;
import com.allegra.handyuvisa.utils.SlideHolder;
import com.allegra.handyuvisa.utils.JSONParser;
import com.allegra.handyuvisa.utils.MyTextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsandoval on 20/06/16.
 */
public class TrackingActivity extends Activity implements View.OnClickListener,TrackingDetailInterface {

    //ImageView iv_menu_back,iv_search_heder;
    ImageView iv_navi_heder;
    ListView lv_tracking_list;
    private View ll_tracking_addmoreservice;
    MyTextView tv_logotext_heder;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private GoogleMap googleMap;
    MapWrapperLayout mapWrapperLayout;
    private ArrayList<TrackingData> array_trackingdata = new ArrayList<TrackingData>();
    LatLngBounds.Builder b;
    ProgressBar progressbar;
    Dialog dialog;
    List<LatLng> pontos = null;
    Double startlati, startlongi, endlati,  endlongi;
    private final int delay = 30000;  //5 mins = 300000
    Handler handler;
    Runnable timerRunnable;
    String bookingId;
    SlideHolder slideHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tracking_layout);
        preferences = getSharedPreferences("My_Pref", 0);
        editor = preferences.edit();
        init();

        try {
            bookingId=getIntent().getStringExtra("bookingId");
            Log.e("System out","bookingId "+bookingId);
            BeanConstants.service.GetBookingTracking(TrackingActivity.this, bookingId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler = new Handler();
        handler.postDelayed(finishRunnable, delay);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        handler.postDelayed(finishRunnable, delay);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(TrackingActivity.this, MyBookingActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
        handler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(finishRunnable);
    }

    private void init() {
        //iv_menu_back.setVisibility(View.VISIBLE);
        // tv_header_back.setText("Tracking");
        lv_tracking_list = (ListView) findViewById(R.id.lv_tracking_list);
        ll_tracking_addmoreservice = findViewById(R.id.ll_tracking_addmoreservice);
        //iv_menu_back.setOnClickListener(this);
        ll_tracking_addmoreservice.setOnClickListener(this);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.userlocation_map_booking1)).getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout21);
        mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onClick(View v) {


        /*if (v == iv_menu_back) {
            Intent intent = new Intent(TrackingActivity.this, MyBookingActivity.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
            handler.removeCallbacks(timerRunnable);
        } *//*else if (v == iv_logo_nevi1_back) {
            Intent intent = new Intent(TrackingActivity.this, DashboardNewActivity.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
            handler.removeCallbacks(timerRunnable);
*/
        if (v == ll_tracking_addmoreservice) {
            Intent intent = new Intent(TrackingActivity.this, HomeActivity_Handy.class);
            ComponentName cn = intent.getComponent();
            Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();
            handler.removeCallbacks(timerRunnable);
        }
    }
    private Runnable finishRunnable = new Runnable() {
        @Override
        public void run() {
            BeanConstants.service.GetBookingTracking(TrackingActivity.this,bookingId);
            handler.postDelayed(finishRunnable, delay);
        }
    };

    @Override
    public void TrackingInfo(ArrayList<TrackingData> trackingdatalist) {
        b = new LatLngBounds.Builder();

        googleMap.clear();

        if(!trackingdatalist.get(0).getProviderLatitude().equalsIgnoreCase("")){

            final Marker pMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(trackingdatalist.get(0).getProviderLatitude()), Double.parseDouble(trackingdatalist.get(0).getProviderLongitude())))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_pin))
                    .anchor(0.5f, 1));

            final Marker cMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(trackingdatalist.get(0).getCustomerLatitude()), Double.parseDouble(trackingdatalist.get(0).getCustomerLongitude())))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location))
                    .anchor(0.5f, 1));

            b.include(new LatLng(Double.parseDouble(trackingdatalist.get(0).getProviderLatitude()), Double.parseDouble(trackingdatalist.get(0).getProviderLongitude())));
            b.include(new LatLng(Double.parseDouble(trackingdatalist.get(0).getCustomerLatitude()), Double.parseDouble(trackingdatalist.get(0).getCustomerLongitude())));

            LatLngBounds bounds = b.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 500, 500, 5);

            googleMap.animateCamera(cu);
            startlati=Double.parseDouble(trackingdatalist.get(0).getProviderLatitude());
            startlongi= Double.parseDouble(trackingdatalist.get(0).getProviderLongitude());
            endlati=Double.parseDouble(trackingdatalist.get(0).getCustomerLatitude());
            endlongi=Double.parseDouble(trackingdatalist.get(0).getCustomerLongitude());

            if(startlati>0.0 && startlongi>0.0){
                new GetDirection().execute();
            }

        }else{
            final Marker cMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(trackingdatalist.get(0).getCustomerLatitude()), Double.parseDouble(trackingdatalist.get(0).getCustomerLongitude())))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location))
                    .anchor(0.5f, 1));

            b.include(new LatLng(Double.parseDouble(trackingdatalist.get(0).getCustomerLatitude()), Double.parseDouble(trackingdatalist.get(0).getCustomerLongitude())));
            LatLngBounds bounds = b.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 500, 500, 5);
            googleMap.animateCamera(cu);
        }
    }


    class GetDirection extends AsyncTask<String, String, List<LatLng>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            progressbar = new ProgressBar(TrackingActivity.this);
            dialog = new Dialog(TrackingActivity.this,
                    android.R.style.Theme_Translucent_NoTitleBar);
            progressbar.setBackgroundResource(R.drawable.progress_background);
            dialog.addContentView(progressbar, new LinearLayout.LayoutParams(40, 40));
            Window window = dialog.getWindow();
            window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            dialog.setCancelable(false);
            dialog.show();

        }

        protected List<LatLng> doInBackground(String... args) {
            String origin = startlati + "," + startlongi;
            String destination = endlati + "," + endlongi;
            String stringUrl = "http://maps.googleapis.com/maps/api/directions/json?origin="
                    + origin + "&destination=" + destination + "&sensor=false";

            try {

                JSONParser json_parser = new JSONParser();
                String jsonOutput = json_parser.makeServiceCall(stringUrl,
                        "GET");
                Log.d("System out",
                        "Route Response:==  " + jsonOutput.toString());
                JSONObject jsonObject = new JSONObject(jsonOutput);

                if (jsonObject.length() > 0) {

                    String status = "";

                    if (jsonObject.has("status")) {
                        status = jsonObject.optString("status");

                        if (status.equalsIgnoreCase("ZERO_RESULTS")) {
                            return pontos;
                        } else {

                            // routesArray contains ALL routes
                            JSONArray routesArray = jsonObject
                                    .getJSONArray("routes");
                            if (routesArray.length() > 0) {
                                // Grab the first route
                                JSONObject route = routesArray.getJSONObject(0);

                                JSONObject poly = route
                                        .getJSONObject("overview_polyline");
                                String polyline = poly.getString("points");
                                pontos = decodePoly(polyline);
                            }
                        }
                    } else {
                        return pontos;
                    }
                } else {
                    return pontos;
                }

            } catch (Exception e) {

            }
            return pontos;
        }

        protected void onPostExecute(List<LatLng> result) {
            super.onPostExecute(result);

            if (result != null) {

                for (int i = 0; i < pontos.size() - 1; i++) {
                    LatLng src = pontos.get(i);
                    LatLng dest = pontos.get(i + 1);
                    try {
                        // here is where it will draw the polyline in your map
                        Polyline line = googleMap
                                .addPolyline(new PolylineOptions()
                                        .add(new LatLng(src.latitude,
                                                        src.longitude),
                                                new LatLng(dest.latitude,
                                                        dest.longitude))
                                        .width(6).color(Color.RED)
                                        .geodesic(true));
                    } catch (NullPointerException e) {
                        Log.e("Error", "NullPointerException onPostExecute: "
                                + e.toString());
                    } catch (Exception e2) {
                        Log.e("Error",
                                "Exception onPostExecute: " + e2.toString());
                    }

                }

                try {
                    if (dialog.isShowing())
                        dialog.dismiss();

                } catch (IllegalArgumentException e) {
                    dialog.dismiss();
                    dialog = null;
                }

            } else {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(TrackingActivity.this,
                                "Route not found", Toast.LENGTH_LONG)
                                .show();

                    }
                });

                try {
                    if (dialog.isShowing())
                        dialog.dismiss();

                } catch (IllegalArgumentException e) {
                    dialog.dismiss();
                    dialog = null;
                }
            }

        }
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public class TrackinAdapter extends BaseAdapter {

        private ArrayList<TrackingData> marray_Search;
        private LayoutInflater mInflater = null;
        public Activity mActivity;

        public TrackinAdapter(Activity activity, ArrayList<TrackingData> array_trckindata) {
            this.mActivity = activity;
            this.marray_Search = array_trckindata;
            this.mInflater = (LayoutInflater) this.mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return marray_Search.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.tracking_adapter_layout, null);
            Log.e("System out", "In get View...");
            final TextView txt_tracking_name = (TextView) convertView.findViewById(R.id.txt_tracking_name);
            final TextView txt_tracking_type = (TextView) convertView.findViewById(R.id.txt_tracking_type);
            final ImageView trcking_img = (ImageView) convertView.findViewById(R.id.trcking_img);

            if (marray_Search.get(position).getServiceName().length() > 0) {
                txt_tracking_type.setText(marray_Search.get(position).getServiceName());
            } else {
                txt_tracking_type.setText("");
            }

            try {
                final Bitmap staticmapBit = getGoogleMapThumbbail2(marray_Search.get(position).getCustomerLatitude(), marray_Search.get(position).getCustomerLongitude(), marray_Search.get(position).getProviderLatitude(), marray_Search.get(position).getProviderLongitude());
                trcking_img.setImageBitmap(staticmapBit);
                trcking_img.setScaleType(ImageView.ScaleType.FIT_XY);

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*Drawable myDrawable = getResources().getDrawable(R.drawable.map_03);
            trcking_img.setImageDrawable(myDrawable);*/

            return convertView;
        }

    }

    public Bitmap getGoogleMapThumbbail2(String latitude, String longitude, String deslatitude, String desSlongitude) {


        String getMapURL = "https://maps.googleapis.com/maps/api/staticmap?size=400x250&path=color:0xff0000ff|weight:5|" + latitude + "," + longitude + "|" + deslatitude + "," + desSlongitude + "&markers=size:mid|color:red|" + latitude + "," + longitude + "&markers=size:mid|color:red|" + deslatitude + "," + desSlongitude;
        Log.d("System out", "Url:=  " + getMapURL);
        Log.d("System out", "In Download Code Path " + getMapURL);
        Bitmap bmp = null;
        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL url = new URL(getMapURL);
            Log.d("System out", "above http ");
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection();
            Log.d("System out", "after http");
            connection.setRequestMethod("GET");
            Log.d("System out", "after get");
            connection.setDoOutput(true);
            connection.connect();

            InputStream in = connection.getInputStream();
            bmp = BitmapFactory.decodeStream(in);
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        return bmp;
    }


}
