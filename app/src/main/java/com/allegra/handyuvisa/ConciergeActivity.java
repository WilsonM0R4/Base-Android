package com.allegra.handyuvisa;

/**
 * Created by jsandoval on 3/06/16.
 */

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allegra.handyuvisa.async.AsyncRestHelper;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.async.AsyncTaskMPosResultEvent;
import com.allegra.handyuvisa.async.ConciergeCodes;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;

public class ConciergeActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    RelativeLayout customDestinationConcierge, customSearchConcierge;//Initially is gone, contains EditText and ListView between others
    LinearLayout  customTextAndButtonSearchConcierge, linLayGetUpAnimationFlights;//Initially is gone, is the custom view with text and image inside header
    ListView listView;
    ProgressBar progressBar;
    SearchAdapterAutoCompleteConcierge adapter;
    String TAG = "ConciergeActivity", labelCity = "", idCity;
    String urlConcierge = "http://actividades.allegra.travel/Actividad/json_destinos2/?query=";
    //"http://viator.vuelos.ninja/Actividad/json_destinos2?query=";
    ArrayList<Concierge> concierges;
    boolean alreadyEnterToThisMethod, isSearchActive = false;

    //*******INNER CLASSES********
    public class Concierge{

        private String id, label;

        public Concierge(String id, String label){
            this.id = id;
            this.label = label;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    //****** OVERRIDE METHODS ******
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_concierge,this);
        //System.gc();
        MyBus.getInstance().register(this);
        //airportData = new ArrayList<>();
        //getLocation();
    }

    @Override
    public void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }

    //If search is active => GetUpAnimation  else Lock onBackButton
    @Override
    public void onBackPressed() {
    Log.e("Sergio","Llega al back");
        /*if (isSearchActive) {
            onGetUpAnimationFlights();
        }*/
    }

    @Override
    public void initViews(View root) {

        customSearchConcierge = (RelativeLayout) root.findViewById(R.id.activity_search_concierge);
        customDestinationConcierge = (RelativeLayout) root.findViewById(R.id.custom_concierge_header);
        customTextAndButtonSearchConcierge = (LinearLayout) root.findViewById(R.id.custom_search_concierge);
        linLayGetUpAnimationFlights = (LinearLayout) root.findViewById(R.id.linLayGetUpAnimationFlights);
        //OnclickListener for hide activity_search custom view and Show Text and Button Search
        linLayGetUpAnimationFlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearchConciergeAndShowViews();
            }
        });

    }

    //****** PROPER METHODS ******

    public void onMenu(View view) {
        animate();
    }

    public void onClearSearch(View view) {
        TextView search = (TextView) findViewById(R.id.et_search);
        search.setText("");
    }

    void hideViewsAndShowSearchConcierge() {

        customTextAndButtonSearchConcierge.setVisibility(View.GONE);
        customSearchConcierge.setVisibility(View.VISIBLE);
        //Clear EditText
        EditText edtSearch = (EditText) findViewById(R.id.et_search);
        edtSearch.setText("");
        LinearLayout linLayGetUp = (LinearLayout) findViewById(R.id.linLayGetUpAnimationFlights);
        linLayGetUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGetUpAnimationConcierge();
                // isSearchActive = false;
            }
        });
    }

    void hideSearchConciergeAndShowViews() {

        customTextAndButtonSearchConcierge.setVisibility(View.VISIBLE);
        customSearchConcierge.setVisibility(View.GONE);
    }

    //Get up animation for Concierge components
    void onGetUpAnimationConcierge() {
        hideSearchConciergeAndShowViews();
        //if keyboard is open, close it.
        EditText edtSearch = (EditText) findViewById(R.id.et_search);
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

    }

    //This method performs autocomplete in ListView
    public void onConciergeLocation(View view) {

        hideViewsAndShowSearchConcierge();

        //Relate the listView from java to the one created in xml
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                //Update textViews inside Header
                ConciergeActivity.Concierge concierge =(Concierge)adapterView.getItemAtPosition(i);
                ImageView imgSelectDestination = (ImageView) findViewById(R.id.selec_destination_concierge);
                imgSelectDestination.setVisibility(View.GONE);
                TextView txtSElectDestination = (TextView)findViewById(R.id.txt_select_your_destination_concierge);
                txtSElectDestination.setVisibility(View.GONE);

                View linLayDestinationHotels = findViewById(R.id.ll_destination_concierge);
                linLayDestinationHotels.setVisibility(View.VISIBLE);
                TextView conciergeName = (TextView)findViewById(R.id.txtDestinationNameConcierge);
                TextView conciergeStateOrCountry = (TextView)findViewById(R.id.txtConciergeStateOrCountry);
                String labelConcierge = concierge.getLabel();
                //For send to SearchConciergeActivity:
                labelCity = labelConcierge;
                idCity = concierge.getId();
                String city = "";
                if (labelConcierge.contains("-")){ //Split city and Country or State
                    String[] parts = labelConcierge.split(" - ");
                    String country = parts[0]; // 004
                    city = parts[1]; // 034556
                    conciergeName.setText(city);
                    conciergeStateOrCountry.setText(country);
                }else {//Only city
                    conciergeName.setText(city);
                    conciergeStateOrCountry.setText("");
                }
                //conciergeName.setText(String.valueOf(concierge.getLabel()));
                //if keyboard is open, close it.
                EditText edtSearch = (EditText) findViewById(R.id.et_search);
                InputMethodManager imm = (InputMethodManager) getApplicationContext().
                        getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);

                hideSearchConciergeAndShowViews();
                return;
            }
        });

        concierges = new ArrayList<>();
        adapter = new SearchAdapterAutoCompleteConcierge(this, concierges);
        listView.setAdapter(adapter);
        progressBar = (ProgressBar) findViewById(R.id.pb_search);


        final EditText note = (EditText) findViewById(R.id.et_search);
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
                    InputMethodManager mgr = (InputMethodManager)
                            getSystemService(INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    performSearch(note.getText().toString());
                }
                return false; // pass on to other listeners.
            }
        });

        /*if (!alreadyEnterToThisMethod) MyBus.getInstance().register(this);
        alreadyEnterToThisMethod = true;*/

    }

    //Called when  EditText note's  addTextChangedListener and setOnEditorActionListener
    private void performSearch(String query) {
        Log.d(TAG, "performSearch");
        progressBar.setVisibility(View.VISIBLE);
        ConciergeCodes apiInfo;
        apiInfo = new ConciergeCodes(query, urlConcierge);
        AsyncRestHelper helper = new AsyncRestHelper(apiInfo);
        helper.execute();
    }

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskMPosResultEvent event) {

        if (progressBar!=null)progressBar.setVisibility(View.GONE);

        HashMap<String, String> data;

        if (event.getResult() != null) {
            data = event.getResult();

            for (String name : data.keySet()) {

                String key = name.toString();
                String value = data.get(name).toString();

                Log.d("Sergio este es", key + " " + value);
            }
            if (concierges != null) concierges.clear();
            if (event.getApiName().equals(ConciergeCodes.APINAME)) {
                int msgCount = Integer.parseInt(data.get(ConciergeCodes.MSG_COUNT));
                if (msgCount > 0) {
                    for (int i = 0; i < msgCount; i++) {
                        concierges.add(new Concierge(data.get("id" + i),
                                data.get("label" + i)));
                    }
                    adapter.notifyDataSetChanged();
                    listView.setSelection(0);
                } else {
                    adapter.notifyDataSetChanged();
                    listView.setEmptyView(findViewById(R.id.emptyElement));
                }
            }
        }
        if (listView != null) {
            setListViewHeightBasedOnChildren(listView);
        }

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

    //Perform call to ConciergeSearchActivity
    public void onSearchConcierge(View view) {
    //Get  destination initial values in order to search
        if (labelCity.equals("")) {
            //Toast.makeText(getApplicationContext(),"Pilas no puede ir vacio" ,Toast.LENGTH_LONG).show();
            onalertDialogDepartureOrArriveNotSelected();
        } else {
            Intent intent = new Intent(this, ConciergeSearchActivity.class);
            intent.putExtra("&labelCity", labelCity);
            intent.putExtra("&idCity", idCity);
            Log.d("labelCity", String.valueOf(labelCity));
            Log.d("idCity", String.valueOf(idCity));

            startActivity(intent);
        }
    }

    public void onalertDialogDepartureOrArriveNotSelected() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alert_dialog_city_concierge_not_selected);
        dialog.show();

        Button btnOk = (Button) dialog.findViewById(R.id.ButtonOkAlertDialogCityNotSelected);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

}
