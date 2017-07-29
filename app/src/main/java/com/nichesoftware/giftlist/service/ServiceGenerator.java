package com.nichesoftware.giftlist.service;

import com.nichesoftware.giftlist.BuildConfig;
import com.nichesoftware.giftlist.service.interceptors.TokenInterceptor;
import com.nichesoftware.giftlist.session.SessionManager;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Kattleya on 24/07/2016.
 */
public class ServiceGenerator {
//    public static final String API_BASE_URL = "http://192.168.1.65:8080/giftlist/";
    public static final String GIFT_IMAGE_URL = "http://192.168.1.65:8090/uploads/%s.jpg";
    private static final String API_BASE_URL = BuildConfig.BASE_URL;

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));

    private static OkHttpClient.Builder getHttpClientBuilder() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        TokenInterceptor tokenInterceptor = new TokenInterceptor(SessionManager.getInstance());
        httpClient.addInterceptor(tokenInterceptor);

        // Add logging as last interceptor
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // Set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return httpClient;
    }

    private static Retrofit getRetrofit() {
        return retrofitBuilder.client(getHttpClientBuilder().build()).build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = getRetrofit();
        return retrofit.create(serviceClass);
    }
}
