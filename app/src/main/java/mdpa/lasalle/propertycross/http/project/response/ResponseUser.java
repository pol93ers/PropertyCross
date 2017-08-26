package mdpa.lasalle.propertycross.http.project.response;


import com.google.gson.annotations.SerializedName;

import mdpa.lasalle.propertycross.data.User;

public class ResponseUser extends Response {
    @SerializedName("user") private User user;

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "ResponseUser{" +
                "user=" + user +
                '}';
    }
}
