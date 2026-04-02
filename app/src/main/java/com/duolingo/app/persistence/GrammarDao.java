package com.duolingo.app.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.duolingo.app.models.GrammarQuestion;
import java.util.List;

@Dao
public interface GrammarDao {
    @Insert
    void insertAll(List<GrammarQuestion> questions);

    @Query("SELECT * FROM grammar_questions WHERE category = :category")
    List<GrammarQuestion> getQuestionsByCategory(String category);

    // Đếm xem trong bảng có dữ liệu chưa để khỏi phải nạp lại CSV nhiều lần
    @Query("SELECT COUNT(*) FROM grammar_questions")
    int getCount();
}