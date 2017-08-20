package mdpa.lasalle.propertycross.util;

import com.facebook.Profile;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FacebookUserData {
    @SerializedName("id") private String id;
    @SerializedName("first_name") private String firstName;
    @SerializedName("last_name") private String lastName;
    @SerializedName("email") private String email;
    @SerializedName("birthday") private String birthday;
    @SerializedName("gender") private String gender;

    public FacebookUserData() {
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        SimpleDateFormat format = new SimpleDateFormat("MM'/'dd'/'yyyy", Locale.getDefault());
        try {
            return format.parse(birthday);
        } catch (NullPointerException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getGender() {
        return gender;
    }

    public static String generatePassword(Profile profile) {
        return "fbapp" + profile.getId();
    }
}