package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.data.Property;

public class ResponseProperties extends Response{
    @SerializedName("data") private ArrayList<Property> properties;

    public ArrayList<Property> getProperties() {
        return properties;
    }
}
