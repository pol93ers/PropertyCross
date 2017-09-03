package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestAddComment extends Request {
    @SerializedName("comment") private String comment;
    @SerializedName("user_id") private String userId;

    public RequestAddComment(String comment, String userId) {
        this.comment = comment;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RequestAddComment{" +
                "comment='" + comment + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
