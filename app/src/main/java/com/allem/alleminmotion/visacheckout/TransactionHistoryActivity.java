package com.allem.alleminmotion.visacheckout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.adapters.ExpandableTransactionsAdapter;
import com.allem.alleminmotion.visacheckout.async.AsyncSoapObject;
import com.allem.alleminmotion.visacheckout.async.AsyncTaskSoapObjectResultEvent;
import com.allem.alleminmotion.visacheckout.async.MyBus;
import com.allem.alleminmotion.visacheckout.models.AllemUser;
import com.allem.alleminmotion.visacheckout.models.Compra;
import com.allem.alleminmotion.visacheckout.models.CuentaClienteInfo;
import com.allem.alleminmotion.visacheckout.models.CuentaClienteInfoAdicional;
import com.allem.alleminmotion.visacheckout.parsers.SoapObjectParsers;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.squareup.otto.Subscribe;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.HashMap;

public class TransactionHistoryActivity extends FrontBackAnimate implements FrontBackAnimate.InflateReadyListener {


    private ProgressBar pb_create;
    private Context ctx;
    private ArrayList<HashMap<String,String>> compras;
    private ArrayList<NameValuePair> postValues;
    private ExpandableTransactionsAdapter adapter ;
    private ExpandableListView lv_trans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        checkLogin();
        super.setView(R.layout.activity_transaction_history, this);

        MyBus.getInstance().register(this);


    }

    private void setViewElements(View root){
        postValues = new ArrayList<>();
        lv_trans = (ExpandableListView) root.findViewById(R.id.lv_transacciones);
        lv_trans.setVisibility(View.INVISIBLE);
        pb_create = (ProgressBar) root.findViewById(R.id.pbTransaccion);
        setCustomActionBar(root,true,getString(R.string.transactions_history));
        registerForContextMenu(lv_trans);

        adapter = new ExpandableTransactionsAdapter(this.ctx);

        lv_trans.setAdapter(adapter);

        final EditText tv_searchText = (EditText) root.findViewById(R.id.tv_searchText);
        tv_searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                adapter.filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
        Button calc_clear_txt = (Button) root.findViewById(R.id.clear_txt);
        calc_clear_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_searchText.setText("");
            }
        });
    }

    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }



    public void onMenu(View view) {
        animate();
    }


    private void setListeners(){

    }

    private void setWaitinUI(boolean b) {
        if (b) pb_create.setVisibility(View.VISIBLE);
        else pb_create.setVisibility(View.GONE);

    }

    @Override
    public void initViews(View root) {
        setViewElements(root);
        setListeners();
        loadBuyouts();
    }

    private void loadBuyouts() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()!=null)
        if (Util.hasInternetConnectivity(ctx)){

            setWaitinUI(true);
            Log.d(Constants.KEY_ID_SESSION+" app",((VisaCheckoutApp)this.getApplication()).getIdSession());
            postValues.add(new BasicNameValuePair(Constants.KEY_ID_SESSION, ((VisaCheckoutApp)this.getApplication()).getIdSession()));
            String wsdl;
            if (BuildConfig.DEBUG || Constants.TESTING) {
                wsdl = Constants.URL_ALLEM_WSDL_TEST;
            }else{
                wsdl = Constants.URL_ALLEM_WSDL_PROD;
            }
            AsyncSoapObject.getInstance(wsdl, Constants.NAMESPACE_ALLEM,
                    Constants.METHOD_OBTENER_COMPRAS, postValues, Constants.ACTIVITY_PROFILE_BUYOUTS).execute();
        }else{
            Toast.makeText(ctx, "No tiene conexión a internet", Toast.LENGTH_SHORT).show();
        }

    }

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {
        compras = new ArrayList<HashMap<String,String>>();
        if(event.getCodeRequest()==Constants.ACTIVITY_PROFILE_BUYOUTS){
            setWaitinUI(false);
            if(event.getResult()!=null){
                Log.d("History ", event.getResult().toString());
                if (event.getResult().getPropertyCount()==0) {
                    Log.d("History ", "No hay compras");
                    TextView empty = (TextView) findViewById(R.id.tv_emptyTransaction);
                    empty.setVisibility(View.VISIBLE);
                    lv_trans.setEmptyView(empty);
                }
                else{
                    compras = SoapObjectParsers.toComprasList(event.getResult(), SoapObjectParsers.DESC);
                    adapter = new ExpandableTransactionsAdapter(ctx);
                    adapter.setTransactions(compras);
                    lv_trans.setAdapter(adapter);
                    lv_trans.setVisibility(View.VISIBLE);
                }
            }else{
                Log.d("History ", "Error en la consulta del WS : " + event.getFaultString());
            }
        }
    }


    private void checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(ctx,LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
            finish();
        }
    }

}
