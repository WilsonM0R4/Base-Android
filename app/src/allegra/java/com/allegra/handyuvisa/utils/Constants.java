package com.allegra.handyuvisa.utils;

import android.content.Context;
import android.os.Bundle;

import com.allegra.handyuvisa.VisaCheckoutApp;
import com.allegra.handyuvisa.models.AllemCommerceUser;
import com.allegra.handyuvisa.models.AllemUser;
import com.allegra.handyuvisa.models.SolicitudCobro;
import com.allem.onepocket.model.OneTransaction;
import com.allem.onepocket.utils.OPKConstants;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by victor on 26/02/15.
 * com.allem.allemevent.utils
 */
public class Constants {

    //********FOR DYNAMIC PROOF OF COVERAGE**********
    public final static String ID_PORTAL = "1";
    public final static String NAMESPACE_PROOF = "http://www.ws.iatai.com/poliza_servicio/";
    public final static String METHOD_PROOF = "consultarPolizasBeneficiarioRequest";
    public final static int ACTIVITY_PROOF = 10405;
    public final static int REQUEST_CODE_PROOF = 9638;
    public static final String URL_DYNAMIC_PROOF_TEST = "http://52.207.72.167:8080/PolizaIataiServicio/poliza_servicio_binding?wsdl";//"http://52.71.117.239:8080/PolizaIataiServicio/poliza_servicio_binding?wsdl";
    public static final String URL_DYNAMIC_PROOF_PROD = "http://52.7.111.227:8080/PolizaIataiServicio/poliza_servicio_binding?wsdl";

    public static String getUrlDynamicProof(){
        if (TESTING) {
            return URL_DYNAMIC_PROOF_TEST;
        }else{
            return URL_DYNAMIC_PROOF_PROD;
        }
    }

    public final static String SPLUNK_API_KEY = "e74061f2";
    public final static int REQUEST_CODE_TRANSACTION_HISTORY = 8891;
    public final static int REQUEST_CODE_PROOF_OF_COVERAGE = 8890;
    public final static int REQUEST_CODE_MY_PROFILE = 8889;

    public final static int REQUEST_CODE_MCARD = 8888;
    public final static int REQUEST_CODE_HOTELS = 8887;
    public final static int REQUEST_CODE_SCAN_QR = 8886;
    public final static int REQUEST_CODE_CONCIERGE = 8885;
    public final static int REQUEST_CODE_SERVICES = 8884;

    //*******ADDED BY SERGIO FARFÁN FOR GET MCARD**********
    public final static String SOAP_AUTH_EMAIL_MCARD = "allegrapp@iatai.com";
    public final static String SOAP_AUTH_PASSWORD_MCARD = "7642e3e3722a158d392d1f4ed5c12ae3";
    public final static String SOAP_URL_MCARD = "http://52.20.11.221:8080/SAU/MembersiaClienteServicio?wsdl";
    public final static String SOAP_URL_MCARD_PROD = "http://52.7.111.227:8080/SAU/MembersiaClienteServicio?wsdl";
    public final static String MCARD_METHOD = "consultaMembresiaClienteRequest";
    public final static String MCARD_NAMESPACE = "http://www.ws.iatai.com/MembersiaClienteServicio/";
    public final static int MCARD_CODE = 9780970;
    public final static String TAG = "AllemConstants";
    public final static String USERNAME = "userkey";
    public final static String PASSWORD = "passkey";
    //***************** EASY CHANGE BETWEEN TESTING AND PRODUCTION SYSTEM URLS *********************

    //** Parameter indicates mode: TESTING = false implies PRODUCTION mode **
    public final static boolean TESTING = true;
    public final static boolean TESTING_AIR = true;

    public final static String DEMOURL="http://demo.allemmarket.com/allemmarketapp/index/login/";
    public final static String DEMOHOST="allemmarket.com";
    public final static String WEBAPPHOST = "ww.allemmarket.com";
    public final static String WEBAPPURL="http://www.allemmarket.com/allemmarketapp/index/login/";
    public final static int CAT_HOTEL=750;
    public final static int CAT_RESTO=749;
    public final static int CAT_EVENTO=748;
    public final static int NUMBER_PICKER_SIZE = 7;
    public final static int PICKER_SIZE_CHILDREN_AND_INFANTS = 6;

    public final static String URL_ALLEM_AIR_PROD = "https://secure.allegraplatform.com/GatewayIatai/IPPG";
    public final static String URL_ALLEM_AIR_TEST =  "https://pruebas.allegraplatform.com/GatewayIatai/IPPG";
    public final static String URL_ALLEM_WSDL_AIR_TEST = URL_ALLEM_AIR_TEST+"?WSDL";
    public final static String URL_ALLEM_WSDL_AIR_PROD = URL_ALLEM_AIR_PROD+"?WSDL";
    public static final String AUTH_KEY_AMADEUS = "wqwer23WERWER234fcbfdfgnr67sd235" ;
    public static final String NAMESPACE_AMADEUS = "http://ws.iatai.com/";
    public static final String METHOD_TRANSACCION_AEREO = "transaccionAereo";
    public static final String METHOD_TRANSACCION_AEREO_SUSC="transaccionarAereoSuscripcion";

    public static String getAirWSDL(){

        if (TESTING_AIR) {
            //Log.d(TAG, "testing");
            return URL_ALLEM_WSDL_AIR_TEST;
        }else{
            return URL_ALLEM_WSDL_AIR_PROD;
        }
    }

    public final static String NAMESPACE_ALLEM= "http://ws.alleminmotion.iatai.com/";
    public final static String URL_ALLEM_BASE_TEST = "http://52.87.70.203:8080/AllemInMotion/AllemInMotion";//"https://pruebas.allegraplatform.com/AllemInMotion/AllemInMotion";
    public final static String URL_ALLEM_BASE_PROD = "https://secureacceptance.allegraplatform.com/AllemInMotion/AllemInMotion";
    public final static String URL_ALLEM_WSDL_TEST = URL_ALLEM_BASE_TEST+"?wsdl";
    public final static String URL_ALLEM_WSDL_PROD = URL_ALLEM_BASE_PROD+"?wsdl";
    public final static String TEMPFILE_PDF="allemtemp.pdf";
    public final static String METHOD_EXCEPTION = "Exception";
    public final static String METHOD_INICIAR_SESION_COMERCIO = "IniciarSesionComercio";
    public final static String METHOD_CERRAR_SESION_COMERCIO = "CerrarSesionComercio";
    public final static String METHOD_VALIDAR_SESION_COMERCIO = "ValidarSesionComercio";
    public final static String METHOD_OBTENER_SOLICITUD_DE_PAGO = "ObtenerSolicitudDePago";
    public final static String METHOD_OBTENER_NOTIFICACIONES_COMERCIO = "ObtenerNotificacionesComercio";
    public final static String METHOD_VALIDAR_TRANSACCION = "ValidarTransaccion";
    public final static String METHOD_HACER_TRANSACCION = "HacerTransaccion";
    public final static String METHOD_OBTENER_COMPRAS = "ObtenerCompras";
    public final static String METHOD_HACER_TRANSACCION_ALLEM_MARKET = "HacerTransaccionAllemMarket";


    public final static String METHOD_INICIAR_SESION = "IniciarSesion";
    public final static String METHOD_CERRAR_SESION = "CerrarSesion";
    public final static String METHOD_VALIDAR_SESION = "ValidarSesion";
    public final static String METHOD_ENVIAR_PASSWORD = "EnviarPassword";
    public final static String METHOD_RECUPERAR_PASSWORD = "RestablecerContrasenaToken";//Restablecer
    public final static String METHOD_CREAR_CUENTA = "CrearCuenta";
    public final static String METHOD_ACTUALIZAR_CUENTA = "ActualizarCuenta";

    public final static String METHOD_AGREGAR_TARJETA_CREDITO = "AgregarTarjetaCredito";
    public final static String METHOD_OBTENER_TARJETAS_CREDITO = "ObtenerTarjetasCredito";
    public final static String METHOD_ELIMINAR_TARJETA_CREDITO = "EliminarTarjetaCredito";

    public final static String METHOD_OBTENER_VIDEOS = "ObtenerVideos";
    public final static String SOAP_AUTH_USER="tech@iatai.com";
    public final static String SOAP_AUTH_PASS="iatai2014";


    public final static String STORE_NAME="Allem Market";
    public final static String STORE_KEY_NUM_ORDEN = "numeroOrden";
    public final static String STORE_KEY_STORE_NAME="comercio";
    public final static String STORE_KEY_REFERENCE = "referencia";
    public final static String STORE_KEY_VALUE = "valor";


    //User
    public final static String KEY_ID_SESSION="idSesion";
    public final static String KEY_GREET="saludo";
    public final static String KEY_NAME="nombre";
    public final static String KEY_SURNAME="apellido";
    public final static String KEY_EMAIL="email";
    public final static String KEY_HPASS="hashpass";
    public final static String KEY_PASSWORD="password";
    public final static String KEY_ID_ACCOUNT="idCuenta";
    public final static String KEY_STATE="estado";
    public final static String KEY_PRODUCT_NUMBER = "numeroProducto";
    public final static String KEY_USER_AGENT = "userAgent";
    public final static String KEY_PUSH_CHANNEL = "pushChannel";
    public final static String KEY_MOBILE_NUMBER = "celular";
    public final static String KEY_ID_NUMBER = "idNumber";
    public final static String KEY_ID_TYPE = "idType";
    public final static String KEY_ID_COUNTRY = "pais";
    public final static String KEY_ID_MOBILE_CODE = "celular_codigo";

    //Compras
    public final static String KEY_COMPRAS="compra";
    public final static String KEY_COMPRAS_ID_COMPRAS="idCompra";
    public final static String KEY_COMPRAS_FECHA="fecha";
    public final static String KEY_COMPRAS_REF="referencia";
    public final static String KEY_COMPRAS_COMERCIO="comercio";
    public final static String KEY_COMPRAS_VALOR="valor";
    public final static String KEY_COMPRAS_MONEDA="moneda";
    public final static String KEY_COMPRAS_URL_VOUCHER="urlVoucherCompra";
    public final static String KEY_COMPRAS_URL_DETALLE="urlDetalleCompra";
    public final static String KEY_COMPRAS_NUM_ORDEN = "numeroOrden";
    public final static String KEY_COMPRAS_NUM_CUOTAS = "numeroCuotas";
    public final static String KEY_COMPRAS_IVA = "iva";
    public final static String KEY_BASE_DEVOLUCION = "baseDevolucion";
    public final static String KEY_USUARIOCUENTA = "usuarioCuenta";
    public final static String KEY_CONTRASENACUENTA = "contrasenaCuenta";

    //Videos
    public final static String KEY_VIDEOS="video";
    public final static String KEY_VIDEOS_ID_VIDEO="idVideo";
    public final static String KEY_VIDEOS_TITULO="titulo";
    public final static String KEY_VIDEOS_DESC="descripcion";
    public final static String KEY_VIDEOS_IMAGEN="imagen";
    public final static String KEY_VIDEOS_URL="url";

    //Tarjeta
    public final static String KEY_CCARD = "tarjetasDeCredito";
    public final static String KEY_CCARD_ID_TARJETA = "idTarjeta";
    public final static String KEY_CCARD_ID_FRANQUICIA = "idFranquicia";
    public final static String KEY_CCARD_ID_SEGMENTO = "segmento";
    public final static String KEY_CCARD_FRANQUICIA = "franquicia";
    public final static String KEY_CCARD_TIPO_TARJETA = "tipoTarjeta";
    public final static String KEY_CCARD_TARJETA_BIN = "tarjetaBin";
    public final static String KEY_CCARD_TARJETA_PROD = "tarjetaProducto";
    public final static String KEY_CCARD_NUM_PROD = "numeroProducto";
    public final static String KEY_CCARD_ESTADO="estado";
    public final static String KEY_CCARD_BANCO="banco";
    public final static String KEY_CCARD_MARCA="marca";
    public final static String KEY_CCARD_DEFAULT="tarjetaPorDefecto";
    public final static String KEY_CCARD_NUMERO = "numeroTarjeta";
    public final static String KEY_CCARD_MES_EXP= "mesExpiracion";
    public final static String KEY_CCARD_ANO_EXP= "anoExpiracion";
    public final static String KEY_CCARD_COD_SEG= "codigoSeguridad";
    public final static String KEY_CCARD_PAIS= "paisCorrespondencia";
    public final static String KEY_CCARD_PROV= "estadoCorrespondencia";
    public final static String KEY_CCARD_CIUDAD= "ciudadCorrespondencia";
    public final static String KEY_CCARD_DIRECCION= "direccionCorrespondencia";
    public final static String KEY_CCARD_COD_POSTAL= "codigoPostal";
    public final static String KEY_CCARD_TXTCARD = "txtTarjeta";
    public final static String KEY_CCARD_LOGO_TARJETA="logoTarjeta";
    public final static String KEY_CCARD_LOGO_BANCO="logoBanco";
    public final static String KEY_CCARD_LOGO_FRANQUICIA="logoFranquicia";
    public final static String KEY_CCARD_IMAGEN_TARJETA="imagenTarjeta";
    public final static String KEY_CCARD_ID_SUSCRIP="suscripcionId";

    //Transaccion
    public final static String KEY_TRANS_ID = "idTransaccion";
    public final static String KEY_TRANS_REF = "referencia";

    public final static String KEY_TRANS_NUM_AUTH = "numeroAutorizacion";
    public final static String KEY_TRANS_ESTADO_ID = "idEstadoTransaccion";
    public final static String KEY_TRANS_ESTADO = "estadoTransaccion";
    public final static String KEY_TRANS_MONEDA = "moneda";
    public final static String KEY_TRANS_VALOR = "valor";
    public final static String KEY_TRANS_URL_REDIR = "urlRedireccion";
    public final static String KEY_TRANS_LIST = "listRespuestaTransaccion";

    //Cobro
    public final static String KEY_COBRO_NIT = "nit";

    //Comercio
    public final static String KEY_COMERCIO_ID = "idComercio";
    public final static String KEY_COMERCIO_COD = "codigoComercio";

    public final static String KEY_COMERCIO_RSOCIAL = "razonSocial";
    public final static String KEY_COMERCIO_NOM_USU = "nombresUario";
    public final static String KEY_COMERCIO_APE_USU = "apellidoUsuario";
    public final static String KEY_COMERCIO_EMAIL_USU = "emailUsuario";

    //Vuelos
    public final static String KEY_VUELOS_RESERVA = "vueloReserva";
    public final static String KEY_VUELOS_RESERVA_INFO="infoVuelosReserva";

    //Solicitud de Pago
    public final static String KEY_ID_SOLICITUD = "idSolicitud";

    //Broadcast Filters
    public final static String BCAST_NOTIFIC_UPDATE = "com.allem.allemvent.UPDATE_NOTIFICATIONS";


    //Push channel global
    public final static String PUSH_GLOBAL = "visacheckout";

    public final static String USER_PUSH = "userPushChannel";

    //Beacons constants.
    public final static String BEACON_START = "beaconstart";
    public final static String BEACON_STOP = "beaconstop";
    public final static String ROUND_ARRIVAL = "roundarrival";


    public final static String ACTIVITY_KEY="activity";
    public final static int ACTIVITY_HOME = 10000;
    public final static int ACTIVITY_LOGIN = 10001;
    public final static int ONE_POCKET_NEEDS_LOGIN = 10050;
    public final static int ACTIVITY_PROFILE_INFORMATION = 10002;
    public final static int ACTIVITY_ALLEM_TV = 10003;
    public final static int ACTIVITY_ALLEM_WALLET = 10004;
    public final static int ACTIVITY_ALLEM_PAY = 10005;

    public final static int ACTIVITY_ALLEM_HOTEL = 10006;
    public final static int ACTIVITY_ALLEM_RESTO = 10007;
    public final static int ACTIVITY_ALLEM_EVENT = 10008;
    public final static int ACTIVITY_PROFILE_BUYOUTS = 10009;
    public final static int ACTIVITY_LOGIN_RECOVER = 10010;
    public final static int ACTIVITY_LOGIN_NEW_USER = 10011;
    public final static int ACTIVITY_UPDATE_USER = 10030;
    public final static int ACTIVITY_PROOF_OF_COVERAGE = 2222234;
    public final static int ACTIVITY_SERVICES = 10012;
    public final static int ACTIVITY_PAY_DETALLE=10013;
    public final static int ACTIVITY_PROFILE = 10014;
    public final static int ACTIVITY_ADD_CARDS = 10015;
    public final static int ACTIVITY_DO_TRANSACTION = 10016;
    public final static int ACTIVITY_NOTIFICACIONES = 10017;
    public final static int ACTIVITY_BUYOUTS_DETAIL = 10018;
    public static final int ACTIVITY_ALLEM_WALLET_DELETE = 10019;
    public static final int ACTIVITY_ADD_CARDS_BINLIST =10020 ;
    public final static int ACTIVITY_DO_TRANSACTION_AM = 10021;
    public final static int ACTIVITY_VUELOS = 10022;
    public static final int ACTIVITY_DO_TRANSACTION_VUELOS = 10023;
    public static final int ACTIVITY_NOTIFICATIONS = 10024;
    public static final int REQUEST_ONEPOCKET_RETURN = 10025;

    public final static int RESULT_LOGOUT = 20001;
    public final static int RESULT_LOGUED_IN = 20002;
    public final static int RESULT_UPDATE = 20003;
    public final static int RESULT_TO_MAIN = 20004;
    public static AllemCommerceUser user;
    public static String DEVICE_TOKEN="";
    public static int menu_selection=1;
    static File cacheDir;
    public static String DEVICE_ID="";
    public static String SENDER_ID="936885725113";

    //**********************NEW TEST URLS*************************

    public static final String URL_AUTOCOMPLETE_FLIGHTS_TEST = "http://autocompletar.allegra.travel/api/values/getinfo3?type=json&query=";
    public static final String URL_AUTOCOMPLETE_HOTELS_TEST = "http://qahoteles.allegra.travel/Api/QuickSearch/Destination/?id=";
    public static final String URL_AUTOCOMPLETE_CONCIERGE_TEST = "http://qaactividades.allegra.travel/Actividad/json_destinos2/?query=";
    public static final String URL_SEARCH_FLIGHTS_TEST = "http://qavuelos.allegra.travel/Vuelos/ResultadosGet?";
    public static final String URL_SEARCH_HOTELS_TEST =  "http://qahoteles.allegra.travel/Hotel/Flow/Availability?";
    public static final String URL_SEARCH_CONCIERGE_TEST = "http://qaactividades.allegra.travel/Actividad/ResultadosGet/?id_destino_ser=";
    public static final String URL_STORE_TEST =  "http://52.203.29.124/allemstore_demo/";
    public static final String URL_RESTAURANT_TEST =  "http://52.203.29.124/allemrestaurant_demo/#!/brand/restaurantes/map";
    public static final String URL_MARKETPLACE_TEST =  "http://dev.allegra.market/?logo=1&onepocket=1utm_source=HandyuVisa&utm_medium=botonhandy&utm_campaign=App";
    public static final String  URL_LOGIN_TEST = "http://52.87.70.203:8080/AllemInMotion/AllemInMotion?wsdl";
    public static final String  URL_SERVICE_TEST = "http://allegra.global/app/servicios_v2_demo/search/?name=";
    public static final String URL_MCARD_HTML_TEST = "http://52.207.72.167:8080/MembresiaProteccionIatai/app/index.xhtml?portal=1&email=";//"http://52.71.117.239:8080/MembresiaProteccionIatai/app/index.xhtml?portal=1&email=";

    //**********************NEW PRODUCTION URLS*************************

    public static final String URL_AUTOCOMPLETE_FLIGHTS_PROD = "http://autocompletar.allegra.travel/api/values/getinfo3?type=json&query=";
    public static final String URL_AUTOCOMPLETE_HOTELS_PROD = "http://hoteles.allegra.travel/Api/QuickSearch/Destination/?id=";
    public static final String URL_AUTOCOMPLETE_CONCIERGE_PROD = "http://actividades.allegra.travel/Actividad/json_destinos2/?query=";
    public static final String URL_SEARCH_FLIGHTS_PROD = "http://vuelos.allegra.travel/Vuelos/ResultadosGet?";
    public static final String URL_SEARCH_HOTELS_PROD = "http://hoteles.allegra.travel/Hotel/Flow/Availability?";
    public static final String URL_SEARCH_CONCIERGE_PROD = "http://actividades.allegra.travel/Actividad/ResultadosGet?id_destino_ser=";
    public static final String URL_STORE_PROD = "http://52.203.29.124/allemstore_v2/";
    public static final String URL_RESTAURANT_PROD =  "http://52.203.29.124/allemrestaurant_v2/#!/brand/restaurantes/map";
    public static final String URL_MARKETPLACE_PROD =  "https://www.allegra.market/?logo=1&onepocket=1utm_source=HandyuVisa&utm_medium=botonhandy&utm_campaign=App";
    public static final String  URL_LOGIN_PROD = "https://secureacceptance.allegraplatform.com/AllemInMotion/AllemInMotion";
    public static final String  URL_SERVICE_PROD = "http://allegra.global/app/servicios_v2/search/?name=";
    public static final String URL_MCARD_HTML_PROD = "http://52.7.111.227:8080//MembresiaProteccionIatai/app/index.xhtml?portal=1&email=";
    //**********************PROPER METHODS*************************

    public static String getWSDL(){
        if (TESTING) {
            //Log.d(TAG, "testing");
            return URL_ALLEM_WSDL_TEST;
        }else{
            return URL_ALLEM_WSDL_PROD;
        }
    }

    public static String getALLEM_BASE(){
        if (TESTING) {
            return URL_LOGIN_TEST;
        }else{
            return URL_LOGIN_PROD;
        }
    }


    public static String getAutocompleteFlightsUrl(){
        if (TESTING) {
            return URL_AUTOCOMPLETE_FLIGHTS_TEST;
        }else{
            return URL_AUTOCOMPLETE_FLIGHTS_PROD;
        }
    }
    public static String getAutocompleteHotelsUrl(){
        if (TESTING) {
            return URL_AUTOCOMPLETE_HOTELS_TEST;
        }else{
            return URL_AUTOCOMPLETE_HOTELS_PROD;
        }
    }
    public static String getAutocompleteConciergeUrl(){
        if (TESTING) {
            return URL_AUTOCOMPLETE_CONCIERGE_TEST;
        }else{
            return URL_AUTOCOMPLETE_CONCIERGE_PROD;
        }
    }

    public static String getSearchFlightsUrl(){
        if (TESTING) {
            return URL_SEARCH_FLIGHTS_TEST;
        }else{
            return URL_SEARCH_FLIGHTS_PROD;
        }
    }
    public static String getSearchHotelsUrl(){
        if (TESTING) {
            return URL_SEARCH_HOTELS_TEST;
        }else{
            return URL_SEARCH_HOTELS_PROD;
        }
    }
    public static String getSearchConciergeUrl(){
        if (TESTING) {
            return URL_SEARCH_CONCIERGE_TEST;
        }else{
            return URL_SEARCH_CONCIERGE_PROD;
        }
    }

    public static String getStoreUrl(){
        if (TESTING) {
            return URL_STORE_TEST;
        }else{
            return URL_STORE_PROD;
        }
    }

    public static String getRestaurantUrl(){
        if (TESTING) {
            return URL_RESTAURANT_TEST;
        }else{
            return URL_RESTAURANT_PROD;
        }
    }
    public static String getMarketPlaceUrl(){
        if (TESTING) {
            return URL_MARKETPLACE_TEST;
        }else{
            return URL_MARKETPLACE_PROD;
        }
    }

    public static String getMcardUrl(){
        if (TESTING) {
            return SOAP_URL_MCARD;
        }else{
            return SOAP_URL_MCARD_PROD;
        }
    }

    public static String getServiceUrl(){
        if (TESTING) {
            return URL_SERVICE_TEST;
        }else{
            return URL_SERVICE_PROD;
        }
    }

    public static String getMcardhtml (){
        if (TESTING){
            return URL_MCARD_HTML_TEST;
        }else {
            return URL_MCARD_HTML_PROD;
        }
    }


    /**
     * Guarda los datos del usuario en SharedPreferences
     * @param ctx
     * @param user
     */
    public static void saveUser(Context ctx,AllemUser user,String channel){
        KeySaver.saveShare(ctx,KEY_GREET,user.saludo);
        KeySaver.saveShare(ctx,KEY_NAME,user.nombre);
        KeySaver.saveShare(ctx,KEY_SURNAME,user.apellido);
        KeySaver.saveShare(ctx,KEY_EMAIL,user.email);
        KeySaver.saveShare(ctx,KEY_PUSH_CHANNEL,channel);
        KeySaver.saveShare(ctx,KEY_MOBILE_NUMBER,user.celular);
        KeySaver.saveShare(ctx,KEY_ID_NUMBER,user.idNumber);
        KeySaver.saveShare(ctx,KEY_ID_TYPE,user.idType);
        KeySaver.saveShare(ctx,KEY_ID_MOBILE_CODE, user.celular_codigo);
    }

    /**
     * Borra los datos del usuario de SharedPreferences
     * @param ctx
     */
    public static void deleteUser(Context ctx){
        KeySaver.removeKey(ctx, KEY_GREET);
        KeySaver.removeKey(ctx, KEY_NAME);
        KeySaver.removeKey(ctx, KEY_SURNAME);
        KeySaver.removeKey(ctx, KEY_EMAIL);
        KeySaver.removeKey(ctx, KEY_PUSH_CHANNEL);
        KeySaver.removeKey(ctx, KEY_MOBILE_NUMBER);
    }

    /**
     * Obtiene los datos del usuario guardados en SharedPreferences, si no hay datos guardados o
     * si alguno falta retorna null
     * @param ctx
     * @return AllemUser
     */
    public static AllemUser getUser(Context ctx){
        AllemUser user = null;
        String saludo,nombre,apellido,email,hashpassword,idSesion;
        int idCuenta;
        boolean estado;
        if (existsUser(ctx)){
                user = new AllemUser(KeySaver.getStringSavedShare(ctx,KEY_GREET),
                    KeySaver.getStringSavedShare(ctx,KEY_NAME),
                    KeySaver.getStringSavedShare(ctx,KEY_SURNAME),
                    KeySaver.getStringSavedShare(ctx,KEY_EMAIL),"","",0,false,KeySaver.getStringSavedShare(ctx,KEY_MOBILE_NUMBER),
                    KeySaver.getStringSavedShare(ctx,KEY_ID_NUMBER),KeySaver.getStringSavedShare(ctx,KEY_ID_TYPE),
                    KeySaver.getStringSavedShare(ctx,KEY_ID_COUNTRY) , KeySaver.getStringSavedShare(ctx,KEY_ID_MOBILE_CODE));
                    String testCode =    KeySaver.getStringSavedShare(ctx,KEY_ID_MOBILE_CODE);
            //Log.d(TAG, testCode);
            user.channel=KeySaver.getStringSavedShare(ctx,KEY_PUSH_CHANNEL);
        }
        return user;
    }

    /**
     * Verifica que todos los datos del usuario esten guardados en SharedPreferences, si es así
     * retorna true, caso contrario false.
     * @param ctx
     * @return boolean
     */
    public static boolean existsUser(Context ctx){
        return KeySaver.isExist(ctx,KEY_GREET)&&
                KeySaver.isExist(ctx,KEY_NAME)&&
                KeySaver.isExist(ctx,KEY_SURNAME)&&
                KeySaver.isExist(ctx,KEY_EMAIL)&&
                KeySaver.isExist(ctx,KEY_PUSH_CHANNEL);
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Integer> currencyScaleFactor = new HashMap<>();
    static {
        currencyScaleFactor.put("COP", 0);
        currencyScaleFactor.put("USD", 2);
    }

    public static Bundle createQRPurchaseBundle(AllemUser user, SolicitudCobro solicitud, String onePocketmessage, String purchaseType, VisaCheckoutApp app) {
        Bundle bundle = new Bundle();
        OneTransaction transaction = initOneTransaction(user, app);
        transaction.add("jsonPayment", onePocketmessage);
        transaction.add("type", purchaseType);
        transaction.add("merchantName", solicitud.usuarioCuenta);
        transaction.add("merchantPassword", solicitud.contrasenaCuenta);

        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, transaction);
        return bundle;
    }

    public static Bundle createPurchaseBundle(AllemUser user, String onePocketmessage, String purchaseType, VisaCheckoutApp app) {
        Bundle bundle = new Bundle();
        OneTransaction transaction = initOneTransaction(user, app);
        transaction.add("jsonPayment", onePocketmessage);
        transaction.add("type", purchaseType);
        bundle.putParcelable(OPKConstants.EXTRA_PAYMENT, transaction);
        return bundle;
    }

    public static Bundle createDataBundle(AllemUser user, VisaCheckoutApp app) {
        Bundle bundle = new Bundle();
        OneTransaction transaction = initOneTransaction(user, app);
        bundle.putParcelable(OPKConstants.EXTRA_DATA, transaction);
        return bundle;
    }

    private static OneTransaction initOneTransaction(AllemUser user, VisaCheckoutApp app) {
        OneTransaction transaction = new OneTransaction();
        transaction.add("sessionId", app.getIdSession());
        transaction.add("first", user.nombre);
        transaction.add("last", user.apellido);
        transaction.add("userName", user.email);
        transaction.add("rawPassword", app.getRawPassword());
        transaction.add("idCuenta", Integer.toString(app.getIdCuenta()));
        transaction.add("docType", user.idType);
        transaction.add("docId", user.idNumber);
        return transaction;

    }
}
