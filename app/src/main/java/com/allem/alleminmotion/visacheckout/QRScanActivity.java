package com.allem.alleminmotion.visacheckout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allem.alleminmotion.visacheckout.async.AsyncSoapObject;
import com.allem.alleminmotion.visacheckout.async.AsyncTaskSoapObjectResultEvent;
import com.allem.alleminmotion.visacheckout.async.MyBus;
import com.allem.alleminmotion.visacheckout.models.Server;
import com.allem.alleminmotion.visacheckout.models.SolicitudCobro;
import com.allem.alleminmotion.visacheckout.utils.Constants;
import com.allem.alleminmotion.visacheckout.utils.Contents;
import com.allem.alleminmotion.visacheckout.utils.GraphicsUtils;
import com.allem.alleminmotion.visacheckout.utils.QRCodeEncoder;
import com.allem.alleminmotion.visacheckout.utils.Util;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.otto.Subscribe;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.math.BigDecimal;
import java.util.ArrayList;


public class QRScanActivity extends FrontBackAnimate{// implements FrontBackAnimate.InflateReadyListener

    //**************GLOBAL ATTRIBUTES************

    private static final String TAG = "QRScanner" ;
    private ImageView iv_qrcode;
    private ArrayList<NameValuePair> postValues;
    SolicitudCobro solicitud;
    TextView vendor, itemsValue, total, taxes;
    private Button pay;
    private ProgressBar pb_create;
    private String currency, reference;

    //************OVERRIDE AND SUBSCRIBE METHODS

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        MyBus.getInstance().register(this);
        postValues = new ArrayList<>();
        //setContentView(R.layout.activity_qrscan);
        //setView(R.layout.activity_qrscan, this);
        if (checkLogin()) {
            openScan();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Subscribe
    public void onAsyncTaskResult(AsyncTaskSoapObjectResultEvent event) {
        //setWaitinUI(false);
        switch(event.getCodeRequest()) {
            case Constants.ACTIVITY_PAY_DETALLE:
                if (event.getResult() != null) {

                    solicitud = PayParsers.toSolicitudCobro(QRScanActivity.this, event.getResult());
                    //vendor.setText(solicitud.razonSocial);
                    BigDecimal beforeTax = solicitud.baseDevolucion;
                    BigDecimal tmpBase = beforeTax.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal adjbeforeTax  = tmpBase.setScale(0, BigDecimal.ROUND_HALF_UP);
                    //itemsValue.setText("$ " + adjbeforeTax);

                    BigDecimal tmpTotal = solicitud.valor.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal adjTotal = tmpTotal.setScale(0, BigDecimal.ROUND_HALF_UP);
                    //total.setText("$ " + adjTotal);

                    BigDecimal tax = solicitud.iva.multiply(solicitud.baseDevolucion).divide(new BigDecimal("100"));
                    BigDecimal tmpIVA = tax.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal adjIVA = tmpIVA.setScale(0, BigDecimal.ROUND_HALF_UP);
                    //taxes.setText("$  " + adjIVA);

                    currency = solicitud.moneda;
                    reference = solicitud.referencia;

                    //Testing
                    Intent intent2 = new Intent(QRScanActivity.this, OnepocketPurchaseActivity.class);
                    Bundle bundle = new Bundle();
                    String data = "{\"businessName\":\"" + solicitud.razonSocial +//vendor.getText()
                            "\",\"purchaseValue\":\"" + ("$ " + adjTotal).toString().replace("$", "").trim() +//total.getText()
                            "\",\"taxValue\":\"" + ("$  " + adjIVA).toString().replace("$", "").trim() +
                            "\",\"devolutionBasisTax\":\"" + ("$ " + adjbeforeTax).toString().replace("$", "").trim() +
                            "\",\"codeISO\":\"" + currency +
                            "\",\"reference\":\"" + reference +
                            "\"}";
                    OneTransaction transaction = new OneTransaction();
                    transaction.add("jsonPayment", data);
                    transaction.add("type", OPKConstants.TYPE_QRCODE);
                    transaction.add("sessionId", ((VisaCheckoutApp)QRScanActivity.this.getApplication()).getIdSession());
                    transaction.add("first", Constants.getUser(QRScanActivity.this).nombre);
                    transaction.add("last", Constants.getUser(QRScanActivity.this).apellido);
                    transaction.add("userName", Constants.getUser(QRScanActivity.this).email);
                    transaction.add("rawPassword", ((VisaCheckoutApp) getApplication()).getRawPassword());
                    transaction.add("idCuenta", Integer.toString(Constants.getUser(QRScanActivity.this).idCuenta));
                    transaction.add("merchantName", solicitud.usuarioCuenta);
                    transaction.add("merchantPassword", solicitud.contrasenaCuenta);
                    bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, transaction);
                    intent2.putExtras(bundle);
                    startActivity(intent2);

                    finish();
                } else {

                    Log.e(TAG, event.getFaultString());
                    Toast.makeText(QRScanActivity.this, event.getFaultString(), Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            }
        }

    //***************PROPER METHODS************

    public void onMenu(View view) {
        animate();
    }

    private Bitmap drawQRCode(String data,int size) throws Exception
    {
        Bitmap bitmap = null;
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(data, null, Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                size);

        try {

            bitmap = qrCodeEncoder.encodeAsBitmap();

        } catch (WriterException e) {
            Log.d(TAG, "Error in drawing QRCode: ", e);
        }
        return bitmap;
    }

    private void obtenerTicket(String idSolicitud) {
        if (Util.hasInternetConnectivity(this)){
            //setWaitinUI(true);
            Log.d(TAG,Constants.KEY_ID_SOLICITUD+":"+idSolicitud);
            postValues.add(new BasicNameValuePair(Constants.KEY_ID_SOLICITUD,idSolicitud));
            AsyncSoapObject.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                    Constants.METHOD_OBTENER_SOLICITUD_DE_PAGO, postValues, Constants.ACTIVITY_PAY_DETALLE).execute();
        }else{
            Toast.makeText(this, "No tiene conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult != null) {

            String id = scanResult.getContents();
            if (resultCode == -1) {
                Log.d("resultado QR", id);
                try {
                    //iv_qrcode.setImageBitmap(drawQRCode(id, GraphicsUtils.getWindowSize(this).x));
                    obtenerTicket(id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }else{
                //Log.d("backing", id);
                finish();
            }

        }
        if (requestCode == Constants.ACTIVITY_LOGIN) {
            openScan();
        }

    }

    private void openScan(){
        Log.d("Entra a","openScan");
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomQrActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(getResources().getString(R.string.qr_scan_description));
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }


    private boolean checkLogin() {
        if(((VisaCheckoutApp)this.getApplication()).getIdSession()==null){
            Intent i =new Intent(QRScanActivity.this,LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);
            return false;
        }
        return true;
    }



}
