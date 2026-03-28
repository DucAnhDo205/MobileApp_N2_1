package com.duolingo.app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.duolingo.app.models.Lesson;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.CSVHelper;
import com.duolingo.app.persistence.LessonRepository;
import com.duolingo.app.persistence.VocabularyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlashcardViewModel extends AndroidViewModel {

    private VocabularyRepository mVocabularyRepository;
    private LessonRepository mLessonRepository;
    private LiveData<List<VocabularyItem>> mAllVocabulary;

    public LiveData<List<VocabularyItem>> getAllVocabulary() {
        return mAllVocabulary;
    }

    public FlashcardViewModel(Application application) {
        super(application);
        mVocabularyRepository = new VocabularyRepository(application);
        mLessonRepository = new LessonRepository(application);
        
        // Mặc định ban đầu lấy tiếng Anh
        mAllVocabulary = mVocabularyRepository.getVocabularyByLanguage("English");

        // Khởi tạo dữ liệu nếu database trống
        mAllVocabulary.observeForever(list -> {
            if (list == null || list.isEmpty()) {
                populateInitialData();
            }
        });
    }

    /**
     * Lấy ngẫu nhiên từ 10 đến 30 từ từ một bài học cụ thể để học.
     * Người dùng cần học hết số từ này để vượt qua bài học (pass).
     */
    public LiveData<List<VocabularyItem>> getRandomVocabularyForLesson(String language, int lessonNumber) {
        // Tạo limit ngẫu nhiên từ 10 đến 30 như yêu cầu
        int limit = new Random().nextInt(21) + 10; 
        return mVocabularyRepository.getRandomVocabularyForLesson(language, lessonNumber, limit);
    }

    public LiveData<List<Lesson>> getLessonsByLanguage(String language) {
        return mLessonRepository.getLessonsByLanguage(language);
    }

    public void updateVocabularyItem(VocabularyItem item) {
        mVocabularyRepository.update(item);
    }

    public void markLessonAsPassed(Lesson lesson) {
        lesson.setPassed(true);
        mLessonRepository.update(lesson);
    }

    private void populateInitialData() {
        // 1. Thêm 10 bài học cho mỗi ngôn ngữ
        String[] languages = {"English", "Japanese", "Korean"};
        for (String lang : languages) {
            for (int i = 1; i <= 10; i++) {
                mLessonRepository.insert(new Lesson(i, "Bài học " + i + " (" + lang + ")", lang));
                
                // 2. Thêm từ vựng mẫu (Giả lập 150 từ cho mỗi bài học)
                // Trong thực tế, bạn có thể đọc từ 3 file CSV khác nhau: english.csv, japanese.csv, korean.csv
                if (lang.equals("English")) {
                    // Ví dụ lấy từ file CSV có sẵn
                    List<VocabularyItem> englishWords = CSVHelper.readVocabularyFromCSV(getApplication(), "english_lessons.csv", "English", i);
                    if (!englishWords.isEmpty()) mVocabularyRepository.insertAll(englishWords);
                } else {
                    // Tạo dữ liệu mẫu cho Japanese và Korean nếu chưa có file CSV
                    generateDummyData(lang, i, 150);
                }
            }
        }
    }

    private void generateDummyData(String language, int lessonNumber, int count) {
        List<VocabularyItem> dummyList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String word = language + " Word " + i + " L" + lessonNumber;
            String meaning = "Nghĩa của từ " + i;
            String example = "Ví dụ cho từ " + i;
            dummyList.add(new VocabularyItem(word, meaning, example, language, "General", lessonNumber));
        }
        mVocabularyRepository.insertAll(dummyList);
    }
}
