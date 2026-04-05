package com.duolingo.app.models;

public class PronunciationLesson {
    private String id;
    private String title;
    private String difficulty;
    private int iconResId;

    public PronunciationLesson(String id, String title, String difficulty, int iconResId) {
        this.id = id;
        this.title = title;
        this.difficulty = difficulty;
        this.iconResId = iconResId;
    }

    // Getter cho các biến trên (chuột phải chọn Generate -> Getter)
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDifficulty() { return difficulty; }
    public int getIconResId() { return iconResId; }
}