package com.duolingo.app.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.duolingo.app.models.VocabularyItem;

import java.util.List;

@Dao
public interface VocabularyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(VocabularyItem item);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<VocabularyItem> items);

    @Update
    void update(VocabularyItem item);

    @Query("SELECT * FROM vocabulary_table WHERE language = :language ORDER BY nextReviewDate ASC")
    LiveData<List<VocabularyItem>> getVocabularyByLanguage(String language);

    // Lấy danh sách các chủ đề (Category) có trong DB
    @Query("SELECT DISTINCT category FROM vocabulary_table WHERE language = :language")
    LiveData<List<String>> getCategories(String language);

    // Lấy từ vựng theo chủ đề để làm bài tập/kiểm tra
    @Query("SELECT * FROM vocabulary_table WHERE category = :category AND language = :language")
    List<VocabularyItem> getVocabularyByCategory(String category, String language);

    // Lấy ngẫu nhiên các từ để học theo bài học
    @Query("SELECT * FROM vocabulary_table WHERE language = :language AND lessonNumber = :lessonNumber ORDER BY RANDOM() LIMIT :limit")
    LiveData<List<VocabularyItem>> getRandomVocabularyForLesson(String language, int lessonNumber, int limit);

    // Lấy ngẫu nhiên 10 từ để làm bài kiểm tra nhanh
    @Query("SELECT * FROM vocabulary_table WHERE language = :language ORDER BY RANDOM() LIMIT 10")
    List<VocabularyItem> getRandomVocabularyForQuiz(String language);

    @Query("SELECT * FROM vocabulary_table WHERE nextReviewDate <= :currentDate ORDER BY nextReviewDate ASC")
    LiveData<List<VocabularyItem>> getDueVocabulary(long currentDate);

    @Query("SELECT * FROM vocabulary_table ORDER BY RANDOM() LIMIT :limit")
    List<VocabularyItem> getRandomVocabulary(int limit);

    @Query("SELECT * FROM vocabulary_table WHERE lessonNumber = :lessonNum ORDER BY RANDOM() LIMIT 1")
    VocabularyItem getRandomVocabFromLesson(int lessonNum);
}
