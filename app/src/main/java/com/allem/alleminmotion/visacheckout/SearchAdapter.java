package com.allem.alleminmotion.visacheckout;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lisachui on 12/1/15.
 */
public class SearchAdapter extends BaseAdapter {


    private LayoutInflater mLayoutInflater;
    private ArrayList<FlightsActivity.AirportData> results;
    private Context context;


    public SearchAdapter(Context context, ArrayList<FlightsActivity.AirportData> results) {
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
    public Object getItem(int i) {
        return results.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //Log.d(TAG, "Populating the list at position: " + position);
        // create a ViewHolder reference
        ViewHolder holder;

        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(R.layout.search_list_item, null);
            holder.itemResult = (TextView) view.findViewById(R.id.list_item_text_view);
            holder.itemAirportCode = (TextView)view.findViewById(R.id.textAirportCode);
            holder.itemCityAndCountry = (TextView) view.findViewById(R.id.textCityAndCountry);

            // the setTag is used to store the data within this view
            view.setTag(holder);

        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }

        //get the string item from the position "position" from array list to put it on the TextView
        FlightsActivity.AirportData airport = results.get(i);
        holder.itemResult.setText(airport.getName());
        holder.itemAirportCode.setText(airport.getCodeIATA());
        holder.itemCityAndCountry.setText(airport.getCity() + ", "+airport.getCountry());

        //this method must return the view corresponding to the data at the specified position.
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
