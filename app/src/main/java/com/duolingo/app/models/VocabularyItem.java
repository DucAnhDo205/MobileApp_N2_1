package com.duolingo.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "vocabulary_table")
public class VocabularyItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String word;
    private String meaning;
    private String example;
    private String language; // "English", "Japanese", "Korean"
    private String category;
    private int lessonNumber; // Thêm cột lessonNumber

    // Các trường cho thuật toán lặp lại ngắt quãng
    private long nextReviewDate;
    private int interval;
    private double easeFactor;

    public VocabularyItem(String word, String meaning, String example, String language, String category, int lessonNumber) {
        this.word = word;
        this.meaning = meaning;
        this.example = example;
        this.language = language;
        this.category = category;
        this.lessonNumber = lessonNumber;
        this.nextReviewDate = System.currentTimeMillis();
        this.interval = 1;
        this.easeFactor = 2.5;
    }

    // Getters và Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getMeaning() { return meaning; }
    public void setMeaning(String meaning) { this.meaning = meaning; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getLessonNumber() { return lessonNumber; }
    public void setLessonNumber(int lessonNumber) { this.lessonNumber = lessonNumber; }

    public long getNextReviewDate() { return nextReviewDate; }
    public void setNextReviewDate(long nextReviewDate) { this.nextReviewDate = nextReviewDate; }

    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }

    public double getEaseFactor() { return easeFactor; }
    public void setEaseFactor(double easeFactor) { this.easeFactor = easeFactor; }
}
