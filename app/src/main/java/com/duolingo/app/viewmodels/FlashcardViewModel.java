package com.duolingo.app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.duolingo.app.models.Lesson;
import com.duolingo.app.models.VocabularyItem;
import com.duolingo.app.persistence.LessonRepository;
import com.duolingo.app.persistence.VocabularyRepository;

import java.util.List;

public class FlashcardViewModel extends AndroidViewModel {

    private VocabularyRepository mVocabularyRepository;
    private LessonRepository mLessonRepository;

    public FlashcardViewModel(Application application) {
        super(application);
        mVocabularyRepository = new VocabularyRepository(application);
        mLessonRepository = new LessonRepository(application);
    }

    public LiveData<List<VocabularyItem>> getVocabularyByLanguage(String language) {
        return mVocabularyRepository.getVocabularyByLanguage(language);
    }

    public LiveData<List<VocabularyItem>> getVocabularyByLesson(int lessonNumber, String language) {
        return mVocabularyRepository.getVocabularyByLesson(lessonNumber, language);
    }

    public LiveData<List<VocabularyItem>> getVocabularyByCategory(String category, String language) {
        return mVocabularyRepository.getVocabularyByCategory(category, language);
    }

    public LiveData<List<VocabularyItem>> getVocabularyByLessonAndCategory(int lessonNumber, String category, String language) {
        return mVocabularyRepository.getVocabularyByLessonAndCategory(lessonNumber, category, language);
    }

    public LiveData<List<String>> getCategories(String language) {
        return mVocabularyRepository.getCategories(language);
    }

    public LiveData<List<Integer>> getLessonNumbers(String language) {
        return mVocabularyRepository.getLessonNumbers(language);
    }

    public void updateVocabularyItem(VocabularyItem item) {
        mVocabularyRepository.update(item);
    }

    public LiveData<List<VocabularyItem>> getDueVocabulary() {
        return mVocabularyRepository.getDueVocabulary();
    }
}
