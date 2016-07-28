package com.allegra.handyuvisa;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by jsandoval on 27/05/16.
 */
public class Mcardprivilege extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {

    public ExpandableListView expandableListViewPrivilegeDrHelp;
    public ExpandableListView expandableListViewAditional;
    public ExpandableListView expandableListViewCotidiana;
    public ExpandableListView expandableListViewFinancial;

    public boolean flag = true;

    public ViewGroup cotidiana_layout;
    public ViewGroup aditional_separator_layout;
    public ViewGroup cotidiana_list_layout;
    public ViewGroup financiera_title;
    public ViewGroup financiera_separator;
    public ViewGroup financial_layout;
    public ViewGroup benefits_description;
    public ViewGroup benefits_list;
    public ViewGroup calltobuylayout;


    private TextView callus;
    private Button calltobuy;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setView(R.layout.fragment_mcard_privilege, this);


    }

    @Override
    public void onDestroy() {
        //MyBus.getInstance().unregister(this);
        super.onDestroy();
    }


    public void initViews(final View root) {

        expandableListViewPrivilegeDrHelp = (ExpandableListView) root.findViewById(R.id.exp_listview);
        expandableListViewAditional = (ExpandableListView) root.findViewById(R.id.exp_listview_aditional);
        expandableListViewCotidiana = (ExpandableListView) root.findViewById(R.id.exp_listview_cotidiana);
        expandableListViewFinancial = (ExpandableListView) root.findViewById(R.id.exp_listview_financial);
        callus = (TextView) root.findViewById(R.id.callUsTxt);
        calltobuy = (Button) root.findViewById(R.id.calltobuy);
        call();


        //Titles Dr Help
        List<String> headings = new ArrayList<String>();
        List<String> L1 = new ArrayList<String>();
        List<String> L2 = new ArrayList<String>();
        List<String> L3 = new ArrayList<String>();
        List<String> L4 = new ArrayList<String>();
        List<String> L5 = new ArrayList<String>();
        List<String> L6 = new ArrayList<String>();
        List<String> L7 = new ArrayList<String>();
        List<String> L8 = new ArrayList<String>();
        List<String> L9 = new ArrayList<String>();
        List<String> L10 = new ArrayList<String>();
        List<String> L11 = new ArrayList<String>();

        //Titles Aditional Information
        List<String> headings_aditional = new ArrayList<String>();
        List<String> L1A = new ArrayList<String>();
        List<String> L2A = new ArrayList<String>();
        List<String> L3A = new ArrayList<String>();
        List<String> L4A = new ArrayList<String>();
        List<String> L5A = new ArrayList<String>();

        //Tittles Daily Protection
        List<String> headings_cotidiana = new ArrayList<String>();
        List<String> L1C = new ArrayList<String>();
        List<String> L2C = new ArrayList<String>();
        List<String> L3C = new ArrayList<String>();
        List<String> L4C = new ArrayList<String>();
        List<String> L5C = new ArrayList<String>();
        List<String> L6C = new ArrayList<String>();

        //Tittles Financial Protection
        List<String> headings_financial = new ArrayList<String>();
        List<String> L1F = new ArrayList<String>();
        List<String> L2F = new ArrayList<String>();
        List<String> L3F = new ArrayList<String>();


        //Information of every title
        HashMap<String, List<String>> ChildList = new HashMap<String, List<String>>();

        //Information of every title aditional
        HashMap<String, List<String>> ChildListAditional = new HashMap<String, List<String>>();

        //Information of every title cotidiana
        HashMap<String, List<String>> ChildListCotidiana = new HashMap<String, List<String>>();

        //Information of every title financial
        HashMap<String, List<String>> ChildListFinancial = new HashMap<String, List<String>>();

        //Information DR HELP
        String heading_items[] = getResources().getStringArray(R.array.header_titles);
        String l1[] = getResources().getStringArray(R.array.h1_items);
        String l2[] = getResources().getStringArray(R.array.h2_items);
        String l3[] = getResources().getStringArray(R.array.h3_items);
        String l4[] = getResources().getStringArray(R.array.h4_items);
        String l5[] = getResources().getStringArray(R.array.h5_items);
        String l6[] = getResources().getStringArray(R.array.h6_items);
        String l7[] = getResources().getStringArray(R.array.h7_items);
        String l8[] = getResources().getStringArray(R.array.h8_items);
        String l9[] = getResources().getStringArray(R.array.h9_items);
        String l10[] = getResources().getStringArray(R.array.h10_items);
        String l11[] = getResources().getStringArray(R.array.h11_items);

        //Information ADDITIONAL INFORMATION
        String heading_items_additional[] = getResources().getStringArray(R.array.header_titles_aditional);
        String l1A[] = getResources().getStringArray(R.array.h1A_items);
        String l2A[] = getResources().getStringArray(R.array.h2A_items);
        String l3A[] = getResources().getStringArray(R.array.h3A_items);
        String l4A[] = getResources().getStringArray(R.array.h4A_items);
        String l5A[] = getResources().getStringArray(R.array.h5A_items);

        //Information DAILY PROTECTION
        String heading_items_cotidiana[] = getResources().getStringArray(R.array.header_titles_cotidiana);
        String l1C[] = getResources().getStringArray(R.array.h1C_items);
        String l2C[] = getResources().getStringArray(R.array.h2C_items);
        String l3C[] = getResources().getStringArray(R.array.h3C_items);
        String l4C[] = getResources().getStringArray(R.array.h4C_items);
        String l5C[] = getResources().getStringArray(R.array.h5C_items);
        String l6C[] = getResources().getStringArray(R.array.h6C_items);

        //Information FINANCIALL INFORMATION
        String heading_items_financial[] = getResources().getStringArray(R.array.header_titles_financial);
        String l1F[] = getResources().getStringArray(R.array.h1F_items);
        String l2F[] = getResources().getStringArray(R.array.h2F_items);
        String l3F[] = getResources().getStringArray(R.array.h3F_items);


        //ADDING DR HELP
        for (String title : heading_items) {
            headings.add(title);
        }
        for (String title : l1) {
            L1.add(title);
        }
        for (String title : l2) {
            L2.add(title);
        }
        for (String title : l3) {
            L3.add(title);
        }
        for (String title : l4) {
            L4.add(title);
        }
        for (String title : l5) {
            L5.add(title);
        }
        for (String title : l6) {
            L6.add(title);
        }
        for (String title : l7) {
            L7.add(title);
        }
        for (String title : l8) {
            L8.add(title);
        }
        for (String title : l9) {
            L9.add(title);
        }
        for (String title : l10) {
            L10.add(title);
        }
        for (String title : l11) {
            L11.add(title);
        }

        //ADDING ADDITIONAL INFORMATION
        for (String title : heading_items_additional) {
            headings_aditional.add(title);
        }
        for (String title : l1A) {
            L1A.add(title);
        }
        for (String title : l2A) {
            L2A.add(title);
        }
        for (String title : l3A) {
            L3A.add(title);
        }
        for (String title : l4A) {
            L4A.add(title);
        }
        for (String title : l5A) {
            L5A.add(title);
        }

        //ADDING DAILY INFORMATION

        for (String title : heading_items_cotidiana) {
            headings_cotidiana.add(title);
        }
        for (String title : l1C) {
            L1C.add(title);
        }
        for (String title : l2C) {
            L2C.add(title);
        }
        for (String title : l3C) {
            L3C.add(title);
        }
        for (String title : l4C) {
            L4C.add(title);
        }
        for (String title : l5C) {
            L5C.add(title);
        }
        for (String title : l6C) {
            L6C.add(title);
        }

        //ADDING FINANCIAL INFORMATION
        for (String title : heading_items_financial) {
            headings_financial.add(title);
        }
        for (String title : l1F) {
            L1F.add(title);
        }
        for (String title : l2F) {
            L2F.add(title);
        }
        for (String title : l3F) {
            L3F.add(title);
        }


        //DR HELP INFORMATION EXPANDABLE
        ChildList.put(headings.get(0), L1);
        ChildList.put(headings.get(1), L2);
        ChildList.put(headings.get(2), L3);
        ChildList.put(headings.get(3), L4);
        ChildList.put(headings.get(4), L5);
        ChildList.put(headings.get(5), L6);
        ChildList.put(headings.get(6), L7);
        ChildList.put(headings.get(7), L8);
        ChildList.put(headings.get(8), L9);
        ChildList.put(headings.get(9), L10);
        ChildList.put(headings.get(10), L11);

        //ADDITIONAL INFORMATION EXPANDABLE
        ChildListAditional.put(headings_aditional.get(0), L1A);
        ChildListAditional.put(headings_aditional.get(1), L2A);
        ChildListAditional.put(headings_aditional.get(2), L3A);
        ChildListAditional.put(headings_aditional.get(3), L4A);
        ChildListAditional.put(headings_aditional.get(4), L5A);

        //DAYLY INFORMATION EXPANDABLE
        ChildListCotidiana.put(headings_cotidiana.get(0), L1C);
        ChildListCotidiana.put(headings_cotidiana.get(1), L2C);
        ChildListCotidiana.put(headings_cotidiana.get(2), L3C);
        ChildListCotidiana.put(headings_cotidiana.get(3), L4C);
        ChildListCotidiana.put(headings_cotidiana.get(4), L5C);
        ChildListCotidiana.put(headings_cotidiana.get(5), L6C);

        //FINANCIAL INFORMATION EXPANDABLE
        ChildListFinancial.put(headings_financial.get(0), L1F);
        ChildListFinancial.put(headings_financial.get(1), L2F);
        ChildListFinancial.put(headings_financial.get(2), L3F);


        MyAdapter myAdapter = new MyAdapter(this, headings, ChildList);
        MyAdapter myAdapter1 = new MyAdapter(this, headings_aditional, ChildListAditional);
        MyAdapter myAdapter2 = new MyAdapter(this, headings_cotidiana, ChildListCotidiana);
        MyAdapter myAdapter3 = new MyAdapter(this, headings_financial, ChildListFinancial);

        expandableListViewPrivilegeDrHelp.setAdapter(myAdapter);
        expandableListViewAditional.setAdapter(myAdapter1);
        expandableListViewCotidiana.setAdapter(myAdapter2);
        expandableListViewFinancial.setAdapter(myAdapter3);

        cotidiana_layout = (ViewGroup) root.findViewById(R.id.titlecotidiana_layout);
        aditional_separator_layout = (ViewGroup) root.findViewById(R.id.aditional_separator);
        cotidiana_list_layout = (ViewGroup) root.findViewById(R.id.daily_list_lay);
        financiera_title = (ViewGroup) root.findViewById(R.id.title_financiera);
        financiera_separator = (ViewGroup) root.findViewById(R.id.separator_financiera);
        financial_layout = (ViewGroup) root.findViewById(R.id.financial_list_lay);
        benefits_description = (ViewGroup) root.findViewById(R.id.benefits);
        benefits_list = (ViewGroup) root.findViewById(R.id.benefits_list);
        calltobuylayout = (ViewGroup) root.findViewById(R.id.call_to_buy);


        expandableListViewAditional.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                RelativeLayout.LayoutParams position = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams position2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams position4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position7 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position8 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position9 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);


                if (!flag) {

                    position9.setMargins(0, 14200, 0, 0);
                    position8.setMargins(0, 10400, 0, 0);
                    position7.setMargins(0, 10000, 0, 0);
                    position6.setMargins(0, 100, 0, 0);
                    position5.setMargins(0, 8710, 0, 0);
                    position4.setMargins(0, 1700, 0, 0);
                    position3.setMargins(0, 100, 0, 0);
                    position2.setMargins(0, 100, 0, 0);
                    position.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    position.setMargins(0, 1280, 0, 0);
                    cotidiana_layout.setLayoutParams(position);
                    aditional_separator_layout.setLayoutParams(position2);
                    cotidiana_list_layout.setLayoutParams(position3);
                    financiera_title.setLayoutParams(position4);
                    financiera_separator.setLayoutParams(position5);
                    financial_layout.setLayoutParams(position6);
                    benefits_description.setLayoutParams(position7);
                    benefits_list.setLayoutParams(position8);
                    calltobuylayout.setLayoutParams(position9);
                    flag = true;

                } else {

                    position9.setMargins(0, 14500, 0, 0);
                    position8.setMargins(0, 10800, 0, 0);
                    position7.setMargins(0, 10400, 0, 0);
                    position6.setMargins(0, 600, 0, 0);
                    position5.setMargins(0, 9120, 0, 0);
                    position4.setMargins(0, 2100, 0, 0);
                    position3.setMargins(0, 600, 0, 0);
                    position2.setMargins(0, 600, 0, 0);
                    position.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    position.setMargins(0, 1800, 0, 0);
                    cotidiana_layout.setLayoutParams(position);
                    aditional_separator_layout.setLayoutParams(position2);
                    cotidiana_list_layout.setLayoutParams(position3);
                    financiera_title.setLayoutParams(position4);
                    financiera_separator.setLayoutParams(position5);
                    financial_layout.setLayoutParams(position6);
                    benefits_description.setLayoutParams(position7);
                    benefits_list.setLayoutParams(position8);
                    calltobuylayout.setLayoutParams(position9);
                    flag = false;
                }
                return false;
            }
        });

        expandableListViewCotidiana.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                RelativeLayout.LayoutParams position = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams position2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams position4 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position5 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position6 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

                if (!flag) {

                    position.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    position.setMargins(0,1500,0,0);
                    position2.setMargins(0,8520,0,0);
                    position3.setMargins(0,20,0,0);
                    position4.setMargins(0,9800,0,0);
                    position5.setMargins(0,10200,0,0);
                    position6.setMargins(0,14000,50,0);
                    financiera_title.setLayoutParams(position);
                    financiera_separator.setLayoutParams(position2);
                    financial_layout.setLayoutParams(position3);
                    benefits_description.setLayoutParams(position4);
                    benefits_list.setLayoutParams(position5);
                    calltobuylayout.setLayoutParams(position6);
                    flag = true;

                } else {
                    position.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
                    position.setMargins(0,2100,0,0);
                    position2.setMargins(0,9120,0,0);
                    position3.setMargins(0,600,0,0);
                    position4.setMargins(0,10400,0,0);
                    position5.setMargins(0,10800,0,0);
                    position6.setMargins(0,14500,0,0);
                    financiera_title.setLayoutParams(position);
                    financiera_separator.setLayoutParams(position2);
                    financial_layout.setLayoutParams(position3);
                    benefits_description.setLayoutParams(position4);
                    benefits_list.setLayoutParams(position5);
                    calltobuylayout.setLayoutParams(position6);

                    flag = false;

                }

                return false;
            }
        });

        expandableListViewFinancial.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                RelativeLayout.LayoutParams position = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                RelativeLayout.LayoutParams position2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                RelativeLayout.LayoutParams position3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                if (!flag) {

                    position.setMargins(0,9800,0,0);
                    position2.setMargins(0,10200,0,0);
                    position3.setMargins(0,14000,0,0);
                    benefits_description.setLayoutParams(position);
                    benefits_list.setLayoutParams(position2);
                    calltobuylayout.setLayoutParams(position3);
                    flag = true;

                } else {

                    position.setMargins(0,10400,0,0);
                    position2.setMargins(0,10800,0,0);
                    position3.setMargins(0,14500,0,0);
                    benefits_description.setLayoutParams(position);
                    benefits_list.setLayoutParams(position2);
                    calltobuylayout.setLayoutParams(position3);
                    flag = false;

                }

                return false;
            }
        });

    }

    private void call() {
        callus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCall();
            }
        });
        calltobuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCall();
            }
        });
    }

    private void sendCall() {
        Intent intent = new Intent(this, CallActivityMcard.class);
        startActivity(intent);
    }

}