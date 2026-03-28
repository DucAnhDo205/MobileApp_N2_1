package com.duolingo.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "lesson_table")
public class Lesson {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int lessonNumber;
    private String title;
    private String language; // "English", "Japanese", "Korean"
    private boolean isPassed;

    public Lesson(int lessonNumber, String title, String language) {
        this.lessonNumber = lessonNumber;
        this.title = title;
        this.language = language;
        this.isPassed = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getLessonNumber() { return lessonNumber; }
    public void setLessonNumber(int lessonNumber) { this.lessonNumber = lessonNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public boolean isPassed() { return isPassed; }
    public void setPassed(boolean passed) { isPassed = passed; }
}
