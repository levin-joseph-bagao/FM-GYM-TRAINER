package com.example.fmgymtrainer;

public class Trainee {
    public String name, username, gender, age, address, birthday, position, number, email, profile, uid;

    public Trainee(String name, String username, String gender, String age, String address, String birthday, String position, String number, String email, String profile, String uid) {
        this.name = name;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.birthday = birthday;
        this.position = position;
        this.number = number;
        this.email = email;
        this.profile = profile;
        this.uid = uid;
    }

    public Trainee(){

    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getAddress() {
        return address;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getPosition() {
        return position;
    }

    public String getNumber() {
        return number;
    }

    public String getEmail() {
        return email;
    }

    public String getProfile() {
        return profile;
    }

    public String getUid() {
        return uid;
    }
}
