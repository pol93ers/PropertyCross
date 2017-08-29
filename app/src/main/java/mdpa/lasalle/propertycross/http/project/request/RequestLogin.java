package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

public class RequestLogin extends Request{
    @SerializedName("username") private String username;
    @SerializedName("password") private String password;

    public RequestLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestLogin{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
