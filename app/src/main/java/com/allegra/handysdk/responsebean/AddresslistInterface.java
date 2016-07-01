package com.allegra.handysdk.responsebean;

import com.allegra.handysdk.bean.AddressResponseBean;

import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public interface AddresslistInterface {
    public void AddressListresponse(ArrayList<AddressResponseBean> addressResponseBeans);

    public void Deleteresponse(String response);
}
