package org.example.librarymanagement1;

public class User {
    private int userId;
    private String name;
    private String email;
    private String phone;
    private String account;
    private String password;
    private String status;

    // Constructor
    public User(int userId, String name, String email, String phone, String account, String password, String status) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.account = account;
        this.password = password;
        this.status = status;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getStatus() {
        return status;
    }
}
