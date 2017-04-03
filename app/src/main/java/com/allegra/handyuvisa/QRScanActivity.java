package com.allegra.handyuvisa;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.allegra.handyuvisa.async.AsyncSoapObject;
import com.allegra.handyuvisa.async.AsyncTaskSoapObjectResultEvent;
import com.allegra.handyuvisa.async.MyBus;
import com.allegra.handyuvisa.models.SolicitudCobro;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.utils.Contents;
import com.allegra.handyuvisa.utils.QRCodeEncoder;
import com.allegra.handyuvisa.utils.Util;
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


public class QRScanActivity extends Fragment{// implements FrontBackAnimate.InflateReadyListener

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
    public static final int PERMISSIONS_REQUEST_CAMERA = 10001;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        MyBus.getInstance().register(this);
        postValues = new ArrayList<>();
        //setContentView(R.layout.activity_qrscan);
        //setView(R.layout.activity_qrscan, this);
        if (checkLogin()) {
            if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                openScan();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_CAMERA);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    openScan();

                } else {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    //finish();
                }
            }
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

                    solicitud = PayParsers.toSolicitudCobro(getActivity(), event.getResult());
                    //vendor.setText(solicitud.razonSocial);
                    BigDecimal beforeTax = solicitud.baseDevolucion;
                    BigDecimal tmpBase = beforeTax.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal adjbeforeTax  = tmpBase.setScale(0, BigDecimal.ROUND_HALF_UP);
                    //itemsValue.setText("$ " + adjbeforeTax);

                    BigDecimal tmpTotal = solicitud.valor.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal adjTotal = tmpTotal.setScale(0, BigDecimal.ROUND_HALF_UP);
                    //total.setText("$ " + adjTotal);

                    //Change in order to fix problem with tax

                    BigDecimal tax = solicitud.iva.multiply(solicitud.baseDevolucion).divide(new BigDecimal("100"));
                    BigDecimal tmpIVA = tax.setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal adjIVA = tmpIVA.setScale(0, BigDecimal.ROUND_HALF_UP);

                  //  Log.d(TAG, "El valor es: "+solicitud.valor.toString());
                   // Log.d(TAG, "El iva es: "+ solicitud.iva.toString());
                   // Log.d(TAG, "La baseDevolucion  es: "+solicitud.baseDevolucion.toString());

                    //taxes.setText("$  " + adjIVA);
                    //Log.d(TAG, "El adjIVA es: "+adjIVA.toString());

                    currency = solicitud.moneda;
                    reference = solicitud.referencia;

                    //Testing
                    //Intent intent2 = new Intent(this, OnepocketPurchaseActivity.class);
                    String data = "{\"businessName\":\"" + solicitud.razonSocial +//vendor.getText()
                            "\",\"purchaseValue\":\"" + ("$ " + solicitud.valor).toString().replace("$", "").trim() +//total.getText() adjTotal
                            "\",\"taxValue\":\"" + ("$  " + solicitud.iva).toString().replace("$", "").trim() +//adjIVA
                            "\",\"devolutionBasisTax\":\"" + ("$ " + solicitud.baseDevolucion).toString().replace("$", "").trim() +//adjbeforeTax
                            "\",\"codeISO\":\"" + currency +
                            "\",\"reference\":\"" + reference +
                            "\"}";
                    Bundle bundle = Constants.createQRPurchaseBundle(Constants.getUser(getActivity())
                            , solicitud, data, OPKConstants.TYPE_QRCODE,
                            (com.allegra.handyuvisa.VisaCheckoutApp) getActivity().getApplication());

                    OnepocketPurchaseActivity fragmentOPKPurchase = new OnepocketPurchaseActivity();
                    fragmentOPKPurchase.setArguments(bundle);

                    ((MainActivity) getActivity()).replaceLayout(fragmentOPKPurchase, false);

                    /*intent2.putExtras(bundle);
                    startActivity(intent2);*/

                    //finish();
                } else {

                    //Log.e(TAG, event.getFaultString());
                    Toast.makeText(getActivity(), event.getFaultString(), Toast.LENGTH_LONG).show();
                    //finish();
                }
                break;
            }
        }

    //***************PROPER METHODS************

    public void onMenu(View view) {
        //((MainActivity) getActivity()).animate();
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
            //Log.d(TAG, "Error in drawing QRCode: ", e);
        }
        return bitmap;
    }

    private void obtenerTicket(String idSolicitud) {
        if (Util.hasInternetConnectivity(getActivity())){
            //setWaitinUI(true);
            //Log.d(TAG,Constants.KEY_ID_SOLICITUD+":"+idSolicitud);
            postValues.add(new BasicNameValuePair(Constants.KEY_ID_SOLICITUD,idSolicitud));
            AsyncSoapObject.getInstance(Constants.getWSDL(), Constants.NAMESPACE_ALLEM,
                    Constants.METHOD_OBTENER_SOLICITUD_DE_PAGO, postValues, Constants.ACTIVITY_PAY_DETALLE).execute();
        }else{
            Toast.makeText(getActivity(), "No tiene conexión a internet", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanResult != null) {

            String id = scanResult.getContents();

            Log.e("OPK", "result data is -- "+id);
            if (resultCode == -1) {
                //Log.d("resultado QR", id);
                try {
                    //iv_qrcode.setImageBitmap(drawQRCode(id, GraphicsUtils.getWindowSize(this).x));
                    obtenerTicket(id);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }else{
                //Log.d("backing", id);
                //finish();
            }

        }
        if (requestCode == Constants.ACTIVITY_LOGIN) {
            openScan();
        }

    }

    private void openScan(){
        //Log.d("Entra a","openScan");
        //IntentIntegrator integrator = new IntentIntegrator(getActivity());
        IntentIntegrator integrator = IntentIntegrator.forFragment(this);
        integrator.setCaptureActivity(CustomQrActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt(getResources().getString(R.string.qr_scan_description));
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }


    private boolean checkLogin() {
        if(((com.allegra.handyuvisa.VisaCheckoutApp)getActivity().getApplication()).getIdSession()==null){

            ((MainActivity) getActivity()).replaceLayout(new LoginActivity(), false);

            /*Intent i =new Intent(QRScanActivity.this,LoginActivity.class);
            this.startActivityForResult(i, Constants.ACTIVITY_LOGIN);*/
            return false;
        }
        return true;
    }



}
