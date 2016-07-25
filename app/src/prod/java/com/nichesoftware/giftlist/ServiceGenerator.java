package com.nichesoftware.giftlist;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Kattleya on 24/07/2016.
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = "http://192.168.1.65:8080/giftlist/";


    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());

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
    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = retrofitBuilder.client(getHttpClientBuilder().build()).build();
        return retrofit.create(serviceClass);
    }
}
