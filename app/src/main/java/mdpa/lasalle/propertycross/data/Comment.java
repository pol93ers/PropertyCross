package mdpa.lasalle.propertycross.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {
    @SerializedName("_id") private String id;
    @SerializedName("property_id") private String propertyId;
    @SerializedName("comment") private String content;
    @SerializedName("user") private UserComment user;
    @SerializedName("time") private String time;

    public class UserComment implements Serializable{
        @SerializedName("_id") private String id;
        @SerializedName("username") private String username;

        public UserComment(String id, String username) {
            this.id = id;
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public String toString() {
            return "UserComment{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    '}';
        }
    }

    public Comment(String id, String propertyId, String content, UserComment user, String time) {
        this.id = id;
        this.propertyId = propertyId;
        this.content = content;
        this.user = user;
        this.time = time;
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

    public UserComment getUser() {
        return user;
    }

    public void setUser(UserComment user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", propertyId='" + propertyId + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", time='" + time + '\'' +
                '}';
    }
}
