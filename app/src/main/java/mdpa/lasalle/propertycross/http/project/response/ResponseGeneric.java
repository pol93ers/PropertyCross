package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

public class ResponseGeneric extends Response{

    @SerializedName("data") private Data data;

    public class Data {
        @SerializedName("message") private String message;

        public String getMessage() {
            return message;
        }
    }

    public Data getData() {
        return data;
    }
}
