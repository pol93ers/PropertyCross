package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.data.Comment;

public class ResponseComments extends Response{

    @SerializedName("comments") private ArrayList<Comment> comments;

    public ArrayList<Comment> getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "ResponseComments{" +
                "comments=" + comments +
                '}';
    }
}
