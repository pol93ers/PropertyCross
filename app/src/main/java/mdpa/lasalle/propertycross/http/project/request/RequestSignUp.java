package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

public class RequestSignUp extends Request{
    @SerializedName("username") private String username;
    @SerializedName("password") private String password;
    @SerializedName("email") private String email;
    @SerializedName("name") private String name;
    @SerializedName("surnames") private String surnames;
    @SerializedName("isNotification") private boolean isNotification;

    public RequestSignUp(String username, String password, String email, String name, String surnames, boolean isNotification) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.surnames = surnames;
        this.isNotification = isNotification;
    }

    @Override
    public String toString() {
        return "RequestSignUp{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", surnames='" + surnames + '\'' +
                ", isNotification=" + isNotification +
                '}';
    }
}
