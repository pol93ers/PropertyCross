package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.data.Image;

public class RequestAddComment extends Request {
    @SerializedName("content") private String content;
    @SerializedName("user_id") private String userId;
    @SerializedName("images") private ArrayList<Image> images;

    public RequestAddComment(String content, String userId, ArrayList<Image> images) {
        this.content = content;
        this.userId = userId;
        this.images = images;
    }

    @Override
    public String toString() {
        return "RequestAddComment{" +
                "content='" + content + '\'' +
                ", userId='" + userId + '\'' +
                ", images=" + images +
                '}';
    }
}
