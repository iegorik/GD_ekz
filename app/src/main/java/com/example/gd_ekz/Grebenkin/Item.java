package com.example.gd_ekz.Grebenkin;

public class Item {
    private long id;
    private String name;        // Название предмета
    private String description; // Краткий текст
    private double price;       // Цена
    private long createdAt;     // Дата создания (в миллисекундах System.currentTimeMillis())
    private boolean isFavorite; // Избранное или нет

    // Конструктор для создания нового предмета
    public Item(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = System.currentTimeMillis();
        this.isFavorite = false;
    }

    // Конструктор для загрузки из БД
    public Item(long id, String name, String description, double price, long createdAt, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
        this.isFavorite = isFavorite;
    }

    // Геттеры и сеттеры (сгенерируй через ПКМ -> Generate -> Getter and Setter)
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
