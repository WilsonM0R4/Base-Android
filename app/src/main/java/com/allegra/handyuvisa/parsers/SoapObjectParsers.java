package com.allegra.handyuvisa.parsers;

import android.util.Log;

import com.allegra.handyuvisa.models.McardCliente;
import com.allegra.handyuvisa.utils.Constants;
import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.models.Compra;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by victor on 28/02/15.
 * com.allem.allemevent.parsers
 */
public class SoapObjectParsers {

    public static final boolean ASC=true;
    public static final boolean DESC=false;
    public static final String TAG="SoapObjectParsers";


    public static McardCliente toMcardCliente(SoapObject soapObject){
        McardCliente mcardCliente = null;
        String idProducto = "";
        if (soapObject.hasProperty("idProducto")){
            idProducto= soapObject.getPropertyAsString("idProducto");
        }
        mcardCliente = new McardCliente(soapObject.getPropertyAsString("idProducto"));
        return  mcardCliente;
    }


    public static AllemUser toAllemUser(SoapObject soapObject){
        AllemUser allemUser = null;
        String saludo="",idSesion="", celular="", idNumber="", idType="";
        if (soapObject.hasProperty("saludo")){
            saludo= soapObject.getPropertyAsString("saludo");
        }
        if (soapObject.hasProperty("idSesion")){
            idSesion= soapObject.getPropertyAsString("idSesion");
        }
        if (soapObject.hasProperty("cuentaClienteInformacionAdicional")){
            SoapObject additionalInfo= (SoapObject)soapObject.getProperty("cuentaClienteInformacionAdicional");
            if(additionalInfo.hasProperty("celular")) celular= additionalInfo.getPropertyAsString("celular");
            if(additionalInfo.hasProperty("pais")) celular= additionalInfo.getPropertyAsString("pais");
            if(additionalInfo.hasProperty("celular_codigo")) celular= additionalInfo.getPropertyAsString("celular_codigo");
        }

        if (soapObject.hasProperty("numeroDocumento")){
            idNumber= soapObject.getPropertyAsString("numeroDocumento");

        }

        if (soapObject.hasProperty("tipoDocumento")){
            idType= soapObject.getPropertyAsString("tipoDocumento");
        }


        allemUser = new AllemUser(saludo,
                soapObject.getPropertyAsString("nombre"),
                soapObject.getPropertyAsString("apellido"),
                soapObject.getPropertyAsString("email"),
                soapObject.getPropertyAsString("password"),
                idSesion,
                Integer.valueOf(soapObject.getProperty("idCuenta").toString()),
                Boolean.valueOf(soapObject.getProperty("estado").toString()),celular,idNumber,idType);

        return allemUser;
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

    public static ArrayList<HashMap<String, String>> toComprasList(SoapObject soapObject,boolean order){
        ArrayList<HashMap<String, String>>  comprasItems = new ArrayList<>();
        int k=0;
        for(int i=0;i<soapObject.getPropertyCount();i++){
            if (order)k=i;
            else k=soapObject.getPropertyCount()-1-i;
            Log.d(TAG, soapObject.getProperty(i).toString());
            HashMap<String,String> compra = new HashMap<>();
            SoapObject soapCompra = (SoapObject) soapObject.getProperty(k);

            compra.put(Constants.KEY_COMPRAS_ID_COMPRAS,soapCompra.getProperty(Constants.KEY_COMPRAS_ID_COMPRAS).toString());
            compra.put(Constants.KEY_COMPRAS_FECHA,soapCompra.getProperty(Constants.KEY_COMPRAS_FECHA).toString());
            compra.put(Constants.KEY_COMPRAS_REF,soapCompra.getProperty(Constants.KEY_COMPRAS_REF).toString());
            compra.put(Constants.KEY_COMPRAS_COMERCIO,soapCompra.getProperty(Constants.KEY_COMPRAS_COMERCIO).toString());
            compra.put(Constants.KEY_COMPRAS_VALOR,soapCompra.getProperty(Constants.KEY_COMPRAS_VALOR).toString());
            compra.put(Constants.KEY_COMPRAS_MONEDA,soapCompra.getProperty(Constants.KEY_COMPRAS_MONEDA).toString());
            compra.put(Constants.KEY_COMPRAS_URL_VOUCHER,soapCompra.getProperty(Constants.KEY_COMPRAS_URL_VOUCHER).toString());
            compra.put(Constants.KEY_COMPRAS_URL_DETALLE,soapCompra.getProperty(Constants.KEY_COMPRAS_URL_DETALLE).toString());
            compra.put(Constants.KEY_TRANS_ID,soapCompra.getProperty(Constants.KEY_TRANS_ID).toString());



            comprasItems.add(compra);
        }

        return comprasItems;
    }


//    public static ArrayList<TvItem> toTvItems(SoapObject soapObject) {
//        ArrayList<TvItem> tvItems = new ArrayList<>();
//        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
//            Log.d(TAG, soapObject.getProperty(i).toString());
//            SoapObject video = (SoapObject) soapObject.getProperty(i);
//            TvItem tvItem = new TvItem(Integer.valueOf(video.getProperty(Constants.KEY_VIDEOS_ID_VIDEO).toString()),
//                    video.getProperty(Constants.KEY_VIDEOS_TITULO).toString(),
//                    video.getProperty(Constants.KEY_VIDEOS_DESC).toString(),
//                    video.getProperty(Constants.KEY_VIDEOS_IMAGEN).toString(),
//                    video.getProperty(Constants.KEY_VIDEOS_URL).toString());
//            tvItems.add(tvItem);
//        }
//        return tvItems;
//    }
//

//
//    public static SolicitudCobro toSolicitudCobro(Context ctx, SoapObject soapObject){
//        SoapObject soapCobro = soapObject;
//        //SoapObject soapCobro = (SoapObject) soapObject.getProperty(0);
//        return new SolicitudCobro(soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_MONEDA,""),
//        soapCobro.getPropertySafelyAsString(Constants.KEY_COBRO_NIT,""),
//        soapCobro.getPropertySafelyAsString(Constants.KEY_COMERCIO_RSOCIAL,""),
//        soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_REF,""),
//        Float.valueOf(soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_VALOR, "0")),
//        Float.valueOf(soapCobro.getPropertySafelyAsString(Constants.KEY_COMPRAS_IVA, "0")),
//        Integer.valueOf(soapCobro.getPropertySafelyAsString(Constants.KEY_COMERCIO_ID, "0")),
//        Integer.valueOf(soapCobro.getPropertySafelyAsString(Constants.KEY_ID_ACCOUNT, "0")));
//    }
//
//    public static ArrayList<Tarjeta> toTarjetas(Context ctx,SoapObject soapObject) {
//        final float cr = 1.581f;
//        ArrayList<Tarjeta> tarjetas = new ArrayList<>();
//        for (int i = 0; i < soapObject.getPropertyCount(); i++) {
//            Log.d(TAG, soapObject.getProperty(i).toString());
//            SoapObject tarjetaC = (SoapObject) soapObject.getProperty(i);
//            String banco="",marca="";
//            /*if (tarjetaC.getProperty(Constants.KEY_CCARD_BANCO).toString().equals("anyType{}")){
//                banco="Banco Desconocido";
//            }else{
//                banco=tarjetaC.getProperty(Constants.KEY_CCARD_BANCO).toString();
//            }
//            if (tarjetaC.getProperty(Constants.KEY_CCARD_MARCA).toString().equals("anyType{}")){
//                marca="";
//            }else{
//                marca=tarjetaC.getProperty(Constants.KEY_CCARD_MARCA).toString();
//            }*/
//            Tarjeta tarjeta = new Tarjeta(
//
//                    Integer.valueOf(tarjetaC.getProperty(Constants.KEY_CCARD_ID_TARJETA).toString()),
//                    Integer.valueOf(tarjetaC.getProperty(Constants.KEY_CCARD_ID_FRANQUICIA).toString()),
//                    tarjetaC.getProperty(Constants.KEY_CCARD_FRANQUICIA).toString(),
//                    tarjetaC.getProperty(Constants.KEY_CCARD_TIPO_TARJETA).toString(),
//                    tarjetaC.getProperty(Constants.KEY_CCARD_TARJETA_BIN).toString(),
//                    tarjetaC.getProperty(Constants.KEY_CCARD_TARJETA_PROD).toString(),
//                    banco,
//                    marca,
//                    Boolean.valueOf(tarjetaC.getProperty(Constants.KEY_CCARD_ESTADO).toString()),
//                    tarjetaC.getPropertySafelyAsString(Constants.KEY_CCARD_IMAGEN_TARJETA.toString()),
//                    tarjetaC.getPropertySafelyAsString(Constants.KEY_CCARD_LOGO_BANCO.toString()),
//                    tarjetaC.getPropertySafelyAsString(Constants.KEY_CCARD_LOGO_FRANQUICIA.toString()),
//                    tarjetaC.getProperty(Constants.KEY_CCARD_ID_SUSCRIP).toString());
//            tarjeta.setUp((int)(GraphicsUtils.getWindowSize(ctx).x*0.3/cr),(int)(GraphicsUtils.getWindowSize(ctx).x/cr),false);
//            tarjetas.add(tarjeta);
//        }
//        return tarjetas;
//    }
//
//
//    public static Transaccion toTransaccion(SoapObject soapObject){
//        String numAuth = soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_NUM_AUTH,"-1");
//        if (numAuth.equals("anyType{}"))numAuth="0";
//        return new Transaccion(Integer.valueOf(soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_ID, "-1")),
//                Integer.valueOf(numAuth),
//                Integer.valueOf(soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_ESTADO_ID, "-1")),
//                soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_REF,""),
//                soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_ESTADO,""),
//                soapObject.getPropertySafelyAsString(Constants.KEY_COMPRAS_MONEDA,""),
//                soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_URL_REDIR,""),
//                Float.valueOf(soapObject.getPropertySafelyAsString(Constants.KEY_TRANS_VALOR, "-1")));
//    }
//
//    public static Transaccion toTransaccionVuelos(SoapObject soapObject){
//        String numAuth = soapObject.getPropertySafelyAsString("codigoAutorizacion","-1");
//        if (numAuth.equals("anyType{}"))numAuth="0";
//        return new Transaccion(Integer.valueOf(soapObject.getPropertySafelyAsString("idTransaccion", "-1")),
//                Integer.valueOf(numAuth),
//                Integer.valueOf(soapObject.getPropertySafelyAsString("idEstado", "-1")),
//                soapObject.getPropertySafelyAsString("referencia",""),
//                soapObject.getPropertySafelyAsString("nombreEstado",""),
//                soapObject.getPropertySafelyAsString("isoMoneda",""),
//                "about:blank",
//                Float.valueOf(soapObject.getPropertySafelyAsString("valor", "-1")));
//    }
//
//    public static ArrayList<Tarjeta> toJSONTarjetas(Context ctx,String jsonCards) {
//        final float cr = 1.581f;
//        ArrayList<Tarjeta> tarjetas = new ArrayList<>();
//        try {
//            JSONObject objcard = new JSONObject(jsonCards);
//            JSONArray cards = objcard.getJSONArray("tarjetas");
//            for (int i = 0; i < cards.length(); i++) {
//                JSONObject card = cards.getJSONObject(i);
//                String banco,marca;
//                if (card.getString(Constants.KEY_CCARD_BANCO).toString().equals("anyType{}")){
//                    banco="Banco Desconocido";
//                }else{
//                    banco=card.getString(Constants.KEY_CCARD_BANCO).toString();
//                }
//                if (card.getString(Constants.KEY_CCARD_MARCA).toString().equals("anyType{}")){
//                    marca="";
//                }else{
//                    marca=card.getString(Constants.KEY_CCARD_BANCO).toString();
//                }
//                Tarjeta tarjeta = new Tarjeta(
//
//                        Integer.valueOf(card.getString(Constants.KEY_CCARD_ID_TARJETA).toString()),
//                        Integer.valueOf(card.getString(Constants.KEY_CCARD_ID_FRANQUICIA).toString()),
//                        card.getString(Constants.KEY_CCARD_FRANQUICIA).toString(),
//                        card.getString(Constants.KEY_CCARD_TIPO_TARJETA).toString(),
//                        card.getString(Constants.KEY_CCARD_TARJETA_BIN).toString(),
//                        card.getString(Constants.KEY_CCARD_TARJETA_PROD).toString(),
//                        banco,
//                        marca,
//                        Boolean.valueOf(card.getString(Constants.KEY_CCARD_ESTADO).toString()),
//                        card.getString(Constants.KEY_CCARD_IMAGEN_TARJETA),
//                        card.getString(Constants.KEY_CCARD_LOGO_BANCO),
//                        card.getString(Constants.KEY_CCARD_LOGO_FRANQUICIA),
//                        card.getString(Constants.KEY_CCARD_ID_SUSCRIP));
//                tarjeta.setUp((int)(GraphicsUtils.getWindowSize(ctx).x*0.3/cr),(int)(GraphicsUtils.getWindowSize(ctx).x/cr),false);
//                tarjetas.add(tarjeta);
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        return tarjetas;
//    }

}
