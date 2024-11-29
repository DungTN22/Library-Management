package org.example.librarymanagement1;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId
                && Objects.equals(name, user.name)
                && Objects.equals(email, user.email)
                && Objects.equals(phone, user.phone)
                && Objects.equals(account, user.account)
                && Objects.equals(password, user.password)
                && Objects.equals(status, user.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name, email, phone, account, password, status);
    }
}
