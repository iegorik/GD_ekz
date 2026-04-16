package com.example.gd_ekz.Grebenkin;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app_database.db";
    private static final int SCHEMA = 2; // УВЕЛИЧИВАЕМ ВЕРСИЮ до 2

    // Таблица пользователей
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    // ТАБЛИЦА ПРЕДМЕТОВ (НОВАЯ)
    public static final String TABLE_ITEMS = "items";
    public static final String COLUMN_ITEM_ID = "_id";
    public static final String COLUMN_ITEM_NAME = "name";
    public static final String COLUMN_ITEM_DESCRIPTION = "description";
    public static final String COLUMN_ITEM_PRICE = "price";
    public static final String COLUMN_ITEM_CREATED_AT = "created_at";
    public static final String COLUMN_ITEM_IS_FAVORITE = "is_favorite";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Таблица пользователей
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL);";
        db.execSQL(createUsersTable);

        // Таблица предметов (НОВАЯ)
        String createItemsTable = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                COLUMN_ITEM_DESCRIPTION + " TEXT, " +
                COLUMN_ITEM_PRICE + " REAL, " +
                COLUMN_ITEM_CREATED_AT + " INTEGER, " +
                COLUMN_ITEM_IS_FAVORITE + " INTEGER DEFAULT 0);"; // 0 = false, 1 = true
        db.execSQL(createItemsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // При обновлении версии удаляем старые таблицы и создаем новые
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }
}
