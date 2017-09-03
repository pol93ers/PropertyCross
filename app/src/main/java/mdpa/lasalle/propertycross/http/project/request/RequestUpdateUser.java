package mdpa.lasalle.propertycross.http.project.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestUpdateUser extends Request{
    @SerializedName("user") private UpdateUser user;

    public class UpdateUser implements Serializable{
        @SerializedName("username") private String username;
        @SerializedName("email") private String email;
        @SerializedName("name") private String name;
        @SerializedName("surname") private String surname;
        @SerializedName("isNotification") private boolean isNotification;

        public UpdateUser(String username, String email, String name, String surname, boolean isNotification) {
            this.username = username;
            this.email = email;
            this.name = name;
            this.surname = surname;
            this.isNotification = isNotification;
        }
    }

    public RequestUpdateUser(String username, String email, String name, String surname, boolean isNotification) {
        this.user = new UpdateUser(username, email, name, surname, isNotification);
    }

    @Override
    public String toString() {
        return "RequestUpdateUser{" +
                "user=" + user +
                '}';
    }
}
