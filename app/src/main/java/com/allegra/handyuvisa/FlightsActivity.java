package com.allegra.handyuvisa;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.allegra.handyuvisa.utils.GPSTracker;
import com.allegra.handyuvisa.utils.NavigationCallback;
import com.allegra.handyuvisa.utils.Util;
import com.squareup.otto.Subscribe;
import com.squareup.timessquare.CalendarPickerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * This class have the logic from SearchActivity with "searchType" = "flights"
 */
public class FlightsActivity extends Fragment {

    //GLOBAL ATTRIBUTES

    private Button oneWay, round,economy,business,first, searchFlightsButton;
    private static final String TAG = "FlightsActivity";
    private int paramTrip = 0; // 0 - round trip; 1 - one way
    private int paramCabin = 0; // 0 - economy; 1 - business; 2 - first
    private int paramAdults = 1;
    private int paramChildren = 0;
    private int paramInfants = 0;
    private String paramDepartDate = "";
    private String paramArriveDate = "";
    private String paramDepartFrom = "";//IATA CODE DEPARTURE
    private String paramArriveAt = "";//IATA CODE ARRIVAL
    private String paramArriveAtName = "";//LARGE NAME ARRIVAL
    private String paramDepartFromName = "";//LARGE NAME DEPARTURE
    private NumberPicker adultsPicker, childrenPicker, infantsPicker;
    private static String url_flights = Constants.getAutocompleteFlightsUrl();
    private ListView listView;
    private SearchAdapter adapter;
    private ArrayList<AirportData> airportData;
    private ProgressBar progressBar;
    private int flightsType = 2;
    View customDateOFTrip, customPassengersCabinSearch, rllSearchAirport;
    RelativeLayout dateTripSelector, headerContainer;
    LinearLayout passengersSelector;
    boolean alreadyEnterToThisMethod, isSearchActive = false, isDeparture = false;
    TextView monthReturn, yearReturn;
    private static final int TEXT_RETURN_DATE_SIZE = 32;//TextSize in sp
    private static final int TEXT_ONE_WAY_SIZE = 22;//TextSize in sp
    Dialog dialog;
    String paramString = "";
    private LocationManager locationManager;
    private boolean isGPSEnabled, isNetworkEnabled, enterToGetLocation;
    private Location location;
    GPSTracker gp;
    Double latitude, longitude;
    String sd = "", seladdressstr = "";
    Boolean flagloca=false;
    String loctitle="";
    Boolean addAddress = true;
    TextView airportCode, arrivalAirporCode ,city, arrivalCity ;
    ImageView imageView, ivArrival, ivFlip;
    ImageButton menuButton;

    //INNER CLASSES

    public class AirportData {

        private String codeIATA;
        private String country;
        private String city;
        private String name;
        public int type = 0;

        public AirportData(String codeIATA, String name, String city, String country, int type) {
            this.codeIATA = codeIATA;
            this.name = name;
            this.type = type;
            this.city = city;
            this.country = country;
        }

        public String getCodeIATA() {
            return codeIATA;
        }

        public String getCountry() {
            return country;
        }

        public String getCity() {
            return city;
        }

        public String getName() {
            return name;
        }

        public String getListInformation() {
            String info = "";
            if (type == flightsType) {
                info = getName();
            }
            return info;
        }
    }

    //OVERRIDE METHODS

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setView(R.layout.fragment_flights, this);
        MyBus.getInstance().register(this);
        airportData = new ArrayList<>();
        ((MainActivity) getActivity()).statusBarVisibility(false);
        //getLocation();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_flights, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
    }

    @Override
    public void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }


    private void initViews(View root) {

        menuButton = (ImageButton) root.findViewById(R.id.menu_image);
        headerContainer = (RelativeLayout) root.findViewById(R.id.ll_header);

        //Set initial Date
        TextView dateBegin = (TextView) root.findViewById(R.id.textDepartureDay);
        TextView yearBegin = (TextView) root.findViewById(R.id.textDepartureMonth);
        TextView textViewYear = (TextView) root.findViewById(R.id.textDepartureYear);

        monthReturn = (TextView) root.findViewById(R.id.textReturnMonth);
        monthReturn.setText(R.string.txt_month_year_selector_flights);
        yearReturn = (TextView) root.findViewById(R.id.textReturnYear);
        yearReturn.setText("");
        //For get geo location and set texts:
        airportCode = (TextView) root.findViewById(R.id.tv_depart_airport);
        city = (TextView) root.findViewById(R.id.tv_city_departure);

        arrivalAirporCode = (TextView) root.findViewById(R.id.tv_arriving_airport);
        arrivalCity = (TextView) root.findViewById(R.id.tv_city_arrival);

        imageView = (ImageView) root.findViewById(R.id.iv_departure);
        ivFlip = (ImageView) root.findViewById(R.id.imageView33);
        ivArrival = (ImageView) root.findViewById(R.id.iv_arrival);

        dateTripSelector = (RelativeLayout) root.findViewById(R.id.panel_dates);
        passengersSelector = (LinearLayout) root.findViewById(R.id.linLayTextsPassengersPerCategory);

        searchFlightsButton = (Button)root.findViewById(R.id.btn_search);
        round = (Button)root.findViewById(R.id.btn_roundtrip);
        oneWay = (Button)root.findViewById(R.id.btn_oneway);
        economy = (Button) root.findViewById(R.id.btn_economy);
        business = (Button)root.findViewById(R.id.btn_business);
        first = (Button)root.findViewById(R.id.btn_first);
        searchFlightsButton.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));



        round.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_muli_light)));
        oneWay.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), getString(R.string.font_muli_light)));
        economy.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli_light)));
        business.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));
        first.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));

        Calendar now = Calendar.getInstance();
        Date today = now.getTime();
        dateBegin.setText(Util.Day_Formatter.format(today).toUpperCase());
        yearBegin.setText(Util.M_Formatter.format(today).toUpperCase());
        textViewYear.setText(Util.Y_Formatter.format(today).toUpperCase());

        //Is a solution for a bug when the method is called from xml onClick attribute
        //Button btnOneWay = (Button) root.findViewById(R.id.btn_oneway);

        setListeners();

        //updateTextsForAirportAndCity();
    }

    //If search is active => GetUpAnimation  else Lock onBackButton
    /*@Override
    public void onBackPressed() {

        if (isSearchActive) {
            onGetUpAnimationFlights();
        }
    }*/

    //****************PROPER METHODS*************


    private void setListeners(){

        new NavigationCallback() {
            @Override
            public void onForwardPressed(Fragment fragment) {

            }
        };

        headerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });

        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRoundTrip(getView());
            }
        });

        oneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onOneWay(getView());
            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).animate();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeparture = true;
                onDepartingLocation(getView());
            }
        });

        ivArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDeparture = false;
                onDepartingLocation(getView());
            }
        });

        ivFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFlip(getView());
            }
        });

        dateTripSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGetSchedule(getView());
            }
        });

        passengersSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlertDialogNumberPicker(getView());
            }
        });

        economy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCabinEconomy(getView());
            }
        });

        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCabinBusiness(getView());
            }
        });

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCabinFirst(getView());
            }
        });

        searchFlightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchFlights(getView());
            }
        });

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        //buildAlertMessageNoGps();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //Called when  EditText note's  addTextChangedListener and setOnEditorActionListener
    private void performSearch(String query) {
       // Log.d(TAG, "performSearch");
        //Log.d("Query code", query);
        progressBar.setVisibility(View.VISIBLE);
        AirportCodes apiInfo;
        apiInfo = new AirportCodes(query, url_flights);
        AsyncRestHelper helper = new AsyncRestHelper(apiInfo);
        helper.execute();
    }


    //Validate each numberPicker
    private void validateGuestQuantity(){

      /*  int adultTotal =  adultsPicker.getValue();
        Log.d("Sergio","adultTotal"+ String.valueOf(adultTotal));*/
        infantsPicker.setMaxValue(adultsPicker.getValue());
    }

    //NUMBER PICKERS FOR SELECT PASSENGERS

    public void setNumberPickers(int adults, int children, int infants) {

        setAdultsPicker(adults);
        setChildrenPicker(children);
        setInfantsPicker(infants);
    }

    public void setAdultsPicker(int adults) {

        adultsPicker.setMinValue(1);
        adultsPicker.setMaxValue(Constants.NUMBER_PICKER_SIZE);
        adultsPicker.setWrapSelectorWheel(false);
        adultsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        adultsPicker.setValue(adults);
        adultsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                paramAdults = newVal;
                validateGuestQuantity();

            }
        });

        setDividerColor(adultsPicker);

    }

    public void setChildrenPicker(int children) {

        childrenPicker.setMinValue(0);
        childrenPicker.setMaxValue(Constants.NUMBER_PICKER_SIZE);
        childrenPicker.setWrapSelectorWheel(false);
        childrenPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        childrenPicker.setValue(children);
        childrenPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                paramChildren = newVal;

            }
        });
        setDividerColor(childrenPicker);
    }

    public void setInfantsPicker(int infants) {

        infantsPicker.setMinValue(0);
        infantsPicker.setMaxValue(Constants.NUMBER_PICKER_SIZE);
        infantsPicker.setWrapSelectorWheel(false);
        infantsPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        infantsPicker.setValue(infants);
        infantsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                paramInfants = i1;
                validateGuestQuantity();
            }
        });
        setDividerColor(infantsPicker);
    }

    private void setDividerColor(NumberPicker picker) {

        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.PersonSelectorItemDescriptionNumber));
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
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
    public void onGetSchedule(View view) {

        dialog = new Dialog(getActivity(), android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_date_picker_custom);

        final TextView dateBegin = (TextView) dialog.findViewById(R.id.textDepartureDayWithoutOnClick);
        final TextView yearBegin = (TextView) dialog.findViewById(R.id.textDepartureMonthWithoutOnClick);
        final TextView dateEnd = (TextView) dialog.findViewById(R.id.textReturnDayWithoutOnClick);
        final TextView yearEnd = (TextView) dialog.findViewById(R.id.textReturnMonthWithoutOnClick);
        final TextView yearReturnDate = (TextView) dialog.findViewById(R.id.textReturnYearWithoutOnClick);

        Calendar now = Calendar.getInstance();
        Date today = now.getTime();

        dateBegin.setText(Util.Day_Formatter.format(today).toUpperCase());
        yearBegin.setText(Util.M_Y_Formatter.format(today).toUpperCase());

        now.add(Calendar.YEAR, 1);
        final CalendarPickerView calendar = (CalendarPickerView) dialog.findViewById(R.id.cv_duration);

        calendar.init(today, now.getTime())
                .withSelectedDate(today)
                .inMode(CalendarPickerView.SelectionMode.RANGE);

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                List<Date> dates = calendar.getSelectedDates();
                int size = dates.size();
               // Log.d("DATE RECEIVED", "");
                if (size > 1) {//Round Trip
                    dateBegin.setText(Util.Day_Formatter.format(dates.get(0)).toUpperCase());
                    yearBegin.setText(Util.M_Y_Formatter.format(dates.get(0)).toUpperCase());
                    dateEnd.setText(Util.Day_Formatter.format(dates.get(size - 1)).toUpperCase());
                    dateEnd.setTextColor(getResources().getColor(R.color.DoubleSelectorButtonSelected_backgroung));
                    dateEnd.setTextSize(TEXT_RETURN_DATE_SIZE);
                    yearEnd.setText(Util.M_Y_Formatter.format(dates.get(size - 1)).toUpperCase());
                    yearReturnDate.setText("");
                } else if (size == 1) {//One way
                    dateBegin.setText(Util.Day_Formatter.format(dates.get(0)).toUpperCase());
                    yearBegin.setText(Util.M_Y_Formatter.format(dates.get(0)).toUpperCase());
                    dateEnd.setText(R.string.one_way_type);
                    dateEnd.setTextColor(getResources().getColor(R.color.GRAY_COLOR));
                    dateEnd.setTextSize(TEXT_ONE_WAY_SIZE);
                    yearEnd.setText("");
                    yearReturnDate.setText("");
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
                List<Date> dates = calendar.getSelectedDates();
                int size = dates.size();
                if (dates.size() > 1) {
                    onRoundTripImplicit();
                    FlightsActivity.this.updateViews(dates.get(0), dates.get(size - 1));
                }
                if (size == 1) {
                    FlightsActivity.this.updateViews(dates.get(0), null);
                    onOneWayImplicit();
                }
                dialog.dismiss();
            }
        });

        ImageButton cancel = (ImageButton) dialog.findViewById(R.id.btn_cancel_date_picker);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        dialog.show();

    }

    public void onOneWayImplicit() {

        paramTrip = 1;
        setButtonAttributes(round, R.drawable.round_corner_transparent_for_type_of_trip, R.color.white);
        setButtonAttributes(oneWay, R.drawable.round_corner_magenta, R.color.white);
    }

    public void onRoundTripImplicit() {
        paramTrip = 0;
        setButtonAttributes(round, R.drawable.round_corner_magenta, R.color.white);
        setButtonAttributes(oneWay, R.drawable.round_corner_transparent_for_type_of_trip, R.color.white);
    }

    public void onMenu(View view) {
        ((MainActivity) getActivity()).animate();
    }

    public void hideViewsAndShowSearchFlight() {

        customDateOFTrip.setVisibility(View.GONE);
        customPassengersCabinSearch.setVisibility(View.GONE);
        rllSearchAirport.setVisibility(View.VISIBLE);
        isSearchActive = true;

        //Clear EditText
        EditText edtSearch = (EditText) getView().findViewById(R.id.et_search);
        edtSearch.setText("");
        LinearLayout linLayGetUp = (LinearLayout) getView().findViewById(R.id.linLayGetUpAnimationFlights);
        linLayGetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetUpAnimationFlights();
                isSearchActive = false;
            }
        });
    }

    public void hideSearchFlightAndShowViews() {
        customDateOFTrip.setVisibility(View.VISIBLE);
        customPassengersCabinSearch.setVisibility(View.VISIBLE);
        rllSearchAirport.setVisibility(View.GONE);
    }

    /**
     * This method performs autocomplete in ListView
     *
     * @param view
     */
    public void onDepartingLocation(View view) {

        customDateOFTrip = view.findViewById(R.id.custom_date_of_trip);
        customPassengersCabinSearch = view.findViewById(R.id.custom_passengers_cabin_search);
        rllSearchAirport = view.findViewById(R.id.search_airport);
        hideViewsAndShowSearchFlight();

        /*boolean departure = false;
        //Validation
        if (view.getId() == R.id.iv_departure || view.getId() == R.id.tv_depart_airport) {
            departure = true;
        }*/
        //final boolean isDeparture = departure;

        //relate the listView from java to the one created in xml
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AirportData data = airportData.get(position);
                Intent bundle = new Intent();
                bundle.putExtra("iata", data.getCodeIATA());
                bundle.putExtra("name", data.getName());
                bundle.putExtra("city", data.getListInformation());

              //  Log.d(TAG, "Update airport code in view");
                //TextView airport;
                //TextView city;
                //ImageView imageView;

                if (isDeparture) {
                    //imageView = (ImageView)view.findViewById(R.id.iv_departure);
                    imageView.setVisibility(View.GONE);
                    //airportCode = (TextView) view.findViewById(R.id.tv_depart_airport);
                    //city = (TextView) view.findViewById(R.id.tv_city_departure);
                    paramDepartFromName = data.getCity();
                    paramDepartFrom = data.getCodeIATA();
                    airportCode.setVisibility(View.VISIBLE);
                    airportCode.setText(paramDepartFrom);
                    city.setText(paramDepartFromName);

                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    p.addRule(RelativeLayout.BELOW, R.id.tv_depart_airport);
                    city.setLayoutParams(p);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) city.getLayoutParams();
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    city.setLayoutParams(lp);

                } else {//Arrival
                    //imageView = (ImageView) view.findViewById(R.id.iv_arrival);
                    //airportCode = (TextView) view.findViewById(R.id.tv_arriving_airport);
                    //city = (TextView) view.findViewById(R.id.tv_city_arrival);
                    ivArrival.setVisibility(View.GONE);
                    paramArriveAt = data.getCodeIATA();
                    paramArriveAtName = data.getCity();
                    String cityName = paramArriveAtName;
                    arrivalAirporCode.setVisibility(View.VISIBLE);
                    arrivalAirporCode.setText(paramArriveAt);
                    arrivalCity.setText(cityName);
                    RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    p.addRule(RelativeLayout.BELOW, R.id.tv_arriving_airport);
                    arrivalCity.setLayoutParams(p);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) arrivalCity.getLayoutParams();
                    lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    arrivalCity.setLayoutParams(lp);
                }

                //if keyboard is open, close it.
                /*EditText edtSearch = (EditText) view.findViewById(R.id.et_search);
                InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext()
                        .getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);*/

                hideSearchFlightAndShowViews();
                return;
            }
        });

        //show the ListView on the screen
        // The adapter is responsible for maintaining the data backing this nameList and for producing
        // a view to represent an item in that data set.

        adapter = new SearchAdapter(getActivity(), airportData);
        listView.setAdapter(adapter);
        progressBar = (ProgressBar) view.findViewById(R.id.pb_search);

        final EditText note = (EditText) view.findViewById(R.id.et_search);
        note.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 2) {
                    performSearch(s.toString());
                }
            }
        });
        note.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager mgr = (InputMethodManager) getActivity().
                            getSystemService(MainActivity.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    performSearch(note.getText().toString());
                }
                return false; // pass on to other listeners.
            }
        });

      /*  if (!alreadyEnterToThisMethod) MyBus.getInstance().register(this);
        alreadyEnterToThisMethod = true;*/

    }

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskMPosResultEvent event) {

        if (progressBar != null){
            progressBar.setVisibility(View.INVISIBLE);
        }
        HashMap<String, String> data;

        if (event.getResult() != null) {
            data = event.getResult();
            airportData.clear();
            if (event.getApiName().equals(AirportCodes.APINAME)) {
                int msgCount = Integer.parseInt(data.get(AirportCodes.MSG_COUNT));
               // Log.d("Cuenta",String.valueOf(msgCount));
                if (msgCount > 0) {

                    for (int i = 0; i < msgCount; i++) {
                        airportData.add(new AirportData(data.get("id" + i),
                                data.get("aeropuerto" + i), data.get("ciudad" + i), data.get("pais" + i), flightsType));
                            }
                    if (adapter != null && listView != null) {
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0); }
                        }
                    if (adapter != null && listView != null) {
                        adapter.notifyDataSetChanged();
                        listView.setEmptyView(getView().findViewById(R.id.emptyElement));
                    }
                }
            }
        //}
        if (listView != null) {//!enterToGetLocation &&
            setListViewHeightBasedOnChildren(listView);
        }
    }

    private void setButtonAttributes(Button button, int drawableId, int textColor) {
        Drawable background;
        int color = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            background = getResources().getDrawable(drawableId, null);
            color = getResources().getColor(textColor, null);
        } else {
            background = getResources().getDrawable(drawableId);
            color = getResources().getColor(textColor);
        }

        button.setBackground(background);
        button.setTextColor(color);
    }

    //Update textViews after check button is pressed in Calendar dialog.

    private void updateViews(Date dateBegin, Date dateEnd) {

        //Log.d("dateBegin", String.valueOf(dateBegin));
        //Log.d("dateEnd", String.valueOf(dateEnd));
        paramDepartDate = Util.Bookings_Formatter.format(dateBegin);
        //SET TEXTS FOR DEPARTURE DATE
        TextView textViewDepartDay, textViewDepartMonth, textViewDepartYear;

        textViewDepartDay = (TextView) getView().findViewById(R.id.textDepartureDay);
        textViewDepartMonth = (TextView) getView().findViewById(R.id.textDepartureMonth);
        textViewDepartYear = (TextView) getView().findViewById(R.id.textDepartureYear);

        textViewDepartDay.setText(Util.Day_Formatter.format(dateBegin).toUpperCase());
        textViewDepartMonth.setText(Util.M_Formatter.format(dateBegin).toUpperCase());
        textViewDepartYear.setText(Util.Y_Formatter.format(dateBegin).toUpperCase());
        //SET TEXTS FOR RETURN DATE
        TextView textViewReturnDay, textViewReturnMonth, textViewReturnYear;

        textViewReturnDay = (TextView) getView().findViewById(R.id.textReturnDay);
        textViewReturnMonth = (TextView) getView().findViewById(R.id.textReturnMonth);
        textViewReturnYear = (TextView) getView().findViewById(R.id.textReturnYear);

        if (dateEnd != null) {//Round Trip
            paramArriveDate = Util.Bookings_Formatter.format(dateEnd);

            textViewReturnDay.setTextColor(getResources().getColor(R.color.CalendarSelectorRange_backgroung));
            textViewReturnDay.setTextSize(TEXT_RETURN_DATE_SIZE);
            textViewReturnDay.setText(Util.Day_Formatter.format(dateEnd).toUpperCase());
            textViewReturnMonth.setText(Util.M_Formatter.format(dateEnd).toUpperCase());
            textViewReturnYear.setText(Util.Y_Formatter.format(dateEnd).toUpperCase());
            yearReturn.setVisibility(View.VISIBLE);

            //Margin Right for LinearLayout
            LinearLayout llyReturnDate = (LinearLayout) getView().findViewById(R.id.llyReturnDate);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llyReturnDate.getLayoutParams();
            params.rightMargin = (int) (16f * this.getResources().getDisplayMetrics().density);
            llyReturnDate.setLayoutParams(params);
            //Margin Top for TextView
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 0, 0, 0);
            textViewReturnDay.setLayoutParams(params2);

        } else { // One Way
            paramArriveDate = "";

            textViewReturnDay.setText(R.string.one_way_type);
            textViewReturnDay.setTextColor(getResources().getColor(R.color.gray));
            textViewReturnDay.setTypeface(null, Typeface.NORMAL);
            textViewReturnDay.setTextSize(TEXT_ONE_WAY_SIZE);
            monthReturn.setText(paramArriveDate);

            //Margin Top for TextView
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 10, 0, 0);
            textViewReturnDay.setLayoutParams(params2);
            yearReturn.setVisibility(View.GONE);
        }
    }

    /**
     * This method calls FlightsSearchActivity in order to connect with Flights API.
     */
    public void onSearchFlights(View view) {

        //VALIDATIONS
        if (paramDepartFrom.equals("") || paramArriveAt.equals("")) { //   || paramDepartFromName.equals("") || paramArriveAtName.equals("")
            onalertDialogDepartureOrArriveNotSelected();
        } else {
            if (paramTrip == 0 && paramArriveDate.equals("")) {
                onalertDialogDateNotSelected();

            } else {

                //Intent intent = new Intent(getActivity(), .class);
                Bundle extraData = new Bundle();
                extraData.putInt("trip", paramTrip);
                extraData.putInt("cabin", paramCabin);
                extraData.putInt("adult", paramAdults);
                extraData.putInt("children", paramChildren);
                extraData.putInt("infant", paramInfants);
                if (paramDepartDate.equals("")){
                    //Log.d("Sergio","Entra al vacio");
                    Calendar now = Calendar.getInstance();
                    Date today = now.getTime();
                    paramDepartDate = Util.Bookings_Formatter.format(today);
                    //Log.d("Sergio", String.valueOf(paramDepartDate));
                }
                extraData.putString("departOn", paramDepartDate);
                extraData.putString("arriveOn", paramArriveDate);
                extraData.putString("departFrom", paramDepartFrom);
                extraData.putString("arriveAt", paramArriveAt);
                extraData.putString("arriveAtName", paramArriveAtName);
                extraData.putString("departFromName", paramDepartFromName);

                /*Log.d("trip", String.valueOf(paramTrip));
                Log.d("cabin", String.valueOf(paramCabin));
                Log.d("adult", String.valueOf(paramAdults));
                Log.d("children", String.valueOf(paramChildren));
                Log.d("infant", String.valueOf(paramInfants));
                Log.d("departOn", String.valueOf(paramDepartDate));
                Log.d("arriveOn", String.valueOf(paramArriveDate));
                Log.d("departFrom", String.valueOf(paramDepartFrom));
                Log.d("arriveAt", String.valueOf(paramArriveAt));
                Log.d("arriveAtName", String.valueOf(paramArriveAtName));
                Log.d("departFromName", String.valueOf(paramDepartFromName));*/

                //startActivity(intent);

                extraData.putString(WebFragment.STARTER_VIEW, this.getClass().getName());
                extraData.putString(WebFragment.WEB_TITLE, getString(R.string.title_flight_results));
                extraData.putString(WebFragment.LOADING_URL, Constants.getSearchFlightsUrl());
                extraData.putBoolean(WebFragment.CAN_RETURN, true);

                WebFragment fragmentSearchFlights = new WebFragment();
                fragmentSearchFlights.setArguments(extraData);
                ((MainActivity) getActivity()).replaceLayout(fragmentSearchFlights, false);
            }
        }
    }

    public void onCabinEconomy(View view) {

        economy.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli_light)));
        business.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));
        first.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));

        paramCabin = 0;
        setButtonAttributes(economy, R.drawable.round_corner_white_border_magenta, R.color.ThirdSelectorButtonTitle_text);
        setButtonAttributes(business, R.drawable.round_corner_transparent_fix, R.color.ThirdSelectorButtonNormal_text);
        setButtonAttributes(first, R.drawable.round_corner_transparent_fix, R.color.ThirdSelectorButtonNormal_text);
    }

    public void onCabinBusiness(View view) {

        economy.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));
        business.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli_light)));
        first.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));

        paramCabin = 1;
        setButtonAttributes(economy, R.drawable.round_corner_transparent_fix, R.color.ThirdSelectorButtonNormal_text);
        setButtonAttributes(business, R.drawable.round_corner_white_border_magenta, R.color.ThirdSelectorButtonTitle_text);
        setButtonAttributes(first, R.drawable.round_corner_transparent_fix, R.color.ThirdSelectorButtonNormal_text);
    }

    public void onCabinFirst(View view) {

        economy.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));
        business.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli)));
        first.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),getString(R.string.font_muli_light)));

        paramCabin = 2;
        setButtonAttributes(economy, R.drawable.round_corner_transparent_fix, R.color.ThirdSelectorButtonNormal_text);
        setButtonAttributes(business, R.drawable.round_corner_transparent_fix, R.color.ThirdSelectorButtonNormal_text);
        setButtonAttributes(first, R.drawable.round_corner_white_border_magenta, R.color.ThirdSelectorButtonTitle_text);
    }

    public void onRoundTrip(View view) {

        round = (Button) view.findViewById(R.id.btn_roundtrip);
        oneWay = (Button) view.findViewById(R.id.btn_oneway);

        paramTrip = 0;

        setButtonAttributes(round, R.drawable.round_corner_magenta, R.color.DoubleSelectorButtonSelected_text);
        setButtonAttributes(oneWay, R.drawable.round_corner_transparent_for_type_of_trip_fix, R.color.DoubleSelectorButtonNormal_text);
        //Change Arrive Date
        TextView textReturnDay = (TextView) view.findViewById(R.id.textReturnDay);
        textReturnDay.setText(R.string.txt_select_date);
        textReturnDay.setTextSize(22);

        monthReturn.setText(R.string.txt_month_year_selector_flights);
        monthReturn.setTextColor(getResources().getColor(R.color.dark_gray));
        yearReturn.setVisibility(View.GONE);

        //Margin Right for LinearLayout
        LinearLayout llyReturnDate = (LinearLayout) view.findViewById(R.id.llyReturnDate);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llyReturnDate.getLayoutParams();
        params.rightMargin = (int) (16f * this.getResources().getDisplayMetrics().density);
        llyReturnDate.setLayoutParams(params);

    }

    public void onOneWay(View view) {
        //Button round = (Button) view.findViewById(R.id.btn_roundtrip);
        //Button oneWay = (Button) view.findViewById(R.id.btn_oneway);

        paramTrip = 1;
        setButtonAttributes(round, R.drawable.round_corner_transparent_for_type_of_trip_fix, R.color.DoubleSelectorButtonNormal_text);
        setButtonAttributes(oneWay, R.drawable.round_corner_magenta, R.color.DoubleSelectorButtonSelected_text);

        //Change Arrive Date
        TextView textView;
        paramArriveDate = "";
        textView = (TextView) view.findViewById(R.id.textReturnDay);
        textView.setText(R.string.one_way_type);
        textView.setTextColor(getResources().getColor(R.color.gray));
        textView.setTextSize(TEXT_ONE_WAY_SIZE);
        //Margin Top for TextView
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params2.setMargins(0, 16, 0, 0);
        textView.setLayoutParams(params2);

       // Log.d("Entra a ", " onOneWay");
        monthReturn.setText("");
        yearReturn.setVisibility(View.GONE);
    }

    public void onFlip(View view) {

        TextView fromAirport = (TextView) view.findViewById(R.id.tv_depart_airport);
        TextView toAirport = (TextView) view.findViewById(R.id.tv_arriving_airport);
        TextView fromCity = (TextView) view.findViewById(R.id.tv_city_departure);
        TextView toCity = (TextView) view.findViewById(R.id.tv_city_arrival);

        String cityFrom = fromCity.getText().toString();
        String cityTo = toCity.getText().toString();

        if (!cityFrom.equals("FROM") && !cityFrom.equals("SALIDA") && !cityTo.equals("TO") && !cityTo.equals("LLEGADA")) {

            String swap = fromAirport.getText().toString();
            fromAirport.setText(toAirport.getText());
            toAirport.setText(swap);

            swap = fromCity.getText().toString();
            fromCity.setText(toCity.getText());
            toCity.setText(swap);

            swap = paramDepartFrom;
            paramDepartFrom = paramArriveAt;
            paramArriveAt = swap;
        }
    }

    public void onClearSearch(View view) {
        TextView search = (TextView) view.findViewById(R.id.et_search);
        search.setText("");
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {

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

    public void onalertDialogDateNotSelected() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_alert_dialog_date_not_selected);
        dialog.show();

        Button btnOk = (Button) dialog.findViewById(R.id.ButtonOkAlertDialogDateNotSelected);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void onalertDialogDepartureOrArriveNotSelected() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_alert_dialog_city_not_selected);
        dialog.show();

        Button btnOk = (Button) dialog.findViewById(R.id.ButtonOkAlertDialogCityNotSelected);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void onAlertDialogNumberPicker(View view) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_number_picker_dialog);
        dialog.show();
        adultsPicker = (NumberPicker) dialog.findViewById(R.id.adultsNumberPicker);
        childrenPicker = (NumberPicker) dialog.findViewById(R.id.childrenNumberPicker);
        infantsPicker = (NumberPicker) dialog.findViewById(R.id.infantsNumberPicker);
        TextView textAdults = (TextView) view.findViewById(R.id.adultsTextView);
        TextView textChildren = (TextView) view.findViewById(R.id.childrenTextView);
        TextView textInfants = (TextView) view.findViewById(R.id.infantsTextView);

        paramString = String.valueOf(textAdults.getText());
        int adults = Integer.parseInt(paramString);
        //Log.d("adults", paramString);

        paramString = String.valueOf(textChildren.getText());
        int children = Integer.parseInt(paramString);

        paramString = String.valueOf(textInfants.getText());
        int infants = Integer.parseInt(paramString);

        setNumberPickers(adults, children, infants);
        //TextViews (Cancel and Ok)
        TextView textCancel = (TextView) dialog.findViewById(R.id.textCancelDialog);
        textCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        TextView textOk = (TextView) dialog.findViewById(R.id.textOkDialog);
        textOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                updateTextsOfPassengers();
            }
        });

    }

    //Update TextViews with new results
    public void updateTextsOfPassengers() {

        TextView textAdults = (TextView) getView().findViewById(R.id.adultsTextView);
        TextView textChildren = (TextView) getView().findViewById(R.id.childrenTextView);
        TextView textInfants = (TextView) getView().findViewById(R.id.infantsTextView);
        textAdults.setText(String.valueOf(adultsPicker.getValue()));
        textChildren.setText(String.valueOf(childrenPicker.getValue()));
        textInfants.setText(String.valueOf(infantsPicker.getValue()));

    }

    //Get up animation for flights components
    public void onGetUpAnimationFlights() {
        hideSearchFlightAndShowViews();
        //if keyboard is open, close it.
        EditText edtSearch = (EditText) getView().findViewById(R.id.et_search);
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(MainActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

    }

}
