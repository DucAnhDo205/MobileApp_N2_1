package com.duolingo.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.models.LetterItem;
import java.util.List;

public class LetterAdapter extends RecyclerView.Adapter<LetterAdapter.ViewHolder> {
    private final List<LetterItem> letterList;
    private final OnLetterClickListener listener;

    public interface OnLetterClickListener {
        void onLetterClick(int position);
    }

    public LetterAdapter(List<LetterItem> letterList, OnLetterClickListener listener) {
        this.letterList = letterList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_letter_button, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LetterItem item = letterList.get(position);
        holder.tvLetter.setText(item.getCharacter());

        // Hiệu ứng: Nếu đã chọn thì mờ đi và không thể bấm
        if (item.isSelected()) {
            holder.itemView.setVisibility(View.INVISIBLE);
            holder.itemView.setClickable(false);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(true);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!item.isSelected()) {
                listener.onLetterClick(position);
            }
        });
    }

    @Override
    public int getItemCount() { return letterList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLetter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLetter = itemView.findViewById(R.id.tv_letter_item);
        }
    }
}