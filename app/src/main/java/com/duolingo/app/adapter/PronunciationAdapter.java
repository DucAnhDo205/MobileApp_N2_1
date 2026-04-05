package com.duolingo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.models.PronunciationLesson;
import java.util.List;

public class PronunciationAdapter extends RecyclerView.Adapter<PronunciationAdapter.LessonViewHolder> {

    private List<PronunciationLesson> lessonList;
    private OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonClick(PronunciationLesson lesson);
    }

    public PronunciationAdapter(List<PronunciationLesson> lessonList, OnLessonClickListener listener) {
        this.lessonList = lessonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pronunciation_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        PronunciationLesson lesson = lessonList.get(position);
        holder.txtTitle.setText(lesson.getTitle());
        holder.txtDifficulty.setText("Mức độ: " + lesson.getDifficulty());
        holder.itemView.setOnClickListener(v -> listener.onLessonClick(lesson));
    }

    @Override
    public int getItemCount() { return lessonList.size(); }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDifficulty;
        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtLessonTitle);
            txtDifficulty = itemView.findViewById(R.id.txtLessonDifficulty);
        }
    }
}