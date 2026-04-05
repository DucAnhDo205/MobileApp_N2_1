package com.duolingo.app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pronunciation_questions")
public class PronunciationQuestion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // Khóa ngoại "mềm" để biết câu này thuộc bài học nào (ví dụ bài "1" là Chào hỏi)
    @ColumnInfo(name = "lesson_id")
    public String lessonId;

    // Câu tiếng Anh để hiển thị lên màn hình và dùng để so sánh
    @ColumnInfo(name = "transcript")
    public String transcript;

    // Constructor
    public PronunciationQuestion(String lessonId, String transcript) {
        this.lessonId = lessonId;
        this.transcript = transcript;
    }
}