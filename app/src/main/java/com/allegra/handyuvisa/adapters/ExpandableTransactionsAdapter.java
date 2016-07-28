package com.allegra.handyuvisa.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.allegra.handyuvisa.PurchaseDetailActivity;
import com.allegra.handyuvisa.models.TransactionDetails;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.R;
import com.google.common.collect.Iterables;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import static com.google.common.collect.Lists.newArrayList;
import static java.text.NumberFormat.getCurrencyInstance;
import static android.text.format.DateUtils.isToday;
import static java.util.Arrays.asList;
import static com.google.common.collect.Iterables.all;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.filterValues;
import static com.google.common.collect.Maps.transformValues;


public  class ExpandableTransactionsAdapter extends BaseExpandableListAdapter {
    public static final String ACCORDION_GROUP = "accordion_group";
    //GLOBAL ATTRIBUTES
    private final Integer[] groups = {R.string.TODAY,
            R.string.SAME_WEEK,
            R.string.SAME_MONTH,
            R.string.BEFORE};
    private final Context ctx;
    private final HashMap<String, List<HashMap<String, String>>> grouped = new HashMap<>();
    @NonNull
    private Map<String, List<HashMap<String, String>>> filtered = new HashMap<>();
    //New variables to compare date, by Sergio Farfan
    private Calendar c = new GregorianCalendar();
    private Calendar today = Calendar.getInstance();

    //CONSTRUCTOR
    public ExpandableTransactionsAdapter(Context ctx){
        this.ctx = ctx;
    }

    //This method is called in TransactionHistoryFragment´s line 148
    public void setTransactions(List<HashMap<String, String>> transactions) {

        decorate(ctx, transactions);
        for (int s:groups) {
            String group = ctx.getResources().getString(s);

            Iterable<HashMap<String, String>> filtered = Iterables.filter(transactions, pOnGroup(group));
            ArrayList newArray = newArrayList(filtered);

            int size = newArray.size();
            Log.d("setting transactions:",group+" "+size);
            grouped.put(group, newArray);
        }
        this.filter("");
    }



    //Returns group´s name to caller method : decorate ()
    private String group(Context context, Date date) {

        if (isToday(date.getTime()) ||  getArrayName(date, 0)) return context.getString(R.string.TODAY);
        else if (isSameWeekAsToday(date)) return context.getString(R.string.SAME_WEEK);
        else if (isSameMonthAsToday(date)) return context.getString(R.string.SAME_MONTH);
        else return context.getString(R.string.BEFORE);
    }

    //Method for verify future dates and send them to TODAY group, by Sergio Farfan
    private  boolean getArrayName(Date date, int numberOfDays){

        c.setTime(date);
        today.add(Calendar.DAY_OF_MONTH, -numberOfDays);
        return c.getTimeInMillis() > today.getTimeInMillis() ;
    }



    private static boolean isSameMonthAsToday(Date date) {

        Calendar c = new GregorianCalendar();
        c.setTime(date);
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -30);
        return c.getTimeInMillis() > today.getTimeInMillis();
    }

    private static boolean isSameWeekAsToday(Date date) {

        Calendar c = new GregorianCalendar();
        c.setTime(date);
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_MONTH, -7);
        return c.getTimeInMillis() > today.getTimeInMillis();
    }

    private void decorate(Context context, List<HashMap<String, String>> list) {

        for (HashMap<String, String> tr:list) {
            Date date = Constants.parseDate(tr.get(TransactionDetails.DATE));
            tr.put(ACCORDION_GROUP, group(context, date));
            int listSize = list.size();
        }
    }

    //OVERRIDE METHODS

    @Override
    public int getGroupCount() {

        return grouped.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String group = getGroup(groupPosition);
        List<HashMap<String,String>> childrenList= filtered.get(group);
        if(childrenList==null){
            return 0;
        }
        return childrenList.size();
    }

    @Override
    public String getGroup(int groupPosition) {

        return ctx.getString(groups[groupPosition]);
    }


    @Override
    public HashMap<String, String> getChild(int groupPosition, int childPosition) {
//exeption filtered.get()group
        String group =  getGroup(groupPosition);
        if(filtered!=null && filtered.get(group) !=null && childPosition<filtered.get(group).size())  return filtered.get(group).get(childPosition); else return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d("group view","Group view ");
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View grid = inflater.inflate(R.layout.fragment_date_group, null);

        TextView tv = (TextView) grid.findViewById(R.id.date_group);
        //tv.setTypeface(Application.muli(grid.getContext()));
        ArrayList historyList = (ArrayList) grouped.get(ctx.getResources().getString(groups[groupPosition]));
        tv.setText(ctx.getString(groups[groupPosition])+" - ("+String.valueOf(historyList.size()+")"));

        return grid;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final HashMap<String, String> item = getChild(groupPosition, childPosition);
        View grid = inflater.inflate(R.layout.item_transaction, null);


        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,PurchaseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.KEY_COMPRAS_ID_COMPRAS, item.get(TransactionDetails.ID_COMPRA));
                bundle.putString(Constants.KEY_COMPRAS_FECHA,item.get(TransactionDetails.DATE));
                bundle.putString(Constants.KEY_COMPRAS_REF,item.get(TransactionDetails.REFERENCE));
                bundle.putString(Constants.KEY_COMPRAS_COMERCIO,item.get(TransactionDetails.MERCHANT));
                bundle.putString(Constants.KEY_COMPRAS_VALOR,item.get(TransactionDetails.AMOUNT));
                bundle.putString(Constants.KEY_COMPRAS_MONEDA,item.get(TransactionDetails.CURRENCY));
                bundle.putString(Constants.KEY_COMPRAS_URL_DETALLE,item.get(TransactionDetails.URL_DETALLE_COMPRA));
                bundle.putString(Constants.KEY_COMPRAS_URL_VOUCHER,item.get(TransactionDetails.URL_VOUCHER_COMPRA));
                bundle.putString(Constants.KEY_TRANS_ID,item.get(TransactionDetails.ID_TRANSACTION_NUMBER));
                intent.putExtras(bundle);
                ctx.startActivity(intent);
            }
        });

        TextView tv_id = (TextView) grid.findViewById(R.id.transaction_value);
        if(item==null){
            return grid;
        }
        tv_id.setText(item.get(TransactionDetails.AMOUNT)+" "+ item.get(TransactionDetails.CURRENCY));
        TextView tv_client_name = (TextView) grid.findViewById(R.id.tv_client_name);
        String buyerName = item.get(TransactionDetails.MERCHANT) ;
        tv_client_name.setText(buyerName);
        TextView tv_date = (TextView) grid.findViewById(R.id.tv_transation_date);
        tv_date.setText(item.get(TransactionDetails.DATE));
        TextView[] bold = {tv_client_name, tv_date};


        return grid;

    }

    private NumberFormat getCurrencyFormat(String currency) {
        String language = preferredLanguage();
        String region = currency.substring(0, 2);
        NumberFormat cf = getCurrencyInstance(new Locale(language, region));
        cf.setMaximumFractionDigits(Constants.currencyScaleFactor.get(currency));
        cf.setCurrency(Currency.getInstance(currency));
        return cf;
    }

    private String preferredLanguage() {

        return ctx.getResources()
                .getConfiguration().locale.getLanguage();
    }

    private Predicate<HashMap<String, String>> pOnGroup(final String group) {

        return new Predicate<HashMap<String, String>>() {
            @Override
            public boolean apply(HashMap<String, String> input) {
                return group.equals(input.get(ACCORDION_GROUP));
            }
        };
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return false;
    }

    public void filter(final String s) {

        final List<String> tokens = asList(s.split(" "));
        filtered = filterValues(
                transformValues(grouped, new Function<List<HashMap<String, String>>, List<HashMap<String, String>>>() {
                    @Override
                    public List<HashMap<String, String>> apply(List<HashMap<String, String>> l) {
                        return newArrayList(Iterables.filter(l, new Predicate<HashMap<String, String>>() {
                            @Override
                            public boolean apply(HashMap<String, String> transaction) {
                                final String tr = transaction
                                        .toString()
                                        .toLowerCase();
                                return all(tokens, new Predicate<String>() {
                                    @Override
                                    public boolean apply(String token) {
                                        return tr.contains(token.toLowerCase());
                                    }
                                });
                            }
                        }));
                    }
                }), new Predicate<List<HashMap<String, String>>>() {
                    @Override
                    public boolean apply(List<HashMap<String, String>> input) {
                        return !input.isEmpty();
                    }
                })
        ;
        notifyDataSetChanged();
    }

}



