package com.duolingo.app.models;

public class MatchItem {
    private String content;      // Nội dung (Từ EN hoặc nghĩa VN)
    private String originalWord; // Từ gốc để so sánh cặp
    private boolean isEnglish;   // Phân biệt ô EN hay VN
    private boolean isSelected = false;
    private boolean isMatched = false;

    public MatchItem(String content, String originalWord, boolean isEnglish) {
        this.content = content;
        this.originalWord = originalWord;
        this.isEnglish = isEnglish;
    }

    // Getters và Setters
    public String getContent() { return content; }
    public String getOriginalWord() { return originalWord; }
    public boolean isEnglish() { return isEnglish; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
    public boolean isMatched() { return isMatched; }
    public void setMatched(boolean matched) { isMatched = matched; }
}