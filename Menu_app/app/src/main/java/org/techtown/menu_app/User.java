package org.techtown.menu_app;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String email;
    public String password;
    public String gender;

    public User(){

    }
    public User(String name, String email, String password, String gender){
        this.name = name;
        this.password = password;
        this.email = email;
        this.gender = gender;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) { this.password = password; }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) { this.gender = gender; }

    @Override
    public String toString() {
        return "User{" +
                "Name='" + name + '\'' +
                ", Password='" + password + '\'' +
                ", Email='" + email + '\'' +
                ", Gender='" + gender + '\'' +
                '}';
    }
}

