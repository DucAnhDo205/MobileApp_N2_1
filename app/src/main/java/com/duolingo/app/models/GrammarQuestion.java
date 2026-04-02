package com.duolingo.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "grammar_questions")
public class GrammarQuestion {

    @PrimaryKey(autoGenerate = true)
    private int id;

    // --- PHẦN LÝ THUYẾT (Header) ---
    private String category;         // Nhóm bài (VD: "Past Simple")
    private String theoryTitle;      // Tiêu đề (VD: "Thì Quá khứ đơn (Past Simple)")
    private String theoryContent;    // Nội dung (VD: "Dùng để diễn tả hành động...")
    private String theoryStructure;  // Cấu trúc (VD: "S + V2/ed + ...")
    private String theoryHint;       // Dấu hiệu (VD: "yesterday, last week...")

    // --- PHẦN CÂU HỎI & ĐÁP ÁN (Body) ---
    private String questionType;     // Loại câu hỏi (VD: "Điền từ")
    private String questionText;     // Câu hỏi (VD: "She ______ a new book last week.")

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;

    private String correctAnswer;    // Đáp án đúng (VD: "bought")

    // --- CONSTRUCTOR (Không truyền ID vì Room tự động tăng autoGenerate) ---
    public GrammarQuestion(String category, String theoryTitle, String theoryContent,
                           String theoryStructure, String theoryHint, String questionType,
                           String questionText, String optionA, String optionB,
                           String optionC, String optionD, String correctAnswer) {
        this.category = category;
        this.theoryTitle = theoryTitle;
        this.theoryContent = theoryContent;
        this.theoryStructure = theoryStructure;
        this.theoryHint = theoryHint;
        this.questionType = questionType;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }

    // --- GETTERS & SETTERS (Bắt buộc phải có để Room đọc/ghi dữ liệu) ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getTheoryTitle() { return theoryTitle; }
    public void setTheoryTitle(String theoryTitle) { this.theoryTitle = theoryTitle; }

    public String getTheoryContent() { return theoryContent; }
    public void setTheoryContent(String theoryContent) { this.theoryContent = theoryContent; }

    public String getTheoryStructure() { return theoryStructure; }
    public void setTheoryStructure(String theoryStructure) { this.theoryStructure = theoryStructure; }

    public String getTheoryHint() { return theoryHint; }
    public void setTheoryHint(String theoryHint) { this.theoryHint = theoryHint; }

    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getOptionD() { return optionD; }
    public void setOptionD(String optionD) { this.optionD = optionD; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }
}