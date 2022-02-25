package com.example.fmgymtrainer;

public class TrainerListName {
    private String name, username, gender, age, address, birthday, position, number, profile, email, uid;


    public TrainerListName(String name, String username, String gender, String age, String address, String birthday, String position, String number, String profile, String email, String uid) {
        this.name = name;
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.address = address;
        this.birthday = birthday;
        this.position = position;
        this.number = number;
        this.profile = profile;
        this.email = email;
        this.uid = uid;
    }

    public TrainerListName() {

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

    public String getProfile() {
        return profile;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
}
