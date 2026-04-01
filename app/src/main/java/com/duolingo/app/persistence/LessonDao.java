package com.duolingo.app.persistence;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.duolingo.app.models.Lesson;

import java.util.List;

@Dao
public interface LessonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Lesson lesson);

    @Update
    void update(Lesson lesson);

    @Query("SELECT * FROM lesson_table WHERE language = :language ORDER BY lessonNumber ASC")
    LiveData<List<Lesson>> getLessonsByLanguage(String language);

    @Query("SELECT COUNT(*) FROM lesson_table")
    int countLessons();
}
