package com.allegra.handysdk.responsebean;

import com.allegra.handysdk.bean.CustomerData;
import com.allegra.handysdk.bean.TimeSlotBean;

import java.util.ArrayList;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public interface ServiceProviderCallInterface {
    public void ServiceProviderresponse(ArrayList<CustomerData> customerDatas);

    public void Timeslotresponse(ArrayList<TimeSlotBean> timeSlotBeans);

    public void AddAddressResponse(String resposne);
}
