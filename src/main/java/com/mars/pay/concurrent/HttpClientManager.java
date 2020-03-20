package com.mars.pay.concurrent;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.SystemUtils.*;

/**
 * @author hufeng
 * @version HttpClientManager.java, v 0.1 2020/3/20 11:36 PM Exp $
 */

public class HttpClientManager {
    private static final Logger            LOGGER   = LoggerFactory.getLogger(HttpClientManager.class.getName());
    private static final String            OS       = OS_NAME + FILE_SEPARATOR + OS_VERSION;
    private static final String            VERSION  = JAVA_VERSION;
    private static final String            FORMAT   = "WeChatPay-Apache-HttpClient/%s (%s) Java/%s";
    /**
     * instance
     */
    private static       HttpClientManager INSTANCE = new HttpClientManager();
    /**
     * sync lock
     */
    private final        Object            lock     = new Object();

    private HttpClientManager() {
    }

    public static HttpClientManager getInstance() {
        return INSTANCE;
    }

    public CloseableHttpClient getHttpClient() {
        try {
            return initializeHttpClient();
        } catch (Exception e) {
            throw new RuntimeException("初始化HttpClient发生异常", e);
        }
    }

    /**
     * 初始化HttpClient
     *
     * @return httpClient
     * @throws Exception e
     */
    private CloseableHttpClient initializeHttpClient() throws Exception {
        HttpClientBuilder httpClientBuilder;
        synchronized (lock) {
            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(
                    RegistryBuilder.<ConnectionSocketFactory>create()
                            .register("http", PlainConnectionSocketFactory.INSTANCE)
                            .register("https", buildSSLConnectionSocketFactory()).build());
            // 连接池配置
            SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(20000)
                    .setSoKeepAlive(true).setTcpNoDelay(true).build();

            // 请求配置
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(20000)
                    .setConnectTimeout(10000).setCookieSpec(CookieSpecs.DEFAULT).build();

            connectionManager.setDefaultSocketConfig(socketConfig);
            connectionManager.setMaxTotal(6000);
            connectionManager.setDefaultMaxPerRoute(6000);

            httpClientBuilder = HttpClients.custom().setConnectionManager(connectionManager)
                    .setDefaultSocketConfig(socketConfig).setDefaultRequestConfig(requestConfig)
                    .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy() {
                        @Override
                        public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                            long keepAlive = super.getKeepAliveDuration(response, context);
                            if (keepAlive == -1) {
                                keepAlive = 5000;
                            }
                            return keepAlive;
                        }
                    }).setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                    .disableCookieManagement().disableRedirectHandling()
                    .addInterceptorFirst((HttpRequestInterceptor) (httpRequest, httpContext) -> {
                        if (!httpRequest.containsHeader("Accept-Encoding")) {
                            httpRequest.addHeader("Accept-Encoding", "gzip,deflate");
                        }
                        httpRequest.addHeader("User-Agent", getUserAgent());
                    });

            closeExpiredConnection(connectionManager);
        }
        return httpClientBuilder.build();
    }

    /**
     * 自动维护线程池，剔除过期和长时间未被使用的线程
     *
     * @param manager manager
     */
    private static void closeExpiredConnection(PoolingHttpClientConnectionManager manager) {
        ScheduledThreadPoolExecutor scheduleExecutor = new ScheduledThreadPoolExecutor(1,
                Executors.defaultThreadFactory());
        scheduleExecutor.scheduleAtFixedRate(() -> {
            try {
                manager.closeExpiredConnections();
            } catch (Throwable t) {
                LOGGER.error("[httpClientManager.closedExpiredConn ERROR");
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

    private static SSLConnectionSocketFactory buildSSLConnectionSocketFactory() throws Exception {
        return new SSLConnectionSocketFactory(createIgnoreVersifySSL(), new NoopHostnameVerifier());
    }

    private static SSLContext createIgnoreVersifySSL() throws Exception {
        TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(null, new TrustManager[]{trustManager}, null);
        return sslContext;
    }

    private static String getUserAgent() {
        return String.format(FORMAT,
                HttpClientManager.class.getPackage().getImplementationVersion(), OS,
                VERSION == null ? "Unknown" : VERSION);
    }
}
