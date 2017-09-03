package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

public class RequestLogin extends Request{
    @SerializedName("email") private String email;
    @SerializedName("password") private String password;

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public String toString() {
        return "RequestLogin{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
