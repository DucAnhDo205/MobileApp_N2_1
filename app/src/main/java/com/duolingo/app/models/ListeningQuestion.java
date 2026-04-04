package com.duolingo.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "listening_questions")
public class ListeningQuestion {
    @PrimaryKey(autoGenerate = true)
    public int id;

    // Thông tin chung của bài nghe
    public String level;
    public String audioFile;
    public String transcript;

    // --- CỤM 4 CÂU HỎI ---
    // Câu 1
    public String q1Text, q1A, q1B, q1C, q1D, q1Correct;
    // Câu 2
    public String q2Text, q2A, q2B, q2C, q2D, q2Correct;
    // Câu 3
    public String q3Text, q3A, q3B, q3C, q3D, q3Correct;
    // Câu 4
    public String q4Text, q4A, q4B, q4C, q4D, q4Correct;

    // Dùng public luôn cho đồ án để get/set code cho lẹ, tối ưu tốc độ gọi biến!
}
