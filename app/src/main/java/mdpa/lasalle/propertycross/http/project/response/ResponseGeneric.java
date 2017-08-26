package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

public class ResponseGeneric extends Response{

    @SerializedName("status") private String status;
    @SerializedName("message") private String message;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResponseGeneric{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
