package com.allegra.handyuvisa;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allegra.handyuvisa.async.AirportCodes;
import com.allegra.handyuvisa.async.AsyncRestHelper;
import com.allegra.handyuvisa.async.AsyncTaskMPosResultEvent;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Util;
import com.squareup.otto.Subscribe;
import com.squareup.timessquare.CalendarPickerView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class HotelsActivity extends FrontBackAnimate  implements FrontBackAnimate.InflateReadyListener  {

    public static final int REQUEST_CODE_DESTINATION = 1;
    private static final int MINIMUN_AGE_CHILDREN= 2, MAXIMUM_AGE_CHILDREN= 11, MAXIMUM_GUESS_IN_ROOM= 7;
    private final String TAG = "HotelsActivity";
    private String paramDestination="", paramDestinationName = "", paramCheckIn = "", paramCheckOut = "";
    private int paramRooms = 1;
    //Data for persist
    private int paramRoomsTotal = 1,paramAdultsTotal = 1, paramChildrenTotal = 0,paramInfantsTotal = 0;
    private int paramAdults = 1, paramAdults2 = 0, paramAdults3 = 0, paramAdults4 = 0;
    private int paramChildren = 0, paramChildren2 = 0, paramChildren3 = 0, paramChildren4 = 0;
    private int paramInfants = 0, paramInfants2 = 0, paramInfants3 = 0,paramInfants4 = 0;
    private View datesLayout, searchLayout, roomsLayout, roomsHeader, roomsGuestHeader,
            llheader, customHotelsHeader, roomsResume, roomsLayoutForSelect;
    private Button searchButton;
    public static final String IATA = "IATA";
    private int count = 0;
    private ViewGroup parent;
    LayoutInflater layoutInflater;
    private int hotelsType = 1, globalSizeOfRooms = 0, counter = 0;
    LinearLayout addRoomsBtn,linLayDateBegin;
    private LinearLayout addRoomOptionPanel, roomsContainer;
    private NumberPicker adultsPicker, childrenPicker, infantsPicker;
    private static String url_hotels = "http://quickbooking.azurewebsites.net/Api/QuickSearch/Destination/bog?id=";
    //private static String url_hotels = "http://alegra.dracobots.com/Hotel/Flow/Availability?cityHotel=Miami&cityHotelHidden=MIA&arrivalHotel=05%2F14%2F2016&departureHotel=05%2F22%2F2016&roomHotel=1&adultHotel1=1&childHotel1=0&language=es-CO&PaymentMethod=OnePocket";
    private ListView listView;
    private SearchAdapterHotels adapter;
    private ArrayList<HotelsActivity.AirportData> airportData;
    private ProgressBar progressBar;
    private boolean alreadyEnterToThisMethod, enterToCancelDateSelector, enterToCancelRoomsSelector = true,
            enterToNextDayCheckout = false;
    private TextView  dateBegin, yearBegin, dateEnd, yearEnd;
    private Date dateBeginSelected, dateEndSelected;//Persist selection in Calendar
    private ImageView headerSeparator;
    private static final int TEXT_RETURN_DATE_SIZE = 32;//TextSize in sp
    private static final int TEXT_ONE_WAY_SIZE = 22;//TextSize in sp
    int counterEvenDateSelected = 0;

    //INNER CLASSES

    public class AirportData {
        private String codeIATA;
        private String country;
        private String city;
        private String name;
        public int type=0;

        public AirportData(String codeIATA, String country, String city, String name, int type) {
            this.codeIATA = codeIATA;
            this.country = country;
            this.city = city;
            this.name = name;
            this.type=type;
        }

        public String getCodeIATA() {return codeIATA;}
        public String getCountry() {return country;}
        public String getCity() {return city;}
        public String getName() {return name;}
        public String getListInformation(){
            String info="";
            if(type== hotelsType){
                info=getName();
            }
            return info;
        }
    }

    //OVERRIDE METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.gc();
        setView(R.layout.fragment_hotels, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void initViews(View root) {
        initializePanels(root);
    }

    @Override
    protected void onDestroy() {
       // MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    //PROPER METHODS



    /**
     * Called when there are 3 o more characters wrote in City or Airport EditText Search
     * @param query
     */
    private void performSearch(String query) {
        Log.d(TAG, "performSearch");
        progressBar.setVisibility(View.VISIBLE);
        AirportCodes apiInfo = new AirportCodes(query,url_hotels);
        Log.d("Query",query);
        AsyncRestHelper helper = new AsyncRestHelper(apiInfo);
        helper.execute();
    }

    // Initialize views
    private void initializePanels(final View view){

        searchLayout = view.findViewById(R.id.search_layout_hotels);
        datesLayout = view.findViewById(R.id.panel_dates);
        llheader = view.findViewById(R.id.ll_header_hotels);
        customHotelsHeader = view.findViewById(R.id.ll_destination);
        headerSeparator = (ImageView) view.findViewById(R.id.iv_header_hotels);

        //Set initial Date
        TextView checkIn = (TextView)view.findViewById(R.id.textDeparture);
        TextView checkOut = (TextView)view.findViewById(R.id.textReturn);
        dateBegin = (TextView)view.findViewById(R.id.textDepartureDay);
        yearBegin = (TextView)view.findViewById(R.id.textDepartureMonth);
        dateEnd = (TextView) view.findViewById(R.id.textReturnDay);
        yearEnd = (TextView) view.findViewById(R.id.textReturnMonth);
        linLayDateBegin = (LinearLayout)view.findViewById(R.id.linLayDateBegin);
        checkIn.setText("CHECKIN");
        checkOut.setText("CHECKOUT");
        setDateSelector();

        roomsHeader = view.findViewById(R.id.relHeaderCancelAndSave);
        roomsGuestHeader = view.findViewById(R.id.magentaHeaderRooms);
        roomsGuestHeader.setVisibility(View.VISIBLE);
        roomsHeader.setVisibility(View.GONE);
        roomsLayout =  view.findViewById(R.id.rooms_panel);
        searchButton = (Button) view.findViewById(R.id.btn_search);
        roomsResume = roomsLayout.findViewById(R.id.show_resume_rooms);

        searchButton.setTypeface(Typeface.createFromAsset(getAssets(),getString(R.string.font_muli)));
        addRoomOptionPanel = (LinearLayout) view.findViewById(R.id.add_room_option);
        addRoomsBtn = (LinearLayout) view.findViewById(R.id.add_room_option);
        roomsContainer = (LinearLayout) view.findViewById(R.id.rooms_container);
        paramAdultsTotal = paramAdults;
        paramChildrenTotal = paramChildren;
        paramInfantsTotal = paramInfants;
        //Hide views and show Rooms Selector View
        roomsResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectGuestInRooms();
                //I need a flag for save data selected about rooms and re inflate them
                //if (enterToCancelRoomsSelector){//Btn Cancel pressed: There is nothing selected previously
                    //selectGuestInRooms();
                Log.d("paramRoomsTotal",String.valueOf(paramRoomsTotal));
                Log.d("paramAdultsTotal",String.valueOf(paramAdultsTotal));
                Log.d("paramChildrenTotal",String.valueOf(paramChildrenTotal));
                Log.d("paramInfantsTotal",String.valueOf(paramInfantsTotal));

                    if (paramRoomsTotal == 1 && paramAdultsTotal == 1 && paramChildrenTotal == 0 && paramInfantsTotal == 0) {
                        roomsContainer.removeAllViews();
                        addRoomToPanel();
                    }

                //}
                else {//Btn Save pressed: Re draw rooms pre selected
                    roomsContainer.setVisibility(View.VISIBLE);
                    if (roomsContainer.getChildCount() == 4) addRoomsBtn.setVisibility(View.GONE);
                }

            }
        });
        layoutInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

        addRoomsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addRoomToPanel();
                if (roomsContainer.getChildCount() == 4) addRoomsBtn.setVisibility(View.GONE);
            }
        });

        listenerForCancelRoomsHotel(view);

        listenerForSaveRoomsHotel(view);

        //Fix a bug
        ImageView imgDestinationText = (ImageView)view.findViewById(R.id.selec_destination);
        imgDestinationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetDestinationHotels();
            }
        });

    }

    public void listenerForSaveRoomsHotel(final View mView){

        ImageButton btnSave = (ImageButton)mView.findViewById(R.id.btn_save_hotels_rooms_original_2);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Juan"," Entra a Save on Click ");

                persistGuestsPerRoomSelected();

                roomsContainer.setVisibility(View.GONE);
                roomsLayoutForSelect.setVisibility(View.GONE);
                addRoomOptionPanel.setVisibility(View.GONE);

                llheader.setVisibility(View.VISIBLE);
                roomsGuestHeader.setVisibility(View.VISIBLE);
                datesLayout.setVisibility(View.VISIBLE);
                llheader.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                customHotelsHeader.setVisibility(View.VISIBLE);
                roomsResume.setVisibility(View.VISIBLE);
                roomsLayout.setVisibility(View.VISIBLE);
                headerSeparator.setVisibility(View.VISIBLE);

                enterToCancelRoomsSelector = false;
                initializePanelsAfterCancelDateSelector();
                addRoomsPreviouslySelectedToPanel();
            }
        });
    }

    //Button Cancel Guests per Room pressed
    public void listenerForCancelRoomsHotel (final View mView){
        Log.d("Cancel Listener","Canceling");
        ImageButton btnCancel = (ImageButton)mView.findViewById(R.id.btn_cancel_hotels_rooms_for_select);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {

                roomsContainer.setVisibility(View.GONE);
                roomsLayoutForSelect.setVisibility(View.GONE);
                addRoomOptionPanel.setVisibility(View.GONE);

                llheader.setVisibility(View.VISIBLE);
                roomsGuestHeader.setVisibility(View.VISIBLE);
                datesLayout.setVisibility(View.VISIBLE);
                llheader.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                customHotelsHeader.setVisibility(View.VISIBLE);
                roomsResume.setVisibility(View.VISIBLE);
                roomsLayout.setVisibility(View.VISIBLE);
                headerSeparator.setVisibility(View.VISIBLE);

                enterToCancelRoomsSelector = true;
                //initializePanelsAfterCancelDateSelector();
                addRoomsPreviouslySelectedToPanel();
            }
        });
    }

    public void initializePanelsAfterCancelDateSelector(){
        roomsHeader.setVisibility(View.GONE);
        counterEvenDateSelected = 0;

        if (enterToCancelDateSelector) {
            setDateSelectorAfterInflateCalendar();
            //enterToCancelDateSelector = false;
        }
        else {
            setDateSelector();

        }

    }

    //Show Guests Per rooms previously selected
    public void addRoomsPreviouslySelectedToPanel(){

        if (!enterToCancelRoomsSelector){//Show value for respective adults, children and infants

            TextView textRoomsForEachGuest = (TextView) findViewById(R.id.room_title);
            String numberOfRooms = "", numberOfAdults= "", numberOfChildren = "", numberOfInfants = "";
            //Validate numberOfRooms
            //PERSIST
            paramRoomsTotal =  paramRooms;
            //*******************************
            if (paramRooms>1){
                numberOfRooms = String.valueOf(paramRooms)+" "+getString(R.string.room)+ ", ";
            }else{
                numberOfRooms = String.valueOf(paramRooms)+" "+getString(R.string.txt_single_room)+ ", ";
            }
            //Validate number of Adults
            if (paramAdultsTotal>1){//Plural
                numberOfAdults = String.valueOf(paramAdultsTotal)+" "+getString(R.string.txt_lbl_adults);
            }else{//Singular
                numberOfAdults = String.valueOf(paramAdultsTotal)+" "+getString(R.string.text_single_adult);
            }
            //Validate if number of children is 0
            if (paramChildrenTotal>0){
                if (paramChildrenTotal>1){//Plural
                    numberOfChildren = ", "+String.valueOf(paramChildrenTotal)+" "+getString(R.string.txt_lbl_child);
                }else{//Singular
                    numberOfChildren = ", "+String.valueOf(paramChildrenTotal)+" "+getString(R.string.text_single_child);
                }
            }
            //Validate if number of infants is 0
            if (paramInfantsTotal>0){
                if (paramInfantsTotal>1){//Plural
                    numberOfInfants = ", "+String.valueOf(paramInfantsTotal)+" "+getString(R.string.txt_lbl_infants);
                }else{//Singular
                    numberOfInfants = ", "+String.valueOf(paramInfantsTotal)+" "+getString(R.string.text_single_infant);
                }
            }
            textRoomsForEachGuest.setText(numberOfRooms+numberOfAdults+numberOfChildren+numberOfInfants);

        } else {//Default values
            TextView textRoomsForEachGuest = (TextView) findViewById(R.id.room_title);
            Log.d("Total Rooms TEst",String.valueOf(paramRoomsTotal));
            Log.d("paramAdultsTotal TEst",String.valueOf(paramAdultsTotal));
            Log.d("paramChildrenTotal TEst",String.valueOf(paramChildrenTotal));
            Log.d("paramInfantsTotal",String.valueOf(paramInfantsTotal));
            //textRoomsForEachGuest.setText(R.string.room_and_guests_title);

        }
    }

    public void setDateSelector(){

        Calendar now = Calendar.getInstance();
        Date today = now.getTime();

        dateBegin.setText(Util.Day_Formatter.format(today).toUpperCase());
        yearBegin.setText(Util.M_Y_Formatter.format(today).toUpperCase());
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);
        Date tomorrow = gc.getTime();
        //Margin Top for dateEnd TextView
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,0);
        dateEnd.setLayoutParams(params);
        dateEnd.setText(Util.Day_Formatter.format(tomorrow).toUpperCase());
        dateEnd.setTextColor(getResources().getColor(R.color.magenta));
        dateEnd.setTextSize(32);
        yearEnd.setText(Util.M_Y_Formatter.format(tomorrow).toUpperCase());
    }

    public void setDateSelectorAfterInflateCalendar(){

        counterEvenDateSelected = 0;
        dateBegin.setText(Util.Day_Formatter.format(dateBeginSelected).toUpperCase());
        yearBegin.setText(Util.M_Y_Formatter.format(dateBeginSelected).toUpperCase());
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);

        dateEnd.setText(Util.Day_Formatter.format(dateEndSelected).toUpperCase());
        dateEnd.setTextColor(getResources().getColor(R.color.magenta));
        dateEnd.setTextSize(32);
        yearEnd.setText(Util.M_Y_Formatter.format(dateEndSelected).toUpperCase());
    }

    private void selectGuestInRooms(){
        //Hide Views
        llheader.setVisibility(View.GONE);
        roomsGuestHeader.setVisibility(View.GONE);
        datesLayout.setVisibility(View.GONE);
        llheader.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);
        customHotelsHeader.setVisibility(View.GONE);
        roomsResume.setVisibility(View.GONE);
        roomsLayout.setVisibility(View.GONE);
        headerSeparator.setVisibility(View.GONE);

        roomsLayoutForSelect = findViewById(R.id.header_for_select);
        roomsLayoutForSelect.setVisibility(View.VISIBLE);
        addRoomOptionPanel.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_DESTINATION) {
                paramDestination = data.getStringExtra(IATA);
                paramDestinationName = data.getStringExtra("NameCity");
                Log.d("IATA EditText",paramDestination);
            }
        }

    }

    //Get up animation for flights components
    public void onGetUpAnimationHotels(){

        searchLayout.setVisibility(View.GONE);
        datesLayout.setVisibility(View.VISIBLE);
        roomsHeader.setVisibility(View.VISIBLE);
        roomsLayout.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        //if keyboard is open, close it.
        EditText edtSearch = (EditText) findViewById(R.id.et_search);
        InputMethodManager imm = (InputMethodManager)this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

    }
    /**
     * Called when imageView select_destination is clicked
     */

    public void onGetDestinationHotels() {

        searchLayout.setVisibility(View.VISIBLE);
        //Bring EditText for search  hotel
        searchLayout.setVisibility(View.VISIBLE);
        datesLayout.setVisibility(View.GONE);
        roomsHeader.setVisibility(View.GONE);
        roomsLayout.setVisibility(View.GONE);
        searchButton.setVisibility(View.GONE);


        LinearLayout linLayGetUp = (LinearLayout)findViewById(R.id.linLayGetUpAnimationFlights);
        linLayGetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetUpAnimationHotels();
                roomsHeader.setVisibility(View.GONE);
            }
        });

        airportData = new ArrayList<>();
        listView = (ListView)findViewById(R.id.list);
        adapter = new SearchAdapterHotels(this, airportData);
        listView.setAdapter(adapter);

        progressBar = (ProgressBar)findViewById(R.id.pb_search);

        final EditText note = (EditText)findViewById(R.id.et_search);
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().length()>2) {
                    performSearch(s.toString());
                }
            }
        });
        note.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    performSearch(note.getText().toString());
                }
                return false; // pass on to other listeners.
            }
        });
        if (!alreadyEnterToThisMethod) MyBus.getInstance().register(this);
        alreadyEnterToThisMethod = true;
        setOnItemclickListenerListView(listView);

    }


    /**
     * When a hotel is onclicked, set its name in the header
     * @param listView
     */
    public void setOnItemclickListenerListView (final ListView listView){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.d("List Hotels",String.valueOf(i) + " "+String.valueOf(l));
                HotelsActivity.AirportData airportData = (AirportData)adapterView.getItemAtPosition(i);
                Log.d("List", String.valueOf(airportData.getCity())+ " "+ String.valueOf(airportData.getName()));

                ImageView imgSelectDestination = (ImageView) findViewById(R.id.selec_destination);
                imgSelectDestination.setVisibility(View.GONE);
                TextView txtSElectDestination = (TextView)findViewById(R.id.txt_select_your_destination);
                txtSElectDestination.setVisibility(View.GONE);

                View linLayDestinationHotels = findViewById(R.id.ll_destination_hotels);
                linLayDestinationHotels.setVisibility(View.VISIBLE);
                TextView txtHotelName = (TextView)findViewById(R.id.txtDestinationName);
                txtHotelName.setText(String.valueOf(airportData.getName()));
                paramDestination = airportData.getCodeIATA();
                TextView txtHotelCityAndCountry = (TextView)findViewById(R.id.txtDestinationCityAndCountry);
                txtHotelCityAndCountry.setText(String.valueOf(airportData.getName()).toUpperCase() + ", "+ String.valueOf(airportData.getCity()).toUpperCase());
                searchLayout.setVisibility(View.GONE);
                datesLayout.setVisibility(View.VISIBLE);
                roomsGuestHeader.setVisibility(View.VISIBLE);
                roomsHeader.setVisibility(View.GONE);//Magenta text cancel and save buttons
                roomsLayout.setVisibility(View.VISIBLE);
                searchButton.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager)HotelsActivity.this
                        .getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(listView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        RelativeLayout linLayDestinationHotels = (RelativeLayout)findViewById(R.id.ll_destination);//_hotels
        linLayDestinationHotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetDestinationHotels();
                roomsHeader.setVisibility(View.GONE);
            }
        });

    }

    private void setDividerColor (NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.black));
                     pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    /*
     * CalendarPickerView - library with Apache License 2.0
     * https://github.com/square/android-times-square
     */

    public void onGetSchedule(View view) {//Uses custom_date_selector_without_onclick

        final Dialog dialog = new Dialog(this, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_date_picker_custom);

        final TextView dateBegin = (TextView) dialog.findViewById(R.id.textDepartureDayWithoutOnClick);
        final TextView yearBegin = (TextView) dialog.findViewById(R.id.textDepartureMonthWithoutOnClick);
        final TextView dateEnd = (TextView) dialog.findViewById(R.id.textReturnDayWithoutOnClick);
        final TextView yearEnd = (TextView) dialog.findViewById(R.id.textReturnMonthWithoutOnClick);
        final TextView yearReturnEnd = (TextView) dialog.findViewById(R.id.textReturnYearWithoutOnClick);

        yearReturnEnd.setVisibility(View.GONE);
        TextView checkIn = (TextView) dialog.findViewById(R.id.textDepartureWithoutOnClick);
        checkIn.setText("CHECKIN");
        TextView checkOut = (TextView) dialog.findViewById(R.id.textReturnWithoutOnClick);
        checkOut.setText("CHECKOUT");

        //If there is not checkin and checkout dates pre selected, Today = Checkin,
        // Tomorrow = Checkout.
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        if (dateBeginSelected!=null){
            dateBegin.setText(Util.Day_Formatter.format(dateBeginSelected).toUpperCase());
            yearBegin.setText(Util.M_Y_Formatter.format(dateBeginSelected).toUpperCase());
        }else {
            dateBegin.setText(Util.Day_Formatter.format(today).toUpperCase());
            yearBegin.setText(Util.M_Y_Formatter.format(today).toUpperCase());
        }
        //Tomorrow date
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);
        Date tomorrow = gc.getTime();

        final CalendarPickerView calendar = (CalendarPickerView) dialog.findViewById(R.id.cv_duration);

        if (dateEndSelected != null){
            dateEnd.setText(Util.Day_Formatter.format(dateEndSelected).toUpperCase());
            yearEnd.setText(Util.M_Y_Formatter.format(dateEndSelected).toUpperCase());

            now.add(Calendar.YEAR, 1);
            calendar.init(today, now.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE);

        }else {
            dateEnd.setText(Util.Day_Formatter.format(tomorrow).toUpperCase());
            yearEnd.setText(Util.M_Y_Formatter.format(tomorrow).toUpperCase());
            //New collection
            List<Date> collection = new ArrayList<Date>();
            collection.add(today);
            collection.add(tomorrow);
            now.add(Calendar.YEAR, 1);


            calendar.init(today, now.getTime())
                    .inMode(CalendarPickerView.SelectionMode.RANGE)
                    .withSelectedDates(collection);
        }
        dateEnd.setTextColor(getResources().getColor(R.color.magenta));
        dateEnd.setTextSize(32);



        counterEvenDateSelected = 0;

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                List<Date> dates = calendar.getSelectedDates();
                int size = dates.size();
                //Set Begin date
                dateBegin.setText(Util.Day_Formatter.format(dates.get(0)).toUpperCase());
                yearBegin.setText(Util.M_Y_Formatter.format(dates.get(0)).toUpperCase());

                Date dateFirstSelected = dates.get(0);

                if (size > 1) {//Check in and Check out

                    dateEnd.setText(Util.Day_Formatter.format(dates.get(size - 1)).toUpperCase());
                    dateEnd.setTextColor(getResources().getColor(R.color.magenta));
                    dateEnd.setTextSize(TEXT_RETURN_DATE_SIZE);
                    yearEnd.setText(Util.M_Y_Formatter.format(dates.get(size - 1)).toUpperCase());
                    counterEvenDateSelected = 0;
                    enterToNextDayCheckout = false;
                }
                else if (size == 1) {

                    counterEvenDateSelected ++;
                    if ((counterEvenDateSelected & 1) == 0) {//Even: force it Check out as Next Day date
                        enterToNextDayCheckout = true;
                        //Toast.makeText(getApplicationContext(), "PAr", Toast.LENGTH_LONG).show();
                        Calendar c = Calendar.getInstance();
                        c.setTime(dateFirstSelected);
                        c.add(Calendar.DATE, 1);
                        dateFirstSelected = c.getTime();
                        dateEnd.setText(Util.Day_Formatter.format(dateFirstSelected).toUpperCase());
                        dateEnd.setTextColor(getResources().getColor(R.color.magenta));
                        dateEnd.setTextSize(TEXT_RETURN_DATE_SIZE);
                        yearEnd.setText(Util.M_Y_Formatter.format(dateFirstSelected).toUpperCase());
                    } else{// Odd: "Select date" for Check out Date
                        //Toast.makeText(getApplicationContext(), "imPAr", Toast.LENGTH_LONG).show();
                        dateEnd.setText(R.string.txt_select_date);//Util.D_M_Formatter.format(dates.get(0))
                        dateEnd.setTextColor(getResources().getColor(R.color.gray));
                        dateEnd.setTextSize(TEXT_ONE_WAY_SIZE);
                        yearEnd.setText(R.string.txt_month_year_selector_flights);//Util.Y_Formatter.format(dates.get(0))
                    }
                }
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });


        ImageButton save = (ImageButton) dialog.findViewById(R.id.btn_save_date_picker);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Entra","a save");
                List<Date> dates = calendar.getSelectedDates();
                int size = dates.size();
                //If size >1: take dates.get(0) and  dates.get(size - 1)
                //else take today and tomorrow dates

                if (size >1) {//dates.size() > 0 && !enterToNextDayCheckout
                    /*Date tomorrow = new Date();
                    Calendar c = Calendar.getInstance();
                    c.setTime(dates.get(0));
                    c.add(Calendar.DATE, 1);
                    tomorrow = c.getTime();*/
                    HotelsActivity.this.updateViews(dates.get(0), dates.get(size - 1));
                    //HotelsActivity.this.updateViews(dates.get(0), dates.get(size - 1));
                    //To persist
                    dateBeginSelected = dates.get(0);
                    dateEndSelected = dates.get(size - 1);//tomorrow
                } else {
                    if (dates.size()>0) {
                        Date tomorrow = new Date();
                        Calendar c = Calendar.getInstance();
                        c.setTime(dates.get(0));
                        c.add(Calendar.DATE, 1);
                        tomorrow = c.getTime();
                        HotelsActivity.this.updateViews(dates.get(0), tomorrow);
                        //To persist
                        dateBeginSelected = dates.get(0);
                        dateEndSelected = tomorrow;

                    }
                }
                dialog.dismiss();
                enterToCancelDateSelector = true;
            }
        });

        ImageButton cancel = (ImageButton) dialog.findViewById(R.id.btn_cancel_date_picker);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Entra","a cancel");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*Sample URL:
       http://quickbooking.azurewebsites.net/Hotel/Flow/Availability?cityHotel=Miami&cityHotelHidden=MIA&roomHotel=1&adultHotel=1&childHotel=0&arrivalHotel=01%2F21%2F2016&departureHotel=01%2F24%2F2016) */

    //Perform call to HotelSearchActivity
    public void onSearchHotels(View view) {

        //Get  destination initial values in order to search hotels
        TextView destination = (TextView)findViewById(R.id.txtDestinationName);
        TextView destinationCode = (TextView)findViewById(R.id.txtDestinationCityAndCountry);
        paramDestinationName = destination.getText().toString();
        //paramDestination = destinationCode.getText().toString();
        //Get checkIn and checkOut initial values in order to search hotels
        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, 1);
        Date tomorrow = gc.getTime();
        //********Validate Params************
        if (paramCheckIn.equals("")&&paramCheckOut.equals("")){
            paramCheckIn = Util.Bookings_Formatter_Hotels.format(today);
            paramCheckOut = Util.Bookings_Formatter_Hotels.format(tomorrow);
        }

        /*if (!paramDestination.equals("")) {
            paramDestination = paramDestination.substring(0, 3);
        }*/
        Log.d("CodigoIATAonSearchHotel",paramDestination);

        if (paramDestination.equals("")) {
            onalertDialogDepartureOrArriveNotSelected();
        } else {
            Intent intent = new Intent(this, HotelSearchActivity.class);
            intent.putExtra("&cityHotel", paramDestinationName);
            intent.putExtra("&cityHotelHidden", paramDestination);
            intent.putExtra("&arrivalHotel", paramCheckIn);
            intent.putExtra("&departureHotel", paramCheckOut);
            intent.putExtra("&roomHotel", paramRooms);
            intent.putExtra("&adultHotel1", paramAdults);
            paramChildren = paramChildren+paramInfants;
            intent.putExtra("&childHotel1", paramChildren);

            switch(paramRooms){

                case 2:
                    intent.putExtra("&adultHotel2", paramAdults2);
                    intent.putExtra("&childHotel2", paramChildren2);
                    break;
                case 3:
                    intent.putExtra("&adultHotel2", paramAdults2);
                    intent.putExtra("&childHotel2", paramChildren2);
                    intent.putExtra("&adultHotel3", paramAdults3);
                    intent.putExtra("&childHotel3", paramChildren3);
                    break;
                case 4:
                    intent.putExtra("&adultHotel2", paramAdults2);
                    intent.putExtra("&childHotel2", paramChildren2);
                    intent.putExtra("&adultHotel3", paramAdults3);
                    intent.putExtra("&childHotel3", paramChildren3);
                    intent.putExtra("&adultHotel4", paramAdults4);
                    intent.putExtra("&childHotel4", paramChildren4);
                    break;
            }

            //TODO: Here are totals
        Log.d("adult", String.valueOf(paramAdultsTotal));
        Log.d("children", String.valueOf(paramChildren));
        Log.d("infant", String.valueOf(paramInfants));
        Log.d("CheckIn", String.valueOf(paramCheckIn));
        Log.d("CheckOut", String.valueOf(paramCheckOut));
        Log.d("roomHotel", String.valueOf(paramRooms));
        Log.d("cityHotel", String.valueOf(paramDestinationName));
        Log.d("cityHotelHidden", String.valueOf(paramDestination));

        startActivity(intent);
            System.gc();
    }

    }

    public void onMenu(View view) {
        animate();
    }

    private void  addRoomToPanel(){

            final int count =roomsContainer.getChildCount();
            headerSeparator.setVisibility(View.GONE);
            roomsGuestHeader.setVisibility(View.VISIBLE);
            final View room = layoutInflater.inflate(R.layout.hotels_rooms_item_test, null);

            TextView title = (TextView) room.findViewById(R.id.room_title);
            String tmp = String.format(getString(R.string.rooms_title), String.valueOf((count + 1)));
            title.setText(tmp);

            ImageView removeRoom = (ImageView) room.findViewById(R.id.remove_room_item);
            if (count > 0) {
            removeRoom.setVisibility(View.VISIBLE);
            }
            removeRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    roomsContainer.removeView(room);
                    updateNumberOfLastRoom();
                    addRoomsBtn.setVisibility(View.VISIBLE);
                }
            });

            final LinearLayout guestsContainers = (LinearLayout) room.findViewById(R.id.custom_adults_children_text);
            guestsContainers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Get textView of reference for number room
                    TextView text = (TextView)room.findViewById(R.id.room_title);
                    String numberOfRoom = String.valueOf(text.getText());
                    int index = numberOfRoom.indexOf(" ");

                    String number = numberOfRoom.substring(index+1,index+2);
                    int  numberRoom = Integer.parseInt(number);
                    //Get textViews of reference for adults, children and infants values pre selected
                    text = (TextView)room.findViewById(R.id.adultsTextView);
                    String paramString = String.valueOf(text.getText());
                    int  adults = Integer.parseInt(paramString);
                    Log.d("adults",paramString);

                    text = (TextView)room.findViewById(R.id.childrenTextView);
                    paramString = String.valueOf(text.getText());
                    int  children = Integer.parseInt(paramString);

                    text = (TextView)room.findViewById(R.id.infantsTextView);
                    paramString = String.valueOf(text.getText());
                    int  infants = Integer.parseInt(paramString);

                    onAlertDialogGuestPickers(guestsContainers, numberRoom, adults, children, infants);
                }
            });
            setNumberPickersTest(room);

    }

    //Persist Guests Per rooms selected data when Save button is pressed
    public void persistGuestsPerRoomSelected(){
        paramAdults = 0;
        paramChildren = 0;
        paramInfants = 0;

        paramChildren2 = 0;
        paramAdults2 = 0;
        paramInfants2 = 0;

        paramAdults3 = 0;
        paramChildren3 = 0;
        paramInfants3 = 0;

        paramAdults4 = 0;
        paramChildren4 =0;
        paramInfants4 = 0;

        int totalTemporalOfRooms = roomsContainer.getChildCount();
        View v;
        //int paramAdultsTotal2 = 0, paramChildrenTotal2 = 0, paramInfantsTotal2 = 0;

        for (short j = 0; j < totalTemporalOfRooms; j++){
            v = roomsContainer.getChildAt(j);
            TextView textAdults = (TextView) v.findViewById(R.id.adultsTextView);
            TextView textChildren = (TextView)v.findViewById(R.id.childrenTextView);
            TextView textInfants = (TextView)v.findViewById(R.id.infantsTextView);
            String valueOfAdults = textAdults.getText().toString();
            String valueOfChildren = textChildren.getText().toString();
            String valueOfInfants = textInfants.getText().toString();
            //Assign respective guests for each room
            switch (j){
                case 0://Room 1
                    paramAdults = Integer.parseInt(valueOfAdults);
                    paramChildren = Integer.parseInt(valueOfChildren);
                    paramInfants = Integer.parseInt(valueOfInfants);


                    break;
                case 1://Room 2
                    paramAdults2 = Integer.parseInt(valueOfAdults);
                    paramChildren2 = Integer.parseInt(valueOfChildren);
                    paramInfants2 = Integer.parseInt(valueOfInfants);
                    break;
                case 2://Room 3
                    paramAdults3 = Integer.parseInt(valueOfAdults);
                    paramChildren3 = Integer.parseInt(valueOfChildren);
                    paramInfants3 = Integer.parseInt(valueOfInfants);
                    break;
                case 3://Room 4
                    paramAdults4 = Integer.parseInt(valueOfAdults);
                    paramChildren4 = Integer.parseInt(valueOfChildren);
                    paramInfants4 = Integer.parseInt(valueOfInfants);
                    break;
            }

            Log.d("T adults",valueOfAdults);
            Log.d("T Children",valueOfChildren);
            Log.d("T Infants",valueOfInfants);

        }
        paramRooms = totalTemporalOfRooms;
        paramAdultsTotal = paramAdults + paramAdults2 + paramAdults3 + paramAdults4;
        paramChildrenTotal = paramChildren + paramChildren2 + paramChildren3 + paramChildren4;
        paramInfantsTotal = paramInfants + paramInfants2 + paramInfants3 + paramInfants4;
        Log.d("paramAdultsTotal",String.valueOf(paramAdultsTotal));
        Log.d("paramChildrenTotal",String.valueOf(paramChildrenTotal));
        Log.d("paramInfantsTotal",String.valueOf(paramInfantsTotal));
        Log.d("paramRooms",String.valueOf(paramRooms));

    }

    //Update numbers when Remove Room button is pressed
    public void updateNumberOfLastRoom(){

        int totalTemporalOfRooms = roomsContainer.getChildCount();
        View v;
        for (short j = 0; j < totalTemporalOfRooms; j++){
            v = roomsContainer.getChildAt(j);
            TextView title = (TextView) v.findViewById(R.id.room_title);
            String tmp = String.format(getString(R.string.rooms_title), String.valueOf((j + 1)));
            title.setText(tmp);
        }
    }

    //Show dialog for each room, in order to select adults, children and infants for that one.
    public void onAlertDialogGuestPickers(final View view,  int number, int adults, int children, int infants){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_number_picker_dialog_hotels);
        dialog.show();
        adultsPicker = (NumberPicker)dialog.findViewById(R.id.adultsNumberPicker);
        childrenPicker = (NumberPicker) dialog.findViewById(R.id.childrenNumberPicker);
        infantsPicker = (NumberPicker) dialog.findViewById(R.id.infantsNumberPicker);
        Log.d("adults in onAlertDialog",String.valueOf(adults));
        setNumberPickersTest2(adults, children, infants);

        TextView title = (TextView)dialog.findViewById(R.id.textTitle);
        String tmp = String.format(getString(R.string.rooms_title), String.valueOf((number)));
        title.setText(tmp);

        TextView textCancel = (TextView)dialog.findViewById(R.id.textCancelDialog);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view3) {
                dialog.dismiss();
                updateTextsOfGuest(view);
            }
        });
        TextView textOk = (TextView)dialog.findViewById(R.id.textOkDialog);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                dialog.dismiss();
                updateTextsOfGuest(view);
            }
        });

    }

    /* Method for set values */

    public void updateTextsOfGuest(View view){

        TextView textAdults = (TextView) view.findViewById(R.id.adultsTextView);
        TextView textChildren = (TextView)view.findViewById(R.id.childrenTextView);
        TextView textInfants = (TextView)view.findViewById(R.id.infantsTextView);
        textAdults.setText(String.valueOf(adultsPicker.getValue()));
        textChildren.setText(String.valueOf(childrenPicker.getValue()));
        textInfants.setText(String.valueOf(infantsPicker.getValue()));

    }

    public void setNumberPickersTest2(int adults, int children, int infants){

        setAdultsPicker(adults);
        setChildrenPicker(children);
        setInfantsPicker(infants);
    }

    public void setNumberPickersTest(final View root){

        roomsContainer.setVisibility(View.VISIBLE);
        roomsContainer.addView(root);
        globalSizeOfRooms = roomsContainer.getChildCount();

    }

    //Validate each numberPicker
    private void validateGuestQuantity(int idNumberPicker, int temporal){

        //Formula
        int newTotal = adultsPicker.getValue() + infantsPicker.getValue() +  childrenPicker.getValue();
        int delta = Constants.NUMBER_PICKER_SIZE - newTotal;
        Log.d("Sergio newTotal", String.valueOf(newTotal));
        Log.d("Sergio delta", String.valueOf(delta));
        Log.d("Sergio temporal", String.valueOf(temporal));

       /* if (delta == 0){
            infantsPicker.setMaxValue(infantsPicker.getValue()+delta);
            childrenPicker.setMaxValue(childrenPicker.getValue()+delta);
            adultsPicker.setMaxValue(adultsPicker.getValue()+delta);
        } else {*/
            infantsPicker.setMaxValue(infantsPicker.getValue() + delta);
            childrenPicker.setMaxValue(childrenPicker.getValue() + delta);
            adultsPicker.setMaxValue(adultsPicker.getValue() + delta);

            adultsPicker.setMinValue(1);
       // }

        /*//if (delta ) {
            switch (idNumberPicker) {
                case 1://Adults
                    infantsPicker.setMaxValue(infantsPicker.getValue()+delta);
                    childrenPicker.setMaxValue(childrenPicker.getValue()+delta);
                    //adultsPicker.setMaxValue(adultsPicker.getValue()+delta);
                    break;
                case 2://Children
                    //childrenPicker.setMaxValue(childrenPicker.getValue()+delta);
                    infantsPicker.setMaxValue(infantsPicker.getValue()+delta);
                    adultsPicker.setMaxValue(adultsPicker.getValue()+delta);
                    break;
                case 3://Infants
                    childrenPicker.setMaxValue(childrenPicker.getValue()+delta);
                    adultsPicker.setMaxValue(adultsPicker.getValue()+delta);
                    //infantsPicker.setMaxValue(infantsPicker.getValue()+delta);
                    break;
            }
        //}*/
    }

    public void setAdultsPicker(int adults){

        Log.d("adults setAdultsPicker",String.valueOf(adults));

            adultsPicker.setMinValue(1);
            adultsPicker.setMaxValue(Constants.NUMBER_PICKER_SIZE);
            adultsPicker.setWrapSelectorWheel(false);
            adultsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            adultsPicker.setValue(adults);
            adultsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                paramAdults = newVal;
                validateGuestQuantity(1, newVal);
            }
        });

        setDividerColor(adultsPicker);
    }

    public void setChildrenPicker(int children){

        childrenPicker.setMinValue(0);
        childrenPicker.setMaxValue(Constants.PICKER_SIZE_CHILDREN_AND_INFANTS);
        childrenPicker.setWrapSelectorWheel(false);
        childrenPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        childrenPicker.setValue(children);
        adultsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                paramChildren = newVal;
                validateGuestQuantity(2, newVal);
            }
        });
        setDividerColor(childrenPicker);
    }

    public void setInfantsPicker(int infants){

        infantsPicker.setMinValue(0);
        infantsPicker.setMaxValue(Constants.PICKER_SIZE_CHILDREN_AND_INFANTS);
        infantsPicker.setWrapSelectorWheel(false);
        infantsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        infantsPicker.setValue(infants);
        infantsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                paramInfants = i1;
                validateGuestQuantity(3, i1);
            }
        });
        setDividerColor(infantsPicker);
    }

    //Update values for dates after dialog date Calendar
    private void updateViews(Date dateBegin, Date dateEnd) {

        Log.d("dateBegin", String.valueOf(dateBegin));
        Log.d("dateEnd", String.valueOf(dateEnd));
        counterEvenDateSelected = 0;
        TextView destination = (TextView)findViewById(R.id.txtDestinationName);
        TextView destinationCode = (TextView)findViewById(R.id.txtDestinationCityAndCountry);

        paramDestinationName = destination.getText().toString();
        paramDestination = destinationCode.getText().toString();
        if (!paramDestination.equals("")) {
            paramDestination = paramDestination.substring(0, 3);
        }
        Log.d("Codigo IATA",paramDestination);
        paramCheckIn = Util.Bookings_Formatter_Hotels.format(dateBegin);
        paramCheckOut = Util.Bookings_Formatter_Hotels.format(dateEnd);
        Log.d("paramCheckIn",paramCheckIn);Log.d("paramCheckOut",paramCheckOut);

        TextView dateBegin2 = (TextView)findViewById(R.id.textDepartureDay);
        TextView yearBegin = (TextView)findViewById(R.id.textDepartureMonth);
        TextView dateEnd2 = (TextView) findViewById(R.id.textReturnDay);
        TextView yearEnd = (TextView) findViewById(R.id.textReturnMonth);

        dateBegin2.setText(Util.Day_Formatter.format(dateBegin).toUpperCase());
        yearBegin.setText(Util.M_Y_Formatter.format(dateBegin).toUpperCase());
        dateEnd2.setText(Util.Day_Formatter.format(dateEnd).toUpperCase());
        yearEnd.setText(Util.M_Y_Formatter.format(dateEnd).toUpperCase());
    }

    //Update and fill ListView with results for City or Airports
    @Subscribe
    public void onAsyncTaskResult(AsyncTaskMPosResultEvent event){

        progressBar.setVisibility(View.GONE);
        HashMap<String, String> data;

        if (event.getResult() != null) {
            data = event.getResult();
            airportData.clear();

            for (String name: data.keySet()){

                String key = name.toString();
                String value = data.get(name).toString();
                Log.d("Sergio Hotels", key + " " + value);
            }

            if (event.getApiName().equals(AirportCodes.APINAME)) {
                int msgCount = Integer.parseInt(data.get(AirportCodes.MSG_COUNT));
                Log.d("Sergio msgCount", String.valueOf(msgCount));
                if (msgCount > 0) {
                    for (int i = 0; i < msgCount; i++) {
                        airportData.add(new AirportData(data.get("IATA" + i),
                                data.get("IataCountry" + i),
                                data.get("Name" + i),
                                data.get("NameCity" + i),hotelsType));
                        Log.d("Sergio tal HOTEL",data.toString());
                    }

                }
                else {
                    listView.setEmptyView(findViewById(R.id.emptyElement));
                }
                adapter.notifyDataSetChanged();
            }

            adapter.notifyDataSetChanged();
        }
        setListViewHeightBasedOnChildren(listView);
    }

    //Clear the EditText for Search after (x) button is clicked
    public void onClearSearch(View view) {
        TextView search = (TextView) findViewById(R.id.et_search);
        search.setText("");
    }

    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public  void setListViewHeightBasedOnChildren(ListView listView) {

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < adapter.getCount(); i++) {
            view = adapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public void onalertDialogDepartureOrArriveNotSelected(){

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alert_dialog_city_not_selected_hotels);
        dialog.show();

        Button btnOk = (Button)dialog.findViewById(R.id.ButtonOkAlertDialogCityNotSelected);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}