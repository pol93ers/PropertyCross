package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

public class RequestUpdateUser extends Request{
    @SerializedName("username") private String username;
    @SerializedName("password") private String password;
    @SerializedName("email") private String email;
    @SerializedName("name") private String name;
    @SerializedName("surname") private String surname;
    @SerializedName("url_image") private String url_image;
    @SerializedName("isNotification") private boolean isNotification;

    public RequestUpdateUser(String username, String password, String email, String name, String surname, String url_image, boolean isNotification) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.url_image = url_image;
        this.isNotification = isNotification;
    }

    @Override
    public String toString() {
        return "RequestUpdateUser{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", url_image='" + url_image + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
