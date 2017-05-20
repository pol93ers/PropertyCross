package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

public class RequestLogin extends Request{
    @SerializedName("email") private String email;
    @SerializedName("password") private String password;

    public RequestLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
