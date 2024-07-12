package com.weshopify.platform.configuration;

import javax.net.ssl.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

import java.security.cert.X509Certificate;
import java.io.IOException;
import java.net.HttpURLConnection;


@Configuration
public class SSLConfig {

    @PostConstruct
    public void disableSslVerification() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            // If you are using RestTemplate, configure it to use the custom SSLContext
            RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory() {
                @Override
                protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
                    if (connection instanceof HttpsURLConnection) {
                        ((HttpsURLConnection) connection).setSSLSocketFactory(sc.getSocketFactory());
                        ((HttpsURLConnection) connection).setHostnameVerifier((hostname, session) -> true);
                    }
                    super.prepareConnection(connection, httpMethod);
                }
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to configure SSL", e);
        }
    }
}
