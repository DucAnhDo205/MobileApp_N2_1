package com.duolingo.app.persistence; // Nhớ check lại package cho khớp

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.duolingo.app.models.ListeningQuestion;

import java.util.List;

@Dao
public interface ListeningDao {

    // Lệnh lấy bài nghe theo độ khó (Dễ, Trung bình, Khó)
    @Query("SELECT * FROM listening_questions WHERE level = :level")
    List<ListeningQuestion> getQuestionsByLevel(String level);

    // Lệnh nhét dữ liệu từ file CSV vào Database
    @Insert
    void insertAll(List<ListeningQuestion> questions);

    // Thêm lệnh đếm số lượng bài nghe (để check xem đã có data chưa)
    @Query("SELECT COUNT(*) FROM listening_questions")
    int getListeningCount();
}