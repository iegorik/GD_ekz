package com.example.gd_ekz.Grebenkin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gd_ekz.R;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FavoriteAdapter adapter;
    private DatabaseAdapter databaseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        databaseAdapter = new DatabaseAdapter(requireContext()).open();

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FavoriteAdapter();
        recyclerView.setAdapter(adapter);

        // Обработчик удаления из избранного
        adapter.setOnRemoveClickListener(item -> {
            item.setFavorite(false);
            databaseAdapter.updateItem(item);
            loadFavorites();
            Toast.makeText(requireContext(), "Удалено из избранного", Toast.LENGTH_SHORT).show();
        });

        // Обработчик нажатия на элемент
        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(requireContext(), ItemDetailActivity.class);
            intent.putExtra("item_id", item.getId());
            intent.putExtra("is_new", false);
            startActivity(intent);
        });

        return view;
    }

    private void loadFavorites() {
        List<Item> favoriteItems = databaseAdapter.getFavoriteItems();
        adapter.setItems(favoriteItems);

        if (favoriteItems.isEmpty()) {
            textViewEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textViewEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavorites();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseAdapter != null) {
            databaseAdapter.close();
        }
    }
}