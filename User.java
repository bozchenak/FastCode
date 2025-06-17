package com.example.appcartochka;

public class User {
    String email;
    String fullName;

    String password;

    int user_id;


    byte[] image;

    public User(String email, String fullName,  String password, int user_id, byte[] image) {
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.user_id = user_id;
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }





    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }



    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
