package mdpa.lasalle.propertycross.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    @SerializedName("username") private String username;
    @SerializedName("name") private String name;
    @SerializedName("surname") private String surname;
    @SerializedName("emails") private ArrayList<Email> email;
    @SerializedName("isNotification") private boolean isNotification;

    public class Email implements Serializable{
        @SerializedName("address") private String address;

        public Email(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public String toString() {
            return "Email{" +
                    "address='" + address + '\'' +
                    '}';
        }
    }

    public User(String username, String name, String surname, ArrayList<Email> email, boolean isNotification) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isNotification = isNotification;
    }

    public void setEmail(ArrayList<Email> email) {
        this.email = email;
    }

    public ArrayList<Email> getEmail() {
        return email;
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
                ", email='" + email + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
