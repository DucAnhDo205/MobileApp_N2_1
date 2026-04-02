package com.duolingo.app.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.duolingo.app.models.GrammarQuestion;

import java.util.List;

@Dao
public interface GrammarDao {

    // --- CÁC HÀM MAIN ACTIVITY ĐANG CẦN ---

    // 1. Hàm kiểm tra xem kho có rỗng không
    @Query("SELECT * FROM grammar_questions")
    List<GrammarQuestion> getAllQuestions();

    // 2. Hàm nhét từng câu hỏi vào Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuestion(GrammarQuestion question);


    // --- CÁC HÀM CŨ CỦA EM (Giữ lại dùng cho sau này) ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GrammarQuestion> questions);

    @Query("SELECT * FROM grammar_questions WHERE category = :category")
    List<GrammarQuestion> getQuestionsByCategory(String category);

    // Đếm xem trong bảng có dữ liệu chưa để khỏi phải nạp lại CSV nhiều lần
    @Query("SELECT COUNT(*) FROM grammar_questions")
    int getCount();

    // Lệnh SQL gom nhóm: Tự động nhả ra danh sách các Category (không bị trùng lặp)
    @Query("SELECT * FROM grammar_questions GROUP BY category")
    List<GrammarQuestion> getAllCategories();
}