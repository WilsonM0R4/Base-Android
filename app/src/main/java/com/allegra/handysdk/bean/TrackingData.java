package com.allegra.handysdk.bean;

/**
 * Created by sergiofarfan on 20/06/16.
 */
public class TrackingData {
    public String providerId;
    public String providerLongitude;
    public String providerLatitude;
    public String customerLatitude;
    public String customerLongitude;
    public String serviceId;
    public String serviceName;
    public String bookingId;
    public String providerName;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderLongitude() {
        return providerLongitude;
    }

    public void setProviderLongitude(String providerLongitude) {
        this.providerLongitude = providerLongitude;
    }

    public String getProviderLatitude() {
        return providerLatitude;
    }

    public void setProviderLatitude(String providerLatitude) {
        this.providerLatitude = providerLatitude;
    }

    public String getCustomerLatitude() {
        return customerLatitude;
    }

    public void setCustomerLatitude(String customerLatitude) {
        this.customerLatitude = customerLatitude;
    }

    public String getCustomerLongitude() {
        return customerLongitude;
    }

    public void setCustomerLongitude(String customerLongitude) {
        this.customerLongitude = customerLongitude;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }
}
