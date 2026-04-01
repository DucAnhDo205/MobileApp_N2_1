package com.duolingo.app.persistence;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.duolingo.app.models.Lesson;

import java.util.List;

public class LessonRepository {

    private LessonDao mLessonDao;

    public LessonRepository(Application application) {
        VocaVerseDatabase db = VocaVerseDatabase.getDatabase(application);
        mLessonDao = db.lessonDao();
    }

    public LiveData<List<Lesson>> getLessonsByLanguage(String language) {
        return mLessonDao.getLessonsByLanguage(language);
    }

    public void insert(Lesson lesson) {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            mLessonDao.insert(lesson);
        });
    }

    public void update(Lesson lesson) {
        VocaVerseDatabase.databaseWriteExecutor.execute(() -> {
            mLessonDao.update(lesson);
        });
    }

    public int countLessons() {
        // This is a synchronous call, maybe use an executor or return LiveData
        return mLessonDao.countLessons();
    }
}
