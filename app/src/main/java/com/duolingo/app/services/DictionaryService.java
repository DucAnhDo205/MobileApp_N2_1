package com.duolingo.app.services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryService {
    
    // API lấy thông tin từ điển miễn phí
    @GET("api/v2/entries/en/{word}")
    Call<List<DictionaryResponse>> getWordDefinition(@Path("word") String word);

    // Lớp chứa dữ liệu trả về từ JSON
    class DictionaryResponse {
        public String word;
        public String phonetic;
        public List<Phonetic> phonetics;
        public List<Meaning> meanings;

        public static class Phonetic {
            public String text;
            public String audio;
        }

        public static class Meaning {
            public List<Definition> definitions;
        }

        public static class Definition {
            public String definition;
            public String example;
        }
    }
}
