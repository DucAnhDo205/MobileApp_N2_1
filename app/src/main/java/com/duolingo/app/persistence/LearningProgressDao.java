package com.duolingo.app.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.duolingo.app.models.LearningProgress;

@Dao
public interface LearningProgressDao {
    @Insert
    void insert(LearningProgress progress);

    @Update
    void update(LearningProgress progress);

    @Query("SELECT * FROM learning_progress WHERE userId = :userId LIMIT 1")
    LearningProgress getProgressByUserId(int userId);
}
