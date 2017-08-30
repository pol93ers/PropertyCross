package mdpa.lasalle.propertycross.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Comment implements Serializable {
    @SerializedName("_id") private String id;
    @SerializedName("property_id") private String propertyId;
    @SerializedName("content") private String content;
    @SerializedName("user") private User user;
    @SerializedName("createdAt") private String createdAt;

    public Comment(String id, String propertyId, String content, User user, String createdAt) {
        this.id = id;
        this.propertyId = propertyId;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", propertyId='" + propertyId + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
