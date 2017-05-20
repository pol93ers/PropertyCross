package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

public class ResponseLogin extends Response {
    @SerializedName("data") private Data login;

    public class Data{
        @SerializedName("authToken") private String authToken;
        @SerializedName("userId") private String userId;

        public String getAuthToken() {
            return authToken;
        }

        public String getUserId() {
            return userId;
        }
    }

    public Data getLogin() {
        return login;
    }
}
