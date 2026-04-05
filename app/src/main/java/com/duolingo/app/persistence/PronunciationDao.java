package com.duolingo.app.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.duolingo.app.models.PronunciationQuestion;
import java.util.List;

@Dao
public interface PronunciationDao {

    // Lấy đúng 5 câu hỏi của bài học mà người dùng vừa click vào
    @Query("SELECT * FROM pronunciation_questions WHERE lesson_id = :lessonId LIMIT 5")
    List<PronunciationQuestion> getQuestionsByLessonId(String lessonId);

    // Dùng để nạp dữ liệu ban đầu từ file CSV (giống cách em làm với Grammar)
    @Insert
    void insertAll(List<PronunciationQuestion> questions);

    // Đếm xem có data chưa để tránh nạp trùng
    @Query("SELECT COUNT(id) FROM pronunciation_questions")
    int getCount();
}
