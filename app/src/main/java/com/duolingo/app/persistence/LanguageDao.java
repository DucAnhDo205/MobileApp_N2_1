package com.duolingo.app.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.duolingo.app.models.Language;
import java.util.List;

@Dao
public interface LanguageDao {
    @Insert
    void insert(Language language);

    @Query("SELECT * FROM languages")
    List<Language> getAllLanguages();

    @Query("SELECT * FROM languages WHERE name = :name LIMIT 1")
    Language getLanguageByName(String name);
}
