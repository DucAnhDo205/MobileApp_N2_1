package com.duolingo.app.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.duolingo.app.fragments.PronunciationQuestionFragment;
import com.duolingo.app.models.PronunciationQuestion;
import java.util.List;

public class PronunciationPagerAdapter extends FragmentStateAdapter {

    private List<PronunciationQuestion> questions;

    public PronunciationPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<PronunciationQuestion> questions) {
        super(fragmentActivity);
        this.questions = questions;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Lấy câu tiếng Anh ở vị trí tương ứng truyền vào Fragment
        return new PronunciationQuestionFragment(questions.get(position).transcript);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}