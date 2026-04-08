package com.duolingo.app.models;

public class MemoryItem {
    private String content;      // Chữ hiển thị (English hoặc Vietnamese)
    private String originalWord; // Chìa khóa để so sánh cặp
    private boolean isFaceUp = false;
    private boolean isMatched = false;
    private boolean isEnglish;

    public MemoryItem(String content, String originalWord, boolean isEnglish) {
        this.content = content;
        this.originalWord = originalWord;
        this.isEnglish = isEnglish;
    }

    // Các hàm Getter/Setter (Generate tự động trong Android Studio)
    public String getContent() { return content; }
    public String getOriginalWord() { return originalWord; }
    public boolean isFaceUp() { return isFaceUp; }
    public void setFaceUp(boolean faceUp) { isFaceUp = faceUp; }
    public boolean isMatched() { return isMatched; }
    public void setMatched(boolean matched) { isMatched = matched; }
    public boolean isEnglish() { return isEnglish; }
}