package com.gigaspaces.ndemo;

import com.gigaspaces.tracing.ZipkinTracerBean;
import io.opentracing.contrib.spring.web.client.TracingRestTemplateInterceptor;
import org.openspaces.core.GigaSpace;
import org.openspaces.core.GigaSpaceConfigurer;
import org.openspaces.core.space.SpaceProxyConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;
import java.util.Collections;

@Configuration
public class KitchenConfiguration {

    @Bean
    public ZipkinTracerBean tracerBean() {
        return new ZipkinTracerBean("KitchenService");
    }

    @Bean
    public RestTemplate restTemplate() {
        setTrustAllCert();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new TracingRestTemplateInterceptor()));
        return restTemplate;
    }

    @Bean
    public GigaSpace gigaSpace() {
        return new GigaSpaceConfigurer(new SpaceProxyConfigurer("kitchen-space")).create();
    }

    private void setTrustAllCert() {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        try {
            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        } catch (Exception e) {
            throw new RuntimeException("Failed to set trustAllCert", e);
        }

    }
}
