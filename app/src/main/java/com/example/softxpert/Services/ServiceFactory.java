package com.example.softxpert.Services;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ServiceFactory {

    public static void getData(final Context context, String url, final RequestListener listener) {

        final OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                listener.onFailure(1000);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (response == null || response.code() != 200) {
                    if (response == null)
                        listener.onFailure(1);
                    else
                        listener.onFailure(response.code());
                } else {

                    String body = Objects.requireNonNull(response.body()).string();
                    call.cancel();
                    listener.onSuccess(body);

                }
            }
        });
    }


}
