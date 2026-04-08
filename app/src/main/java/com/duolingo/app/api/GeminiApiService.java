package com.duolingo.app.api;

import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {
    @POST("v1/models/gemini-2.0-flash:generateContent")
    Call<JsonObject> generateExam(
            @Query("key") String apiKey,
            @Body JsonObject body
    );
}