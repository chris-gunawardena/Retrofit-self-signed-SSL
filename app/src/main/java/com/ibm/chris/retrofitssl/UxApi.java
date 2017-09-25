package com.ibm.chris.retrofitssl;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;


public final class UxApi {

    private static UxApiService instance = null;
    private static String lastHeaderHash = null;

    public interface UxApiService {
        @GET("/v2/autocomplete")
        Call<AutocompleteHolder> getLatestAutoSuggestItems();
    }

    private static String getHeadersHash(String ... headers) {
        return TextUtils.join(",", headers);
    }

    /**
     * Singleton to manage all http requests
     * only creates a new instance if no instance exist
     * or headers have changed
     * @param ctx context
     * @return a retrofit API endpoint for UX API
     */
    public static UxApiService getInstance(Context ctx) {
        final String store = "123";
        final String token = "";
        final String assortmentId = "assid";

        // only create a new instance if no instance exist or headers have changed
        String newHeaderHash = getHeadersHash(store, token, assortmentId);
        if (instance == null || !newHeaderHash.equals(lastHeaderHash)) {
            lastHeaderHash = newHeaderHash;
            String BASE_URL = "https://mobileapi.test.jomni.nl";

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            Request request = chain.request().newBuilder()
                                    .header("Content-Type", "application/json")
                                    .header("X-jumbo-store", store==null ? "" : store)
                                    .header("X-jumbo-token", token==null ? "" : token)
                                    .header("X-jumbo-assortmentid", assortmentId==null ? "" : assortmentId)
                                    .build();
                            return chain.proceed(request);
                        }
                    })
                    .build();

            try {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        //.client(getUnsafeOkHttpClient(store, token, assortmentId))
                        .okHttpClientBuilder(ctx, store, token, assortmentId)
                        .client(okHttpClient)
                        .build();

                instance = retrofit.create(UxApiService.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return instance;
    }

    // SSL Handshake
//    private static OkHttpClient okHttpClientBuilder(Context ctx, final String store, final String token, final String assortmentId) throws Exception {
//        CertificateFactory cf = CertificateFactory.getInstance("X.509");
//        Certificate ca;
//        // Certificate should change with mode - will update.
//        try (InputStream cert = ctx.getResources().openRawResource(R.raw.debug_cert)) {
//            ca = cf.generateCertificate(cert);
//        }
//
//        // creating a KeyStore containing our trusted CAs
//        String keyStoreType = KeyStore.getDefaultType();
//        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//        keyStore.load(null, null);
//        keyStore.setCertificateEntry("ca", ca);
//
//        // creating a TrustManager that trusts the CAs in our KeyStore
//        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
//        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
//        tmf.init(keyStore);
//
//        // creating an SSLSocketFactory that uses our TrustManager
//        SSLContext sslContext = SSLContext.getInstance("TLS");
//        sslContext.init(null, tmf.getTrustManagers(), null);
//
////        okHttpClient.sslSocketFactory(sslContext.getSocketFactory());
//
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.sslSocketFactory(sslContext.getSocketFactory());
//        builder.hostnameVerifier(new HostnameVerifier() {
//            @Override
//            public boolean verify(String hostname, SSLSession session) {
//                return true;
//            }
//        });
//
//
//        return builder.addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//
//                        Request request = chain.request().newBuilder()
//                                .header("Content-Type", "application/json")
//                                .header("X-jumbo-store", store==null ? "" : store)
//                                .header("X-jumbo-token", token==null ? "" : token)
//                                .header("X-jumbo-assortmentid", assortmentId==null ? "" : assortmentId)
//                                .build();
//                        return chain.proceed(request);
//                    }
//                })
//                .build();
//    }
/*
    */

    // Safity check needed!

//    private static OkHttpClient getUnsafeOkHttpClient(final String store, final String token, final String assortmentId) {
//        try {
//            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[] {
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return new java.security.cert.X509Certificate[]{};
//                        }
//                    }
//            };
//
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("SSL");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            // Create an ssl socket factory with our all-trusting manager
//            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.sslSocketFactory(sslSocketFactory);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//
//            return builder.addInterceptor(new Interceptor() {
//                @Override
//                public Response intercept(Chain chain) throws IOException {
//
//                    Request request = chain.request().newBuilder()
//                            .header("Content-Type", "application/json")
//                            .header("X-jumbo-store", store==null ? "" : store)
//                            .header("X-jumbo-token", token==null ? "" : token)
//                            .header("X-jumbo-assortmentid", assortmentId==null ? "" : assortmentId)
//                            .build();
//                    return chain.proceed(request);
//                }
//            }).build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
