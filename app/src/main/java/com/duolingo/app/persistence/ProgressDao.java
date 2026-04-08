package com.duolingo.app.persistence;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.duolingo.app.models.GameHistory;
import java.util.List;

@Dao
public interface ProgressDao {
    @Insert
    void insertGameHistory(GameHistory history);

    // Lấy tổng số sao của người dùng để hiển thị trên Dashboard [cite: 12, 23]
    @Query("SELECT SUM(Score) FROM GameHistory WHERE UserID = :userId")
    int getTotalStars(int userId);

    // Lấy tất cả lịch sử chơi để tính toán chuỗi ngày (Streak) [cite: 11, 23]
    @Query("SELECT * FROM GameHistory WHERE UserID = :userId ORDER BY PlayedAt DESC")
    List<GameHistory> getAllHistory(int userId);
}