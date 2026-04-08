package com.duolingo.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.models.ExamQuestion;
import java.util.List;

public class ExamNavigationAdapter extends RecyclerView.Adapter<ExamNavigationAdapter.ViewHolder> {
    private List<ExamQuestion> questions;
    private int currentIndex;
    private OnQuestionClickListener listener;
    private boolean isReviewMode = false;

    public interface OnQuestionClickListener {
        void onQuestionClick(int position);
    }

    public ExamNavigationAdapter(List<ExamQuestion> questions, int currentIndex, OnQuestionClickListener listener) {
        this.questions = questions;
        this.currentIndex = currentIndex;
        this.listener = listener;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = index;
        notifyDataSetChanged();
    }

    public void setReviewMode(boolean reviewMode) {
        this.isReviewMode = reviewMode;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_nav, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExamQuestion q = questions.get(position);
        holder.tvNum.setText(String.valueOf(position + 1));

        if (isReviewMode) {
            if (q.getUserSelectedAnswer() == q.getCorrectIndex()) {
                holder.tvNum.setBackgroundResource(R.drawable.bg_nav_correct);
                holder.tvNum.setTextColor(Color.WHITE);
            } else {
                holder.tvNum.setBackgroundResource(R.drawable.bg_nav_wrong);
                holder.tvNum.setTextColor(Color.WHITE);
            }
        } else {
            if (position == currentIndex) {
                holder.tvNum.setBackgroundResource(R.drawable.bg_nav_current);
                holder.tvNum.setTextColor(Color.WHITE);
            } else if (q.getUserSelectedAnswer() != -1) {
                holder.tvNum.setBackgroundResource(R.drawable.bg_nav_done);
                holder.tvNum.setTextColor(Color.WHITE);
            } else {
                holder.tvNum.setBackgroundResource(R.drawable.bg_nav_empty);
                holder.tvNum.setTextColor(Color.BLACK);
            }
        }

        holder.itemView.setAlpha(position == currentIndex ? 1.0f : 0.7f);
        holder.itemView.setOnClickListener(v -> listener.onQuestionClick(position));
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNum;
        ViewHolder(View v) {
            super(v);
            tvNum = v.findViewById(R.id.tv_nav_num);
        }
    }
}