package com.duolingo.app.models;

public class StudyModule {
    private String id;
    private int iconResId;
    private String title;
    private String description;
    private int colorRes;   // Lưu ID của màu chính
    private int bgColorRes; // Lưu ID của màu nền

    public StudyModule(String id, int iconResId, String title, String description, int colorRes, int bgColorRes) {
        this.id = id;
        this.iconResId = iconResId;
        this.title = title;
        this.description = description;
        this.colorRes = colorRes;
        this.bgColorRes = bgColorRes;
    }

    public String getId() { return id; }
    public int getIconResId() { return iconResId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getColorRes() { return colorRes; }
    public int getBgColorRes() { return bgColorRes; }
}
