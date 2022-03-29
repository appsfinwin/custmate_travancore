package com.finwin.travancore.traviz.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit instance = null;
    private static Retrofit retrofitIfsc = null;
    private static Retrofit retrofitTransfer = null;
    private static Retrofit retrofitContacts = null;
    private static Retrofit retrofitTest = null;

    public static Retrofit RetrofitClient() {

        if (instance == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


            final OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .build();

            instance = new Retrofit.Builder()
                    //original
                    //.baseUrl("https://custmatetravancore.digicob.in/")//travancore
                    .baseUrl("http://192.168.0.221:171/")//local
                    //.baseUrl("http://www.finwintechnologies.com:5363/")
                    //.baseUrl("http://custmatelocal.digicob.in/")
                    //.baseUrl("http://103.210.40.113/")

                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return instance;
    }

    public static Retrofit RetrofitIfsc() {

        if (retrofitIfsc == null) {

            final OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(300, TimeUnit.SECONDS)
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .build();

            retrofitIfsc = new Retrofit.Builder()
                    .baseUrl("http://www.finwintechnologies.com:82/api/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitIfsc;
    }

    public static Retrofit RetrofitTransfer() {

        if (retrofitTransfer == null) {

            final OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(100, TimeUnit.SECONDS)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .build();

            retrofitTransfer = new Retrofit.Builder()
                    .baseUrl("https://custmatetravancore.digicob.in/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitTransfer;
    }

    public static Retrofit RetrofitContacts() {

        if (retrofitContacts == null) {

            final OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(100, TimeUnit.SECONDS)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .build();

            retrofitContacts = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.221:170/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitContacts;
    }

    public static Retrofit RetrofitTest() {

        if (retrofitTest == null) {

            final OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(100, TimeUnit.SECONDS)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .build();

            retrofitTest = new Retrofit.Builder()
                    .baseUrl("http://custmatelocal.digicob.in/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitTest;
    }

}