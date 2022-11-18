package com.hacktiv.ecommerce.Model;

public class Accounts {

    String name, email, password, level;

    public Accounts(){}

    public Accounts(String email, String name, String password, String level) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}