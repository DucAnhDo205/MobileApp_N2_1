package com.duolingo.app.models;

public class LetterItem {
    private String character;
    private boolean isSelected;

    public LetterItem(String character) {
        this.character = character;
        this.isSelected = false;
    }

    public String getCharacter() { return character; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}