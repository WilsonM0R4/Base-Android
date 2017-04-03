package com.allegra.handyuvisa.models;

import java.util.HashMap;

/**
 * Created by lisachui on 10/14/15.
 */
public class TransactionDetails {


    public static final String ID_TRANSACTION_NUMBER = "idTransaccion";
    public static final String REFERENCE = "referencia";
    public static final String AUTHORIZATION_NUMBER = "codigoAutorizacion";
    public static final String DATE = "fecha";
    public static final String CURRENCY = "moneda";
    public static final String AMOUNT = "valor";
    public static final String ACCORDION_GROUP = "accordion_group";
    public static final String MERCHANT = "comercio";
    public static final String ID_COMPRA = "idCompra";
    public static final String URL_VOUCHER_COMPRA = "urlVoucherCompra";
    public static final String URL_DETALLE_COMPRA = "urlDetalleCompra";


    private HashMap<String, String> details;

    public TransactionDetails(HashMap<String, String> details) {
        this.details = details;
    }

    public String getTransactionNumber() {
        return details.get(ID_TRANSACTION_NUMBER);
    }

    public String getReference() {
        return details.get(REFERENCE);
    }

    public String getAuthorizationNumber() {
        return details.get(AUTHORIZATION_NUMBER);
    }



    public String getDate() {
        return details.get(DATE);
    }


}
