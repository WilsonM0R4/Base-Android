package com.allegra.handyuvisa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sfarfan on 03/06/16.
 */
public class SearchAdapterAutoCompleteConcierge extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<ConciergeActivity.Concierge> results;
    private Context context;

    //Constructor
    public SearchAdapterAutoCompleteConcierge(Context context, ArrayList<ConciergeActivity.Concierge> results) {
        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.results = results;
        this.context = context;
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        //return null;
        // create a ViewHolder reference
        ViewHolder holder;
       // Log.e("SEarchAdapter", "Concierge getView");

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.search_list_item, null);
            holder.itemResult = (TextView) view.findViewById(R.id.list_item_text_view);
            //holder.itemAirportCode = (TextView)view.findViewById(R.id.textAirportCode);
            holder.itemCityAndCountry = (TextView) view.findViewById(R.id.textCityAndCountry);

            // the setTag is used to store the data within this view
            view.setTag(holder);

        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }

        //get the string item from the position "i" from array list to put it on the TextView
        ConciergeActivity.Concierge concierge = results.get(i);
        String labelConcierge = concierge.getLabel();
       // Log.e("SErfar", labelConcierge.toString());
        if (labelConcierge.contains("-")){ //Split city and Country or State
            String[] parts = labelConcierge.split(" - ");
            String country = parts[0]; // 004
            String city = parts[1]; // 034556
            holder.itemResult.setText(city);
            holder.itemCityAndCountry.setText(country);
        }else {//Only city
            holder.itemResult.setText(concierge.getLabel());
            holder.itemCityAndCountry.setText("");
        }

        return view;
    }


    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {
        protected TextView itemResult;
        protected TextView itemAirportCode;
        protected TextView itemCityAndCountry;
    }
}
