package com.example.gd_ekz.Grebenkin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gd_ekz.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Item> items = new ArrayList<>();
    private OnItemClickListener itemClickListener;
    private OnRemoveClickListener removeClickListener;

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.removeClickListener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_layout, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Item item = items.get(position);
        holder.textName.setText(item.getName());
        holder.textDescription.setText(item.getDescription());
        holder.textPrice.setText(String.format(Locale.getDefault(), "%.2f ₽", item.getPrice()));

        // Форматируем дату
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        String dateStr = sdf.format(new Date(item.getCreatedAt()));
        holder.textDate.setText("Добавлено: " + dateStr);

        // Нажатие на элемент
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(item);
            }
        });

        // Нажатие на кнопку удаления из избранного
        holder.buttonRemove.setOnClickListener(v -> {
            if (removeClickListener != null) {
                removeClickListener.onRemoveClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView textName, textDescription, textPrice, textDate;
        ImageButton buttonRemove;

        FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textItemName);
            textDescription = itemView.findViewById(R.id.textItemDescription);
            textPrice = itemView.findViewById(R.id.textItemPrice);
            textDate = itemView.findViewById(R.id.textItemDate);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }
}