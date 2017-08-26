package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin extends Response {
    @SerializedName("user_id") private String userId;
    @SerializedName("token") private String authToken;

    public String getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public String toString() {
        return "ResponseLogin{" +
                "userId='" + userId + '\'' +
                ", authToken='" + authToken + '\'' +
                '}';
    }
}
