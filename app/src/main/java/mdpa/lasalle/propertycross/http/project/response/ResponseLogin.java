package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseLogin extends Response {
    @SerializedName("data") private Data data;

    public class Data implements Serializable{
        @SerializedName("userId") private String userId;
        @SerializedName("authToken") private String authToken;

        public String getUserId() {
            return userId;
        }

        public String getAuthToken() {
            return authToken;
        }

        @Override
        public String toString() {
            return "Data{" +
                    "userId=" + userId +
                    ", authToken='" + authToken + '\'' +
                    '}';
        }
    }

    public Data getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ResponseLogin{" +
                "data=" + data +
                '}';
    }
}
