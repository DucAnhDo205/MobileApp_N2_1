package com.duolingo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.duolingo.app.R;
import com.duolingo.app.models.GrammarLesson;

import java.util.List;

public class GrammarLessonAdapter extends RecyclerView.Adapter<GrammarLessonAdapter.ViewHolder> {

    private List<GrammarLesson> lessonList;
    private OnItemClickListener listener;

    // Interface này là mấu chốt để bắt sự kiện khi em bấm vào 1 dòng
    public interface OnItemClickListener {
        void onItemClick(GrammarLesson lesson);
    }

    // Constructor
    public GrammarLessonAdapter(List<GrammarLesson> lessonList, OnItemClickListener listener) {
        this.lessonList = lessonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Gọi cái khuôn item_grammar_lesson.xml ra đây
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grammar_lesson, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrammarLesson lesson = lessonList.get(position);

        holder.tvTitle.setText(lesson.getTitle());
        holder.tvDesc.setText(lesson.getDescription());

        // CHỐT CHẶN Ở ĐÂY: Gắn sự kiện click thẳng vào cái CardView có ID đàng hoàng
        holder.cardLesson.setOnClickListener(v -> listener.onItemClick(lesson));
    }

    @Override
    public int getItemCount() {
        return lessonList.size();
    }

    // Ánh xạ View của từng dòng
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        View cardLesson; // Khai báo thêm biến này

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_lesson_title);
            tvDesc = itemView.findViewById(R.id.tv_lesson_desc);
            cardLesson = itemView.findViewById(R.id.card_lesson); // Ánh xạ nó
        }
    }
}