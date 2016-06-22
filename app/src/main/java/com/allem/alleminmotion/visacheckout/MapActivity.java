package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handysdk.bean.AddressResponseBean;
import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.CustomerData;
import com.allegra.handysdk.bean.ServiceProviderData;
import com.allegra.handysdk.bean.TimeSlotBean;
import com.allegra.handysdk.responsebean.AddresslistInterface;
import com.allegra.handysdk.responsebean.ServiceProviderCallInterface;
import com.allem.alleminmotion.visacheckout.utils.CircularImageView;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.GPSTracker;
import com.allem.alleminmotion.visacheckout.utils.MyAutoCompleteTextView;
import com.allem.alleminmotion.visacheckout.utils.MyEditText;
import com.allem.alleminmotion.visacheckout.utils.MyTextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by jsandoval on 20/06/16.
 */
public class MapActivity extends Activity implements View.OnClickListener,ServiceProviderCallInterface,AddresslistInterface, AdapterView.OnItemClickListener {

    ImageView iv_navi_hedermap, iv_logo_hedermap, iv_search_hedermap, iv_back_hedermap;
    public GoogleMap googleMap;
    MyAutoCompleteTextView ed_search_headermap;
    MyTextView tv_go_headermap;
    static final LatLng myLocation = new LatLng(0,0);
    MyTextView tv_booknow_map, tv_booklater_map, address;
    String umAddress1 = "", city = "", zipCode = "", umAddressTitle = "";
    String selectedDate="";
    int day, nextday;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter, newformater;
    View view;
    Boolean flagmarker=false;
    GPSTracker gp;
    Double latitude, longitude;
    private LocationManager locationManager;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private Location location;
    ArrayList<ServiceProviderData> ProviderList = new ArrayList<>();
    ArrayList<CustomerData> MainList = new ArrayList<>();
    public ArrayList<String> ary_starttime = new ArrayList<String>();
    public ArrayList<String> ary_edntime = new ArrayList<String>();
    public ArrayList<String> ary_combin = new ArrayList<String>();
    String sd = "";
    public String seltStartTime = "", seltEntTime = "", TfinalEndTime = "", strDate = "", seladdressstr = "";
    EditText edclock;
    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
    Calendar cal = Calendar.getInstance();
    public Date curntTIme;
    private Date dateComparestart;
    private Date dateCompareend;
    Boolean addAddress = true;
    RadioButton radiobtn_address;
    ArrayList<String> AdderssTitle = new ArrayList<>();
    HashMap<String, String> map;
    ArrayList<AddressResponseBean> addressList = new ArrayList<>();
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCM9X__zioBGFi_1bAcbTviHUFPQZ-0UhM";
    private Marker PMarker;
    private HashMap<Marker, Integer> allMarkersMap = new HashMap<Marker, Integer>();
    private MapWrapperLayout mapWrapperLayout;
    Boolean flagloca=false;
    String loctitle="";
    private ViewGroup infoWindow;
    String locationurl;
    CircularImageView user_chg_pro_image_update1;
    Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        init();
        curntTIme = new Date(System.currentTimeMillis());
        DateFormat df2 = new SimpleDateFormat("HH:mm");
        System.out.println(df2.format(curntTIme));
        strDate = df2.format(curntTIme);
        getLocation();
    }

    public Location getLocation() {
        try {

            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                buildAlertMessageNoGps();
            } else {
                gp = new GPSTracker(this);
                latitude = gp.getLatitude();
                longitude = gp.getLongitude();
                if (latitude != 0.0) {
                    new SendfeedbackJob().execute();
                } else {
                    Toast.makeText(MapActivity.this, "Fail to fetch your current location. Please check your GPS status and try again", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
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

    private void APiCall() {
        BeanConstants.userBeen.setUser_ID(BeanConstants.loginData.getUser_ID());
        BeanConstants.userBeen.setService_ID(getIntent().getStringExtra("serviceid"));
        BeanConstants.userBeen.setLatitude(latitude + "");
        BeanConstants.userBeen.setLongitude(longitude + "");
        BeanConstants.service.ServiceProviderCall(MapActivity.this);
    }

    @Override
    public void ServiceProviderresponse(ArrayList<CustomerData> serviceProviderData) {
        MainList = new ArrayList<>();
        ProviderList = new ArrayList<>();
        if (serviceProviderData.size() > 0) {
            MainList.addAll(serviceProviderData);
            ProviderList.addAll(serviceProviderData.get(0).getServiceProviderDatas());
        } else {
            tv_booknow_map.setClickable(false);
        }

        Log.d("System out","provider list: "+ProviderList.size());
        DisplayMapDataSearch(ProviderList);
    }

    void getAddress() {

        BeanConstants.service.GetAddressList(MapActivity.this);
    }

    @Override
    public void AddressListresponse(ArrayList<AddressResponseBean> addressResponseBeans) {
        addressList.addAll(addressResponseBeans);

        for (int i = 0; i < addressList.size(); i++) {
            AdderssTitle.add(addressList.get(i).getUser_Address_Title());
        }

    }

    @Override
    public void Deleteresponse(String response) {

    }

    @Override
    public void Timeslotresponse(ArrayList<TimeSlotBean> timeSlotBeans) {
        if (timeSlotBeans.size() == 0) {
            Toast.makeText(MapActivity.this, "Service is Not Available On this Day", Toast.LENGTH_SHORT).show();
        } else {
            ClickShowOtherSelectTimeSlot(timeSlotBeans);
        }
    }

    public void AddAddress(String umaddress, String City, String Zipcode, String umAddressTitle) {
        BeanConstants.addressBean.setAddrestitile(umAddressTitle);
        BeanConstants.addressBean.setAddressline(umaddress);
        BeanConstants.addressBean.setCity(City);
        BeanConstants.addressBean.setZipcode(Zipcode);
        BeanConstants.addressBean.setLatitude(latitude + "");
        BeanConstants.addressBean.setLongitude(longitude + "");
        BeanConstants.service.AddAddress(MapActivity.this);


    }

    @Override
    public void AddAddressResponse(String response) {
        Toast.makeText(MapActivity.this, "Address has been added successfully", Toast.LENGTH_SHORT).show();

    }

    public void ClickShowOtherSelectTimeSlot(ArrayList<TimeSlotBean> arrayList) {

        final Dialog dialog = new Dialog(MapActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.click_viewfile);
        ListView dialoglist;
        TextView title;
        dialoglist = (ListView) dialog.findViewById(R.id.dialoglist1);
        title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Select Time");
        if (ary_starttime.size() > 0) {
            ary_starttime.clear();
        }
        if (ary_edntime.size() > 0) {
            ary_edntime.clear();
        }

        if (ary_combin.size() > 0) {
            ary_combin.clear();
        }
        Log.d("System out", "arraylist size:" + arrayList.size());
        if (arrayList.size() > 0) {
            for (int i = 0; i < arrayList.size(); i++) {
                ary_starttime.add(arrayList.get(i).getFrom_Time());
                ary_edntime.add(arrayList.get(i).getTo_Time());
            }
        }

        if (ary_starttime.size() > 0) {
            for (int j = 0; j < ary_starttime.size(); j++) {
                String jj = ary_starttime.get(j).split(":")[0] + ":" + ary_starttime.get(j).split(":")[1] + " - " + ary_edntime.get(j).split(":")[0] + ":" + ary_edntime.get(j).split(":")[1];
                ary_combin.add(jj);
            }
        }

        dialoglist.setAdapter(new ArrayAdapter<String>(MapActivity.this, R.layout.list_cell, ary_combin));
        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                seltStartTime = ary_starttime.get(position).split(":")[0] + ":" + ary_starttime.get(position).split(":")[1];
                seltEntTime = ary_edntime.get(position).split(":")[0] + ":" + ary_edntime.get(position).split(":")[1];
                Log.d("System out", "" + seltStartTime + " - " + seltEntTime);

                edclock.setText(seltStartTime + " - " + seltEntTime);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constants.executeLogcat(MapActivity.this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        addAddress = true;
        if (requestCode == 2) {
            String message = data.getStringExtra("address");
            if (!message.equalsIgnoreCase("")) {
                ed_search_headermap.setText("");
                if (message.contains("-")) {
                    address.setText(message.split("-")[1].replace(", ,", ","));
                    seladdressstr = message;
                    getLatLongFromGivenPickupAddress(message.split("-")[1].replace(", ,", ","));
                } else {
                    address.setText(message);
                    seladdressstr = message;
                    getLatLongFromGivenPickupAddress(message);
                }
            }
            addAddress = false;
        }
    }

    public void getLatLongFromGivenPickupAddress(String youraddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            if (youraddress.contains("-")) {
                address = coder.getFromLocationName(youraddress.split("-")[1].replace(", ,", ","), 5);
            } else {
                address = coder.getFromLocationName(youraddress, 5);
            }
            Address location1 = address.get(0);
            if (location1.getLongitude() == 0) {
                Toast.makeText(MapActivity.this, "Please enter valid address", Toast.LENGTH_LONG).show();
            } else {

                latitude = location1.getLatitude();
                longitude = location1.getLongitude();
                APiCall();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {

        iv_navi_hedermap = (ImageView) findViewById(R.id.iv_navi_hedermap);
        iv_navi_hedermap.setVisibility(View.VISIBLE);
        iv_search_hedermap = (ImageView) findViewById(R.id.iv_search_hedermap);
        iv_search_hedermap.setVisibility(View.GONE);
        ed_search_headermap = (MyAutoCompleteTextView) findViewById(R.id.ed_search_headermap);
        ed_search_headermap.setVisibility(View.VISIBLE);
        iv_logo_hedermap = (ImageView) findViewById(R.id.iv_logo_hedermap);
        iv_logo_hedermap.setVisibility(View.GONE);
        tv_go_headermap = (MyTextView) findViewById(R.id.tv_go_headermap);
        tv_go_headermap.setVisibility(View.VISIBLE);

        tv_booklater_map = (MyTextView) findViewById(R.id.tv_booklater_map);
        tv_booklater_map.setOnClickListener(this);
        tv_booknow_map = (MyTextView) findViewById(R.id.tv_booknow_map);
        tv_booknow_map.setOnClickListener(this);
        iv_back_hedermap = (ImageView) findViewById(R.id.iv_back_hedermap);
        iv_back_hedermap.setVisibility(View.GONE);
        address = (MyTextView) findViewById(R.id.address);
        radiobtn_address = (RadioButton) findViewById(R.id.radiobtn_address);
        radiobtn_address.setOnClickListener(this);
        radiobtn_address.setButtonDrawable(R.drawable.radiobtn_unselected);
        mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
        mapWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));

        iv_navi_hedermap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_go_headermap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddress = true;
                String message = seladdressstr;
                if (!message.equalsIgnoreCase("")) {
                    ed_search_headermap.setText("");
                    if (message.contains("-")) {
                        address.setText(message.split("-")[1].replace(", ,", ","));
                        seladdressstr = message;
                        getLatLongFromGivenPickupAddress(message.split("-")[1].replace(", ,", ","));
                    } else {
                        address.setText(message);
                        seladdressstr = message;
                        getLatLongFromGivenPickupAddress(message);
                    }
                }
                addAddress = false;
            }
        });

        ed_search_headermap.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.autocomplete_cell));
        ed_search_headermap.setOnItemClickListener(MapActivity.this);
    }


    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        seladdressstr = (String) adapterView.getItemAtPosition(position);

        InputMethodManager inputManager = (InputMethodManager) MapActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(ed_search_headermap.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                        Log.e("System out", "Values " + filterResults.values);
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;


        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            //  sb.append("&components=country:gr");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            Log.e("System out", "Url..." + url);

            //System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("System out", "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("System out", "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");


            Log.e("System out", "predsJsonArray " + predsJsonArray.length());

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());

            for (int h = 0; h < AdderssTitle.size(); h++) {
                if (AdderssTitle.get(h).toLowerCase().contains(input.toLowerCase())) {
                    resultList.add(AdderssTitle.get(h) + " - " + addressList.get(h).getUser_Address() + ", " + addressList.get(h).getUser_City() + ", " + addressList.get(h).getUser_ZipCode());
                }
            }

            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

                JSONArray jarray = new JSONArray(predsJsonArray.getJSONObject(i).getString("terms"));

                JSONObject add3 = jarray.getJSONObject(jarray.length() - 1);
                map = new HashMap<>();
                map.put("userCity", add3.getString("value"));
            }
        } catch (JSONException e) {
            Log.e("System out", "Cannot process JSON results", e);
        }
        return resultList;
    }

    @Override
    public void onClick(View v) {
        if (tv_booknow_map == v) {

            if (MainList.size() > 0) {
                Log.d("System out", "start time: " + MainList.get(0).getCustomer_Booking_Start_Time() + " end time: " + MainList.get(0).getCustomer_Booking_End_Time());

                String startTime = MainList.get(0).getCustomer_Booking_Start_Time();
                String endtime = MainList.get(0).getCustomer_Booking_End_Time();
                String leadTime = "";
                leadTime = MainList.get(0).getLead_Time().replace("min", "").replace("Hour", "").trim();

                try {
                    Date date = df.parse(endtime);

                    if (leadTime == null || leadTime.equalsIgnoreCase("")) {
                        leadTime = "00";
                    }

                    int TEndTime =(int) Float.parseFloat(leadTime);
                    cal.setTimeInMillis(date.getTime());
                    cal.add(Calendar.MINUTE, -TEndTime);
                    TfinalEndTime = df.format(cal.getTime());
                    Log.d("System out", "TfinalEndTime :=  " + TfinalEndTime +leadTime );

                    curntTIme = parseDate(strDate);
                    dateComparestart = parseDate(startTime);
                    dateCompareend = parseDate(TfinalEndTime);
                    String currentDateandTime="",Currentdate="";

                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        currentDateandTime = sdf.format(new Date());
                        Currentdate=sdf1.format(new Date());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if (ProviderList.size()>0){
                        if (dateComparestart.before(curntTIme) && dateCompareend.after(curntTIme)) {

                            Log.d("System out","time: "+new SimpleDateFormat("HH:mm").format(new Date())+ "Date: "+new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            Intent intent = new Intent(MapActivity.this, BookNowActivity.class);
                            intent.putExtra("Estimate_Time", ProviderList.get(0).getEstimate_Time());
                            BeanConstants.BookingData.setBooking_Address(address.getText().toString().trim());
//                            BeanConstants.BookingData.setCustomer_Booking_End_Time(strDate);
//                            BeanConstants.BookingData.setCustomer_Booking_Start_Time(strDate);
                            BeanConstants.BookingData.setCustomer_Booking_End_Time("");
                            BeanConstants.BookingData.setCustomer_Booking_Start_Time("");
                            BeanConstants.BookingData.setBooking_Time(new SimpleDateFormat("HH:mm").format(new Date()));
                            BeanConstants.BookingData.setBooking_Date(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            BeanConstants.BookingData.setCustomer_Longitude(longitude + "");
                            BeanConstants.BookingData.setCustomer_Latitude(latitude + "");
                            BeanConstants.BookingData.setBooking_Type("Immediate");
                            startActivity(intent);
                        } else {
                            Log.e("System out", "in else toast");
                            Toast.makeText(getApplicationContext(), "Sorry booking time over", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(MapActivity.this, "Sorry! Currently provider is not available.", Toast.LENGTH_LONG).show();
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MapActivity.this, "Sorry! Currently provider is not available.", Toast.LENGTH_LONG).show();
            }
        } else if (tv_booklater_map == v) {

            final Dialog dialog = new Dialog(MapActivity.this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.timedate_activity);
            final MyTextView tvCalender = (MyTextView) dialog.findViewById(R.id.tv_timepicker_timedate);
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd / MM / yyyy", Locale.US);
            String formattedDate = df.format(c.getTime());
            tvCalender.setText(formattedDate);
            selectedDate=new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(c.getTime());
            edclock = (EditText) dialog.findViewById(R.id.ed_datepicker_timedate);
            Button btDone = (Button) dialog.findViewById(R.id.bt_done_timedate);
            btDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (edclock.getText().toString().trim().length() == 0) {
                        Toast.makeText(MapActivity.this, "Please select time slot", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Intent intent = new Intent(MapActivity.this, BookNowActivity.class);
                        BeanConstants.BookingData.setBooking_Address(address.getText().toString().trim());
                        BeanConstants.BookingData.setCustomer_Booking_End_Time(seltEntTime);
                        BeanConstants.BookingData.setCustomer_Booking_Start_Time(seltStartTime);
                        BeanConstants.BookingData.setBooking_Time(selectedDate+" "+seltStartTime);
                        BeanConstants.BookingData.setBooking_Date(selectedDate);
                        BeanConstants.BookingData.setCustomer_Longitude(longitude + "");
                        BeanConstants.BookingData.setCustomer_Latitude(latitude + "");
                        BeanConstants.BookingData.setBooking_Type("Later");
                        intent.putExtra("Estimate_Time", "Will notify");
                        startActivity(intent);
                    }
                }
            });
            tvCalender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar newCalendar;
                    newCalendar = Calendar.getInstance();
                    day = newCalendar.get(Calendar.DAY_OF_MONTH);
                    nextday = newCalendar.get(Calendar.DAY_OF_MONTH);

                    dateFormatter = new SimpleDateFormat("dd / MM / yyyy", Locale.US);
                    newformater= new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    Log.i("System out", "day " + day);
                    Log.i("System out", "nextday " + nextday);

                    datePickerDialog = new DatePickerDialog(MapActivity.this, new DatePickerDialog.OnDateSetListener() {

                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            view.setMinDate(System.currentTimeMillis() - 1000);
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            nextday = dayOfMonth;
                            tvCalender.setText(dateFormatter.format(newDate.getTime()));
                            selectedDate=newformater.format(newDate.getTime());
                        }

                    }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePickerDialog.getDatePicker().setCalendarViewShown(false);
                    datePickerDialog.show();
                }
            });

            edclock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dateStr = tvCalender.getText().toString().trim();
                    String newDateStr = "";
                    try {
                        SimpleDateFormat curFormater = new SimpleDateFormat("dd / MM / yyyy", Locale.US);
                        Date dateObj = curFormater.parse(dateStr);
                        SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
                        newDateStr = postFormater.format(dateObj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    BeanConstants.service.GetTimeSlot(MapActivity.this, newDateStr);
                }
            });

            dialog.show();

        } else if (v == radiobtn_address) {
            if (address.getText().toString().length() == 0) {
                Toast.makeText(MapActivity.this, "Please select address from dropdown", Toast.LENGTH_LONG).show();
            } else {
                radiobtn_address.setButtonDrawable(R.drawable.radiobtn_selected);
                radiobtn_address.setEnabled(false);
                final Dialog dialog = new Dialog(MapActivity.this);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.entertitle);
                final MyEditText title = (MyEditText) dialog.findViewById(R.id.title);
                LinearLayout done_ll = (LinearLayout) dialog.findViewById(R.id.done_ll);
                ImageView img_close_map = (ImageView) dialog.findViewById(R.id.img_close_map);


                img_close_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                done_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (title.getText().toString().length() > 0) {
                            umAddressTitle = title.getText().toString();
                            umAddress1 = seladdressstr;
                            city = "";
                            zipCode = "";
                            AddAddress(umAddress1, city, zipCode, umAddressTitle);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MapActivity.this, "Please enter title", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        }

    }


    private Date parseDate(String date) {

        final String inputFormat = "HH:mm";
        SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat);
        try {
            return inputParser.parse(date);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            String apiRequest = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude; //+ "&ka&sensor=false"
            HttpGet httpGet = new HttpGet(apiRequest);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();
            sd = "";
            Log.e("System out", "google apiRequest: " + apiRequest);

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e1) {
            } catch (IOException e2) {
            }

            try {

                JSONObject jobjh = new JSONObject(stringBuilder.toString());

                JSONArray jaaray = new JSONArray(jobjh.getString("results"));

                JSONObject jobj = jaaray.getJSONObject(0);

                JSONArray jaarayaddress_components = new JSONArray(
                        jobj.getString("address_components"));

                for (int i = 0; i < jaarayaddress_components.length(); i++) {
                    JSONObject jobjaddress_components = jaarayaddress_components
                            .getJSONObject(i);
                    if (sd.length() == 0) {
                        sd = jobjaddress_components.getString("long_name");
                    } else {
                        sd = sd + ", "
                                + jobjaddress_components.getString("long_name");
                    }

                }

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (!flagloca){
                            if (sd.equalsIgnoreCase("")) {
                                addAddress = true;
                                Toast.makeText(MapActivity.this, "Fail to fetch your current location. Please check your GPS status and try again", Toast.LENGTH_LONG).show();
                            } else {
                                addAddress = false;
                                address.setText(sd);
                                seladdressstr = sd;
                                APiCall();
                            }
                        }else{
                            loctitle=sd;
                        }


                    }
                });

            } catch (JSONException e3) {

                e3.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }

    LatLngBounds.Builder b;
    private void DisplayMapDataSearch(final ArrayList<ServiceProviderData> CommonData) {

        Log.d("System out", "Size of CommonData:==  " + CommonData.size());
        if (infoWindow != null) {
            infoWindow.removeAllViews();
        }

        if (googleMap!=null){
            googleMap.clear();
        }

        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_home)).getMap();
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMapToolbarEnabled(false);


        if (PMarker != null) {
            PMarker.remove();
            googleMap.clear();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setTrafficEnabled(true);

        }

        if (allMarkersMap.size() > 0) {
            allMarkersMap.clear();
        }
        b = new LatLngBounds.Builder();

        Log.d("System out", "latitude: " + latitude);

        Marker mMarker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_black))
                //  .title(markername.get(i))
                .anchor(0.5f, 1));

        b.include(mMarker.getPosition());

        allMarkersMap.put(mMarker, 1000);

        Log.d("System out","commdata size:"+CommonData.size());
        if (CommonData.size()>0){
            for ( int i = 0; i < CommonData.size(); i++) {
                View marker1 = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
                user_chg_pro_image_update1 = (CircularImageView) marker1.findViewById(R.id.user_chg_pro_image_update);
                double Lattitude = 0.0;
                double Longitude = 0.0;


                Lattitude = Double.parseDouble(CommonData.get(i).getUser_Latitude());
                Longitude = Double.parseDouble(CommonData.get(i).getUser_Longitude());

                String   Dp = CommonData.get(i).getSub_Service_Image();
                if (TextUtils.isEmpty(Dp)) {
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.plasholder_75);
                    user_chg_pro_image_update1.setImageBitmap(largeIcon);
                } else {
                    try {
                        locationurl = BeanConstants.customer_image_subservice + Dp+"&h=50";
                        Log.d("System out", "locationurl url: " + locationurl);
                        Picasso.with(MapActivity.this)
                                .load(locationurl.trim())
                                .noFade()
                                .into(user_chg_pro_image_update1);

                        mThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.d("System out", "in thread");
                                            if (!flagmarker){
                                                flagmarker=true;
                                                user_chg_pro_image_update1.setImageBitmap(null);
                                                mThread.interrupt();
                                                googleMap.clear();
                                                DisplayMapDataSearch(ProviderList);

                                            }

                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        mThread.start();

                        // String locationurl1 = BeanConstants.customer_image_subservice + CommonData.get(0).getSub_Service_Image() + "&h=80";

                        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            @Override
                            public View getInfoWindow(Marker marker) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.mapdialog, null);
                                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
                                final CircularImageView img = (CircularImageView) infoWindow.findViewById(R.id.map_img);
                                final MyTextView providername = (MyTextView) infoWindow.findViewById(R.id.map_providername);
                                final MyTextView servicename = (MyTextView) infoWindow.findViewById(R.id.map_servicename);
                                servicename.setText(CommonData.get(0).getSub_Service_Name());
                                providername.setText(CommonData.get(0).getUser_First_Name() + " " + CommonData.get(0).getUser_Last_Name());
                                Log.d("System out", "locationurl url: " + locationurl);
                                Picasso.with(MapActivity.this)
                                        .load(locationurl.trim())
                                        .placeholder(R.drawable.plasholder_75)
                                        .into(img);

                                return infoWindow;
                            }
                        });

                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        Toast.makeText(MapActivity.this, "Could not post image due to insufficient storage memory", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                PMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Lattitude, Longitude))
                        .title(sd)
                        .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker1))));
                b.include(PMarker.getPosition());
                        /*.anchor(0.5f, 1));*/
                allMarkersMap.put(PMarker, i);
                Log.d("System out", "PMarker" + PMarker + " i" + i);
                LatLngBounds bounds = b.build();
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 100, 100, 0);
                googleMap.animateCamera(cu);
            }
        }else{
            Toast.makeText(MapActivity.this, "Sorry! Currently provider is not available.", Toast.LENGTH_LONG).show();
        }

        getAddress();


    }

    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Log.d("System out", "width :" + view.getMeasuredWidth());
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}

