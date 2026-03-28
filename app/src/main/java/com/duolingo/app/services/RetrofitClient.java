package com.duolingo.app.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit dictionaryRetrofit = null;
    private static Retrofit mySqlRetrofit = null;

    // Kết nối tới API Từ điển thế giới
    public static Retrofit getDictionaryClient() {
        if (dictionaryRetrofit == null) {
            dictionaryRetrofit = new Retrofit.Builder()
                    .baseUrl("https://api.dictionaryapi.dev/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return dictionaryRetrofit;
    }

    // Kết nối tới Server MySQL của bạn (Thay IP bằng IP máy tính của bạn)
    public static Retrofit getMySqlClient() {
        if (mySqlRetrofit == null) {
            mySqlRetrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2/vocaverse/") // 10.0.2.2 là địa chỉ localhost dành cho Emulator
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mySqlRetrofit;
    }
}
