package com.allegra.handyuvisa;

import android.content.Context;
import android.util.Log;


import com.allegra.handyuvisa.models.Compra;
import com.allegra.handyuvisa.models.SolicitudCobro;
import com.allegra.handyuvisa.models.Transaccion;
import com.allegra.handyuvisa.utils.Constants;

import org.ksoap2.serialization.SoapObject;

import java.math.BigDecimal;
import java.util.ArrayList;

public class PayParsers {

    private static final String TAG = "PayParsers";
    public static final boolean ASC=true;
    public static final boolean DESC=false;

    public static Transaccion toTransaccion(SoapObject soapObject){
        String numAuth = soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_NUM_AUTH,"-1");
        if (numAuth.equals("anyType{}"))numAuth="0";
        return new Transaccion(Integer.valueOf(soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_ID,"-1")),
                Integer.valueOf(numAuth),
                Integer.valueOf(soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_ESTADO_ID,"-1")),
                soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_REF,""),
                soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_ESTADO,""),
                soapObject.getPropertySafelyAsString(Constants.KEY_COMPRAS_MONEDA,""),
                soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_URL_REDIR,""),
                Float.valueOf(soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_VALOR,"-1")));
    }

    public static Transaccion toTransaccionVuelos(SoapObject soapObject){
        String numAuth = soapObject.getPropertySafelyAsString("codigoAutorizacion","-1");
        if (numAuth.equals("anyType{}"))numAuth="0";
        return new Transaccion(Integer.valueOf(soapObject.getPropertySafelyAsString("idTransaccion","-1")),
                Integer.valueOf(numAuth),
                Integer.valueOf(soapObject.getPropertySafelyAsString("idEstado","-1")),
                soapObject.getPropertySafelyAsString("referencia",""),
                soapObject.getPropertySafelyAsString("nombreEstado",""),
                soapObject.getPropertySafelyAsString("isoMoneda",""),
                "about:blank",
                Float.valueOf(soapObject.getPropertySafelyAsString("valor","-1")));
    }

    public static SolicitudCobro toSolicitudCobro(Context ctx, SoapObject soapObject){
        SoapObject soapCobro = soapObject;
        //SoapObject soapCobro = (SoapObject) soapObject.getProperty(0);
        return new SolicitudCobro(soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_MONEDA, ""),
                soapCobro.getPropertySafelyAsString(Constants.KEY_COBRO_NIT, ""),
                soapCobro.getPropertySafelyAsString(Constants.KEY_COMERCIO_RSOCIAL, ""),
                soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_REF, ""),
                new BigDecimal(soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_VALOR, "0")),
                new BigDecimal(soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_IVA, "0")),
                Integer.valueOf(soapCobro.getPropertySafelyAsString(Constants.KEY_COMERCIO_ID,"0")),
                Integer.valueOf(soapCobro.getPropertySafelyAsString(Constants.KEY_ID_ACCOUNT,"0")),
                new BigDecimal(soapCobro.getPropertySafelyAsString(Constants.KEY_BASE_DEVOLUCION, "0")),
                soapCobro.getPropertySafelyAsString(Constants.KEY_USUARIOCUENTA),
                soapCobro.getPropertySafelyAsString(Constants.KEY_CONTRASENACUENTA));
    }

    public static ArrayList<Compra> toCompras(SoapObject soapObject,boolean order){
        ArrayList<Compra> comprasItems = new ArrayList<>();
        int k=0;
        for(int i=0;i<soapObject.getPropertyCount();i++){
            if (order)k=i;
            else k=soapObject.getPropertyCount()-1-i;
            Log.d(TAG, soapObject.getProperty(i).toString());
            SoapObject soapCompra = (SoapObject) soapObject.getProperty(k);
            Compra compra = new Compra(Integer.valueOf(soapCompra.getProperty(Constants.KEY_COMPRAS_ID_COMPRAS).toString()),
                    soapCompra.getProperty(Constants.KEY_COMPRAS_FECHA).toString(),
                    soapCompra.getProperty(Constants.KEY_COMPRAS_REF).toString(),
                    soapCompra.getProperty(Constants.KEY_COMPRAS_COMERCIO).toString(),
                    soapCompra.getProperty(Constants.KEY_COMPRAS_VALOR).toString(),
                    soapCompra.getProperty(Constants.KEY_COMPRAS_MONEDA).toString(),
                    soapCompra.getProperty(Constants.KEY_COMPRAS_URL_VOUCHER).toString(),
                    soapCompra.getProperty(Constants.KEY_COMPRAS_URL_DETALLE).toString(),
                    soapCompra.getProperty(Constants.KEY_TRANS_ID).toString());
            comprasItems.add(compra);
        }
        return comprasItems;
    }

}
