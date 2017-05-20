package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

public class ResponseError extends Response{

    @SerializedName(value="data", alternate={"token","message"}) private String error;

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "ResponseError{" +
                "httpStatus=" + httpStatus +
                ", error='" + error + '\'' +
                '}';
    }
}
