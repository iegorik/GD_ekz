package com.example.gd_ekz.Grebenkin;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;

public class DatabaseAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context) {
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open() {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // Метод для добавления нового пользователя
    public long insertUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.COLUMN_PASSWORD, user.getPassword());
        // insert возвращает id новой строки или -1 при ошибке
        return database.insert(DatabaseHelper.TABLE_USERS, null, cv);
    }

    // Метод для поиска пользователя по email и паролю (для входа)

    // Метод для поиска пользователя по email и паролю (для входа)
    public User getUser(String email, String password) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS, // таблица
                null, // все колонки
                DatabaseHelper.COLUMN_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?", // условие WHERE
                new String[]{email, password}, // аргументы для WHERE
                null, null, null);

        User user = null;
        if (cursor.moveToFirst()) {
            // ИСПРАВЛЕНО: COLUMN_USER_ID вместо COLUMN_ID
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PASSWORD));
            user = new User(id, userEmail, userPassword);
        }
        cursor.close();
        return user;
    }
    // Проверка, существует ли уже пользователь с таким email
    // Проверка, существует ли уже пользователь с таким email
    public boolean isEmailExists(String email) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_USERS,
                new String[]{DatabaseHelper.COLUMN_USER_ID}, // ИСПРАВЛЕНО: COLUMN_USER_ID
                DatabaseHelper.COLUMN_EMAIL + " = ?",
                new String[]{email},
                null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    // Получить все предметы
    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ITEMS,
                null,
                null, null, null, null,
                DatabaseHelper.COLUMN_ITEM_CREATED_AT + " DESC"); // Сортировка: новые сверху

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_DESCRIPTION));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_PRICE));
            long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_CREATED_AT));
            boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_IS_FAVORITE)) == 1;

            items.add(new Item(id, name, description, price, createdAt, isFavorite));
        }
        cursor.close();
        return items;
    }

    // Получить только избранные предметы
    public List<Item> getFavoriteItems() {
        List<Item> items = new ArrayList<>();
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ITEMS,
                null,
                DatabaseHelper.COLUMN_ITEM_IS_FAVORITE + " = ?",
                new String[]{"1"},
                null, null,
                DatabaseHelper.COLUMN_ITEM_CREATED_AT + " DESC");

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_DESCRIPTION));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_PRICE));
            long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_CREATED_AT));

            items.add(new Item(id, name, description, price, createdAt, true));
        }
        cursor.close();
        return items;
    }

    // Добавить новый предмет
    public long insertItem(Item item) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ITEM_NAME, item.getName());
        cv.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, item.getDescription());
        cv.put(DatabaseHelper.COLUMN_ITEM_PRICE, item.getPrice());
        cv.put(DatabaseHelper.COLUMN_ITEM_CREATED_AT, item.getCreatedAt());
        cv.put(DatabaseHelper.COLUMN_ITEM_IS_FAVORITE, item.isFavorite() ? 1 : 0);
        return database.insert(DatabaseHelper.TABLE_ITEMS, null, cv);
    }

    // Обновить предмет
    public long updateItem(Item item) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COLUMN_ITEM_NAME, item.getName());
        cv.put(DatabaseHelper.COLUMN_ITEM_DESCRIPTION, item.getDescription());
        cv.put(DatabaseHelper.COLUMN_ITEM_PRICE, item.getPrice());
        cv.put(DatabaseHelper.COLUMN_ITEM_IS_FAVORITE, item.isFavorite() ? 1 : 0);
        return database.update(
                DatabaseHelper.TABLE_ITEMS,
                cv,
                DatabaseHelper.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    // Удалить предмет
    public long deleteItem(long id) {
        return database.delete(
                DatabaseHelper.TABLE_ITEMS,
                DatabaseHelper.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // Получить предмет по ID
    public Item getItem(long id) {
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_ITEMS,
                null,
                DatabaseHelper.COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        Item item = null;
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_DESCRIPTION));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_PRICE));
            long createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_CREATED_AT));
            boolean isFavorite = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ITEM_IS_FAVORITE)) == 1;
            item = new Item(id, name, description, price, createdAt, isFavorite);
        }
        cursor.close();
        return item;
    }
}
