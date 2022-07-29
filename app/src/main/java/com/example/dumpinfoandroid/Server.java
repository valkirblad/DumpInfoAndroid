package com.example.dumpinfoandroid;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Server {

    static Retrofit retrofitServer = new Retrofit.Builder()
            .baseUrl("http://192.168.1.1")  // ip сервера для отправки данных
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    static Api apiServer = retrofitServer.create(Api.class);

}
