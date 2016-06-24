package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.allegra.handysdk.bean.BeanConstants;
import com.allegra.handysdk.bean.SubService;
import com.allegra.handysdk.responsebean.AdapterRefreshInterface;
import com.allegra.handysdk.responsebean.CategoryCallInterface;
import com.allem.alleminmotion.visacheckout.utils.MyTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by jsandoval on 20/06/16.
 */
public class HomeFragment extends Fragment implements View.OnClickListener , AdapterRefreshInterface {

    static View parentView;
    Context context;
    GridView gridview;
    GridAdapter adapter;
    public static ArrayList<SubService> mainarray=new ArrayList<>();
    static int height,h;
    DisplayMetrics display;
    static MyTextView tvservicecount;
    static String servicename="";
    static ArrayList<String> Childservicelist=new ArrayList<>();
    static int servicecount=0,service_pos=0;
    static ArrayList<String> Mainserviceflaglist=new ArrayList<>();

    //*************  OVERRIDE METHODS  *************
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView=inflater.inflate(R.layout.homefra_activity,container,false);
        init();
        return  parentView;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        context=getActivity();
    }

    @Override
    public void Refreshadapter(Activity activity, int position, String servicename) {
        mainarray = new ArrayList<>();
        mainarray=(BeanConstants.CategoryData.get(position).getSubServices());
        adapter.notifyDataSetChanged();

    }

    //*************  PROPER METHODS  *************
    private void init() {
        mainarray=(BeanConstants.CategoryData.get(service_pos).getSubServices());
        tvservicecount=(MyTextView)parentView.findViewById(R.id.tv_service_homefra);
        tvservicecount.setOnClickListener(this);
        display =getActivity().getResources().getDisplayMetrics();
        height = display.heightPixels;
        h = (height * 13) / 100;
        gridview=(GridView)parentView.findViewById(R.id.gridview);
        adapter=new GridAdapter(getActivity());
        gridview.setAdapter(adapter);
    }

    //*************  INNER CLASSES  *************
    public static class GridAdapter extends BaseAdapter {
        Context context;
        GridAdapter(Activity context){
            this.context=context;
        }

        @Override
        public int getCount() {
            return mainarray.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater minflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = minflater.inflate(R.layout.homeadapter, null);

            final ImageView iv_homefr=(ImageView)convertView.findViewById(R.id.iv_homefr);
            final MyTextView tv_homefr=(MyTextView)convertView.findViewById(R.id.tv_homefr);
            Picasso.with(context).load(BeanConstants.customer_image_subservice+mainarray.get(position).getSub_Service_Image()+ "&h="+h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(iv_homefr);
            tv_homefr.setText(mainarray.get(position).getSub_Service_Name());
            if (servicename.equalsIgnoreCase(mainarray.get(position).getMain_cat_name())) {
                if (mainarray.get(position).getSub_cat_flag().equalsIgnoreCase("false") ) {
                    Picasso.with(context).load(BeanConstants.customer_image_subservice + mainarray.get(position).getSub_Service_Image() + "&h="+h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(iv_homefr);
                }else{
                    if (!Childservicelist.contains(mainarray.get(position).getSub_Service_ID())){
                        Childservicelist.add(mainarray.get(position).getSub_Service_ID());
                        servicecount++;
                        CategoryCallInterface intface =(CategoryCallInterface)context;
                        intface.setdata(servicecount + " Service(s) selected",Childservicelist);
                    }
                    iv_homefr.setImageResource(R.drawable.icon_selected);

                }
            }else{
                CategoryCallInterface intface =(CategoryCallInterface)context;
                intface.setdata(servicecount + " Service(s) selected",Childservicelist);
                Childservicelist.clear();
                servicecount=0;

                Picasso.with(context).load(BeanConstants.customer_image_subservice + mainarray.get(position).getSub_Service_Image() + "&h="+h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(iv_homefr);
                mainarray.get(position).setSub_cat_flag("false");
                CategoryCallInterface intface1 =(CategoryCallInterface)context;
                intface1.setdata(servicecount + " Service(s) selected",Childservicelist);
            }


            convertView.setId(position);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Mainserviceflaglist = new ArrayList<String>();
                    if (mainarray.get(v.getId()).getSub_cat_flag().equalsIgnoreCase("false")) {
                        mainarray.get(v.getId()).setSub_cat_flag("true");
                        iv_homefr.setImageResource(R.drawable.icon_selected);
                        Mainserviceflaglist.add(mainarray.get(v.getId()).getMain_cat_name());
                        servicename = mainarray.get(v.getId()).getMain_cat_name();
                        Childservicelist.add(mainarray.get(v.getId()).getSub_Service_ID());
                        servicecount++;
                    } else {
                        servicecount--;
                        Childservicelist.remove(mainarray.get(v.getId()).getSub_Service_ID());
                        tvservicecount.setText(servicecount + " Service(s) selected");
                        mainarray.get(v.getId()).setSub_cat_flag("false");
                        Picasso.with(context).load(BeanConstants.customer_image_subservice + mainarray.get(v.getId()).getSub_Service_Image() + "&h=" + h).error(R.drawable.plasholder_75).placeholder(R.drawable.plasholder_75).into(iv_homefr);
                    }
                    CategoryCallInterface intface1 =(CategoryCallInterface)context;
                    intface1.setdata(servicecount + " Service(s) selected",Childservicelist);

                }
            });

            return convertView;
        }
    }
}