package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.models.Property;

public class ResponseProperty extends Response {
    @SerializedName("data") private Property property;

    public Property getProperty() {
        return property;
    }
}
