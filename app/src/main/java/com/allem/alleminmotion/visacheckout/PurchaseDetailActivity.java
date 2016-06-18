package com.allem.alleminmotion.visacheckout;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.allem.alleminmotion.visacheckout.async.AsyncDownload;
import com.allem.alleminmotion.visacheckout.async.AsyncTaskBooleanResultEvent;
import com.allem.alleminmotion.visacheckout.async.MyBus;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.GraphicsUtils;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by victor on 19/02/15.
 * ${PACKAGE_NAME}
 */
public class PurchaseDetailActivity extends Activity {
    private static final String TAG="PurchaseDetailActivity";
    private static final int QRSCANNER_ID = 9382;


    private ActionBar actionBar;
    private Context ctx;
    private WebView wv_pdf;
    private String voucher,savepath;
    private ProgressBar pb_loadpdf;
    private RelativeLayout rl_voucher;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ctx=this;

        //Registro el Bus que se va a encargar de manejar el hilo de ejecución
        //Del AsyncDownload que descargará el archivo pdf.
        MyBus.getInstance().register(ctx);
        savepath = Environment.getExternalStorageDirectory() + "/" +ctx.getResources().getString(R.string.app_name);
        voucher=getIntent().getStringExtra(Constants.KEY_COMPRAS_URL_VOUCHER);
        Log.d(TAG,"voucher: "+voucher);
        setView();
        setListeners();
    }


    // Si se destruye la actividad elimino el registro del Bus
    @Override
    protected void onDestroy() {
        MyBus.getInstance().unregister(this);
        super.onDestroy();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    private void setView(){
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);  //permits the action bar overlay over the screen
        setContentView(R.layout.activity_purchase_detail);


        wv_pdf = (WebView) findViewById(R.id.wv_pdf);
        pb_loadpdf = (ProgressBar)findViewById(R.id.pb_loadpdf);
        rl_voucher = (RelativeLayout)findViewById(R.id.rl_voucher);
        rl_voucher.setVisibility(View.VISIBLE);
        //Using a webview to load a pdf via google docs
        WebSettings webSettings = wv_pdf.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN) //required for running javascript on android 4.1 or later
        {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        wv_pdf.setWebViewClient(new MyWebViewClient());
        /* zoom to content */
        wv_pdf.getSettings().setUseWideViewPort(true);
        wv_pdf.getSettings().setLoadWithOverviewMode(true);

        setCustomActionBar(true,getString(R.string.purchase_detail));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.hasInternetConnectivity(ctx)){
            loadpdf();
        }else{
            Toast.makeText(ctx, "No tiene conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            pb_loadpdf.setVisibility(View.GONE);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            pb_loadpdf.setVisibility(View.VISIBLE);
        }

    }

    private void setListeners() {
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
           /* case R.id.action_qr:
                if(qr){
                    rl_voucher.setVisibility(View.VISIBLE);
                }else{
                    rl_voucher.setVisibility(View.GONE);
                }
                qr=!qr;
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadpdf() {

        pb_loadpdf.setVisibility(View.VISIBLE);
        Log.d(TAG,"parameter: "+voucher+"\n"+savepath+"\n"+Constants.TEMPFILE_PDF);
        AsyncDownload.getInstance(voucher, savepath, Constants.TEMPFILE_PDF, Constants.ACTIVITY_BUYOUTS_DETAIL).execute();
    }

    private void setCustomActionBar(boolean backOption, String activityTitle){
        TextView title = (TextView)findViewById(R.id.tv_title_secc);
        ImageButton backButton = (ImageButton) findViewById(R.id.btn_back);
        ImageButton menuButton = (ImageButton) findViewById(R.id.ib_menu);
        menuButton.setVisibility(View.INVISIBLE);
        if (backOption) backButton.setVisibility(View.VISIBLE); else backButton.setVisibility(View.INVISIBLE);
        title.setText(activityTitle);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Subscribe
    public void onAsyncTaskResult(AsyncTaskBooleanResultEvent event) {
        if(event.getCodeRequest()==Constants.ACTIVITY_BUYOUTS_DETAIL){
            pb_loadpdf.setVisibility(View.GONE);
            if(event.getResult()!=null){
                Log.d(TAG,"se descargó el pdf en "+savepath+"/"+Constants.TEMPFILE_PDF);
                File pdfFile = new File(savepath+"/",Constants.TEMPFILE_PDF);
                final Uri uri = Uri.fromFile(pdfFile);
                Log.d(TAG,uri.toString());

                try {
                    InputStream ims = getAssets().open("pdfviewer/index.html");
                    String line = getStringFromInputStream(ims);
                    if(line.contains("THE_FILE")) {
                        line = line.replace("THE_FILE", uri.toString());
                        line = line.replace("CANVAS_WIDTH", String.valueOf(GraphicsUtils.getWindowSize(ctx).x));
                        line = line.replace("CANVAS_HEIGHT", String.valueOf(GraphicsUtils.getWindowSize(ctx).y));
                        Log.d(TAG,"line: "+line);
                        String pdfurl = "file://" + getFilesDir() + "/index.html";
                        Log.d(TAG,"pdfurl:"+pdfurl);
                        wv_pdf.clearCache(true);
                        wv_pdf.loadDataWithBaseURL("file:///android_asset/pdfviewer/",line,"text/html", "UTF-8", "");
                    }
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }else{
                //Hubo algún problema al actualizar, se puede reintentar la descarga, se pude usar un
                //contador para realizar un determinado número de intentos.
            }
        }
    }


    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}