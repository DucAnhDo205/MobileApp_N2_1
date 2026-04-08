package com.duolingo.app.models;

import java.util.List;

public class ExamQuestion {
    private String question;
    private List<String> options;
    private int correctIndex;
    private String explanation;
    private int userSelectedAnswer = -1;

    public ExamQuestion(String question, List<String> options, int correctIndex, String explanation) {
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
        this.explanation = explanation;
    }

    public String getQuestion() { return question; }
    public List<String> getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
    public String getExplanation() { return explanation; }
    public int getUserSelectedAnswer() { return userSelectedAnswer; }
    public void setUserSelectedAnswer(int userSelectedAnswer) { this.userSelectedAnswer = userSelectedAnswer; }
}