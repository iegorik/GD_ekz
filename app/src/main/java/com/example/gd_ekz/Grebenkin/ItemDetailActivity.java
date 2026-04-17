package com.example.gd_ekz.Grebenkin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gd_ekz.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ItemDetailActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextPrice;
    private TextView textViewDate;
    private Button buttonSave, buttonDelete;

    private DatabaseAdapter databaseAdapter;
    private Item currentItem;
    private long itemId = -1;
    private boolean isNewItem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        databaseAdapter = new DatabaseAdapter(this).open();

        // Инициализация View
        editTextName = findViewById(R.id.editTextDetailName);
        editTextDescription = findViewById(R.id.editTextDetailDescription);
        editTextPrice = findViewById(R.id.editTextDetailPrice);
        textViewDate = findViewById(R.id.textViewDetailDate);
        buttonSave = findViewById(R.id.buttonSave);
        buttonDelete = findViewById(R.id.buttonDelete);

        // Получаем ID предмета из Intent
        itemId = getIntent().getLongExtra("item_id", -1);
        isNewItem = getIntent().getBooleanExtra("is_new", false);

        if (isNewItem) {
            // Режим создания нового предмета
            setTitle("Новый предмет");
            buttonDelete.setVisibility(View.GONE);
            textViewDate.setVisibility(View.GONE);
        } else if (itemId != -1) {
            // Режим просмотра/редактирования существующего предмета
            setTitle("Редактирование");
            loadItem();
        } else {
            Toast.makeText(this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Обработчик сохранения
        buttonSave.setOnClickListener(v -> saveItem());

        // Обработчик удаления
        if (buttonDelete.getVisibility() == View.VISIBLE) {
            buttonDelete.setOnClickListener(v -> deleteItem());
        }
    }

    private void loadItem() {
        currentItem = databaseAdapter.getItem(itemId);
        if (currentItem != null) {
            editTextName.setText(currentItem.getName());
            editTextDescription.setText(currentItem.getDescription());
            editTextPrice.setText(String.valueOf(currentItem.getPrice()));

            // Форматируем дату
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            String dateStr = sdf.format(new Date(currentItem.getCreatedAt()));
            textViewDate.setText("Дата создания: " + dateStr);
        } else {
            Toast.makeText(this, "Предмет не найден", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void saveItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();

        // Валидация
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Введите название");
            return;
        }
        if (TextUtils.isEmpty(priceStr)) {
            editTextPrice.setError("Введите цену");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            editTextPrice.setError("Некорректная цена");
            return;
        }

        long result;
        if (isNewItem) {
            // Создаем новый предмет
            Item newItem = new Item(name, description, price);
            result = databaseAdapter.insertItem(newItem);
            if (result != -1) {
                Toast.makeText(this, "Предмет добавлен", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка при добавлении", Toast.LENGTH_SHORT).show();
            }
        } else if (currentItem != null) {
            // Обновляем существующий
            currentItem.setName(name);
            currentItem.setDescription(description);
            currentItem.setPrice(price);
            result = databaseAdapter.updateItem(currentItem);
            if (result > 0) {
                Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteItem() {
        if (currentItem != null) {
            long result = databaseAdapter.deleteItem(currentItem.getId());
            if (result > 0) {
                Toast.makeText(this, "Предмет удален", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ошибка при удалении", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseAdapter != null) {
            databaseAdapter.close();
        }
    }
}