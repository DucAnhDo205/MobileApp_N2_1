package com.duolingo.app.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.duolingo.app.models.VocabularyItem;

import java.util.List;

public class VocabularyRepository {

    private VocabularyDao mVocabularyDao;

    public VocabularyRepository(Application application) {
        VocaVerseDatabase db = VocaVerseDatabase.getDatabase(application);
        mVocabularyDao = db.vocabularyDao();
    }

    public LiveData<List<VocabularyItem>> getVocabularyByLanguage(String language) {
        return mVocabularyDao.getVocabularyByLanguage(language);
    }

    public LiveData<List<VocabularyItem>> getRandomVocabularyForLesson(String language, int lessonNumber, int limit) {
        return mVocabularyDao.getRandomVocabularyForLesson(language, lessonNumber, limit);
    }

    public LiveData<List<VocabularyItem>> getDueVocabulary() {
        return mVocabularyDao.getDueVocabulary(System.currentTimeMillis());
    }

    public void insert(VocabularyItem item) {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            mVocabularyDao.insert(item);
        });
    }

    public void update(VocabularyItem item) {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            mVocabularyDao.update(item);
        });
    }

    public void insertAll(List<VocabularyItem> items) {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            mVocabularyDao.insertAll(items);
        });
    }
}
