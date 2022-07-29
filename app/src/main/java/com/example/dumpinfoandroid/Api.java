package com.example.dumpinfoandroid;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {


    // отправка json
    @POST("/")
    Call<Post> post(@Body Post post);


    // отправка файлов
    @Multipart
    @POST("/")
    Call<ResponseBody> upload(@Part MultipartBody.Part file);
}
