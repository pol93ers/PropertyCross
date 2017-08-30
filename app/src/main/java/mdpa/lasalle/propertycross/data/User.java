package mdpa.lasalle.propertycross.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable{
    @SerializedName("username") private String username;
    @SerializedName("name") private String name;
    @SerializedName("surname") private String surname;
    @SerializedName("password") private String password;
    @SerializedName("email") private String email;
    @SerializedName("isNotification") private boolean isNotification;

    public User(String username, String name, String surname, String password, String email, boolean isNotification) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.password = password;
        this.email = email;
        this.isNotification = isNotification;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
