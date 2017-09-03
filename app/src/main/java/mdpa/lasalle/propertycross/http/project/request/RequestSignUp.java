package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

public class RequestSignUp extends Request{
    @SerializedName("username") private String username;
    @SerializedName("password") private String password;
    @SerializedName("email") private String email;
    @SerializedName("name") private String name;
    @SerializedName("surname") private String surname;
    @SerializedName("isNotification") private boolean isNotification;

    public RequestSignUp(String username, String password, String email, String name, String surname, boolean isNotification) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.isNotification = isNotification;
    }

    @Override
    public String toString() {
        return "RequestSignUp{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
