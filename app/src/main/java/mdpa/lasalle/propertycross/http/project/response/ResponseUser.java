package mdpa.lasalle.propertycross.http.project.response;


import com.google.gson.annotations.SerializedName;

public class ResponseUser extends Response {
    @SerializedName("id") private String id;
    @SerializedName("email") private String email;
}
