package com.nichesoftware.giftlist.service;

import com.nichesoftware.giftlist.BuildConfig;

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

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            // add your other interceptors …
            // add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
        }

        return httpClient;
    }

    public static Retrofit getRetrofit() {
        return retrofitBuilder.client(getHttpClientBuilder().build()).build();
    }

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = getRetrofit();
        return retrofit.create(serviceClass);
    }
}
