package com.duolingo.app.models;

public class GrammarLesson {
    private String categoryId; // Khóa chính để chọc xuống Database (VD: "Past Simple")
    private String title;      // Tên hiển thị to đùng (VD: "Thì Quá khứ đơn")
    private String description; // Tên tiếng Anh ở dưới (VD: "Past Simple")

    // Constructor để nạp dữ liệu
    public GrammarLesson(String categoryId, String title, String description) {
        this.categoryId = categoryId;
        this.title = title;
        this.description = description;
    }

    // Getters để Adapter lấy dữ liệu ra dùng
    public String getCategoryId() { return categoryId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}