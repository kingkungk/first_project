package com.kingkung.train.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TrainApiService {
    private static TrainApi service;

    private final Map<String, List<Cookie>> cookieStore = new HashMap<>();

    public TrainApiService(final Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("TrainApiService", message);
            }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(logging);

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            X509TrustManager trustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
        }

        builder.cookieJar(new CookieJar() {

            List<Cookie> emptyCookie = new ArrayList<>();

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> newCookies) {
                String host = httpUrl.host();
                if (newCookies == null || newCookies.size() < 1) {
                    return;
                }
                List<Cookie> cookies = cookieStore.get(host);
                if (cookies == null || cookies.size() == 0) {
                    List<Cookie> saveCookies = new ArrayList<>(newCookies);
                    cookieStore.put(host, saveCookies);
                    return;
                }
                for (Cookie newCookie : newCookies) {
                    String name = newCookie.name();
                    if (TextUtils.isEmpty(name)) {
                        continue;
                    }
                    Iterator<Cookie> it = cookies.iterator();
                    while (it.hasNext()) {
                        Cookie cookie = it.next();
                        if (name.equals(cookie.name())) {
                            it.remove();
                        }
                    }
                    cookies.add(newCookie);
                }
                saveCookieIfNeed(httpUrl, cookies);
            }

            public void saveCookieIfNeed(HttpUrl httpUrl, List<Cookie> saveCookies) {
                if (httpUrl.encodedPath().equals(Urls.UAMAUTH_CLIENT)) {
                    SharedPreferences sp = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    for (int i = 0; i < saveCookies.size(); i++) {
                        editor.putString("cookie" + i, saveCookies.get(i).toString());
                    }
                    editor.putInt("num", saveCookies.size());
                    editor.commit();
                }
            }

            public List<Cookie> loadCookieIfNeed(HttpUrl httpUrl) {
                if (httpUrl.encodedPath().equals(Urls.UAMTK)) {
                    List<Cookie> cookies = new ArrayList<>();
                    SharedPreferences sp = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                    int num = sp.getInt("num", 0);
                    for (int i = 0; i < num; i++) {
                        cookies.add(Cookie.parse(httpUrl, sp.getString("cookie" + i, "")));
                    }
                    return cookies;
                }
                return null;
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                if (httpUrl.encodedPath().equals(Urls.QUERY_TRAIN)) {
                    return emptyCookie;
                }
                String host = httpUrl.host();
                List<Cookie> cookies = cookieStore.get(host);
                if (cookies == null || cookies.size() == 0) {
                    cookies = loadCookieIfNeed(httpUrl);
                    if (cookies != null && cookies.size() > 0) {
                        cookieStore.put(host, cookies);
                    }
                }
                return cookies != null ? cookies : emptyCookie;
            }
        });

        OkHttpClient client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        service = retrofit.create(TrainApi.class);
    }

    public static TrainApi getTrainApi(Context context) {
        if (service == null) {
            new TrainApiService(context);
        }
        return service;
    }
}
