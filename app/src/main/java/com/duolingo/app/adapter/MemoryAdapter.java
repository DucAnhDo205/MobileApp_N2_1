package com.duolingo.app.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.duolingo.app.R;
import com.duolingo.app.models.MemoryItem;
import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.ViewHolder> {
    private final List<MemoryItem> items;
    private final OnCardClickListener listener;

    public interface OnCardClickListener {
        void onCardClick(int position);
    }

    public MemoryAdapter(List<MemoryItem> items, OnCardClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memory_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MemoryItem item = items.get(position);
        holder.tvContent.setText(item.getContent());

        if (item.isMatched()) {
            holder.itemView.setAlpha(0.3f);
            holder.itemView.setClickable(false);
            holder.viewBack.setVisibility(View.GONE);
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.cardMemory.setCardBackgroundColor(Color.parseColor("#EEEEEE"));
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.itemView.setClickable(true);
            holder.cardMemory.setCardBackgroundColor(Color.WHITE);

            if (item.isFaceUp()) {
                holder.tvContent.setVisibility(View.VISIBLE);
                holder.viewBack.setVisibility(View.GONE);
            } else {
                holder.tvContent.setVisibility(View.GONE);
                holder.viewBack.setVisibility(View.VISIBLE);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onCardClick(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardMemory;
        View viewBack;
        TextView tvContent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardMemory = itemView.findViewById(R.id.card_memory);
            viewBack = itemView.findViewById(R.id.view_back);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}