package com.example.appcartochka;

public class item {

    int id;

    String name;

    int user_id;
    String address;
    String date;
    String time;

    public item(int id, String name, int user_id, String address, String date, String time) {
        this.id = id;
        this.name = name;
        this.user_id = user_id;
        this.address = address;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String description) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String example) {
        this.address = address;
    }

}
