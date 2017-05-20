package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

public class RequestAddComment extends Request {
    @SerializedName("comment") private String comment;
    @SerializedName("propertyId") private String propertyId;

    public RequestAddComment(String comment, String propertyId) {
        this.comment = comment;
        this.propertyId = propertyId;
    }

    public String getComment() {
        return comment;
    }

    public String getPropertyId() {
        return propertyId;
    }
}
