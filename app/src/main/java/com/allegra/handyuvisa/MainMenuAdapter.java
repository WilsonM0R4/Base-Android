package com.allegra.handyuvisa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by lisachui on 12/1/15.
 */
public class MainMenuAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private BackFragment.MenuActivity[] mActivities;

    public MainMenuAdapter(Context context, BackFragment.MenuActivity[] activities) {
        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mActivities = activities;
    }

    @Override
    public int getCount() {
        return mActivities.length;
    }

    @Override
    public Object getItem(int i) {
        return mActivities[i];
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

            view = mLayoutInflater.inflate(R.layout.list_main_menu, null);
            holder.itemName = (TextView) view.findViewById(R.id.menu_title);
            holder.icon = (ImageView) view.findViewById(R.id.menu_icon);

            // the setTag is used to store the data within this view
            view.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)view.getTag();
        }
        //get the string item from the position "position" from array list to put it on the TextView
        BackFragment.MenuActivity activity = mActivities[i];
        holder.itemName.setText(activity.toString());
        holder.icon.setImageResource(activity.getIconResource());

        //this method must return the view corresponding to the data at the specified position.
        return view;
    }

    /**
     * Static class used to avoid the calling of "findViewById" every time the getView() method is called,
     * because this can impact to your application performance when your list is too big. The class is static so it
     * cache all the things inside once it's created.
     */
    private static class ViewHolder {
        protected TextView itemName;
        protected ImageView icon;

    }

}
