package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RequestAddComment extends Request {
    @SerializedName("content") private String content;
    @SerializedName("user_id") private String userId;

    public RequestAddComment(String content, String userId) {
        this.content = content;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RequestAddComment{" +
                "content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
