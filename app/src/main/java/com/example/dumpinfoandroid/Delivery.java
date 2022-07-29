package com.example.dumpinfoandroid;

import androidx.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Delivery {
    private final ServerCallback serverCallback;

    Delivery(ServerCallback serverCallback) {
        this.serverCallback = serverCallback;
    }

    public void setLoad(List<Content> contentList, List<String> clientId, String packagesName) {
        for (int i = 0; i < contentList.size(); i++) {
            Server.apiServer.post(new Post(Collections.singletonList(contentList.get(i)), clientId, packagesName)).enqueue(new Callback<Post>() {
                @Override
                public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                    if (response.body() != null) {
                        serverCallback.onServerResponse(response.body().toString());
                    } else {
                        serverCallback.onServerResponse("");
                    }
                }
                @Override
                public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                    serverCallback.onServerResponse("");
                }
            });
        }
    }
}
