package mdpa.lasalle.propertycross.data;

import java.util.ArrayList;

public class Comment {
    private String comment;
    private ArrayList<String> images;
    private String user;
    private String createdAt;

    public Comment(String comment, ArrayList<String> images, String user, String createdAt) {
        this.comment = comment;
        this.images = images;
        this.user = user;
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
