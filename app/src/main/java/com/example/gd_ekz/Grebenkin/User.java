package com.example.gd_ekz.Grebenkin;

public class User {
    private long id;
    private String email;
    private String password; // В реальном приложении пароль нужно хэшировать!

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    // Геттеры и сеттеры (в Android Studio можно сгенерировать: ПКМ -> Generate -> Getter and Setter)
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
