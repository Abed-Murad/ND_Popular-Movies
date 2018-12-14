package com.am.popularmoviesstageone.network;


import com.am.popularmoviesstageone.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.am.popularmoviesstageone.util.CONST.BASE_IMDB_URL;

public class APIClient {

    private static Retrofit retrofit = null;
    static String API_KEY = BuildConfig.API_KEY;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // For Adding The API Key to Each Request Ever Made
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                Request request = requestBuilder.build();
                return chain.proceed(request);
            });

            // For Date Strings Format to Be Converted To Date Objects
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                    .create();

            // The Retrofit Object
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_IMDB_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ApiRequests getApiService() {
        return APIClient.getClient().create(ApiRequests.class);
    }
}
