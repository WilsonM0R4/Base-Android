package com.allegra.handyuvisa.async;

import com.allegra.handyuvisa.utils.Constants;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

/**
 * Created by sergiofarfan on 12/14/16.
 */

public class SslConnection {
    private static final String TAG = "SSLConection";
    private static TrustManager[] trustManagers;

    private static class CustomX509TrustManager implements javax.net.ssl.X509TrustManager {
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            //Log.d(TAG, "Str: "+arg1);
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
            //Log.d(TAG, "Str: "+arg1);
        }

        public X509Certificate[] getAcceptedIssuers() {
            return (_AcceptedIssuers);
        }
    }

    static void allowSSLCertificate() {

        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //Log.d("hostName ssl:",hostname);
                //In order to fix Google Play vulnerability
                if (hostname.equals(Constants.URL_ALLEM_AIR_PROD_HOST)||
                        hostname.equals(Constants.URL_HOSTNAME_SECUREACCEPTANCE)||
                        hostname.equals(Constants.URL_HOSTNAME_ALLEGRA_PLATFORM)||
                        hostname.equals(Constants.URL_HOSTNAME_APPS_FLYER))return true;
                else return false;
            }
        });

        javax.net.ssl.SSLContext context;
        if (trustManagers == null) {
            trustManagers = new TrustManager[]{new CustomX509TrustManager()};
        }
        try {
            context = javax.net.ssl.SSLContext.getInstance("TLS");
            context.init(null, trustManagers, new SecureRandom());
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            //Log.e("NoSuchAlgorithmExce", e.toString());
        } catch (KeyManagementException e) {
            //Log.e("KeyManagementExce", e.toString());
        }
    }
}
