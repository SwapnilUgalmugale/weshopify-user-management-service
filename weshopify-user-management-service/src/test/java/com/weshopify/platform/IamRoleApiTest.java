package com.weshopify.platform;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.web.WebAppConfiguration;

import com.weshopify.platform.outbound.RoleMgmtClient;

import jakarta.annotation.PostConstruct;

@SpringBootTest
@ActiveProfiles("test")
@WebAppConfiguration
public class IamRoleApiTest extends WeshopifyUserManagementServiceApplicationTests {
	
	@Autowired
	private RoleMgmtClient roleClient;
	
	 @PostConstruct
	    public void disableSslVerification() throws Exception {
		 try {
	        TrustManager[] trustAllCerts = new TrustManager[]{
	               new X509TrustManager() {

					@Override
					public void checkClientTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {
						
					}

					@Override
					public void checkServerTrusted(X509Certificate[] chain, String authType)
							throws CertificateException {						
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
	            	   
	               }
	        };

	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
	        System.out.println("SSL verification has been disabled.");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	@Test
	public void testFindAllRoles() {
		roleClient.findAllRoles();
	}
}
