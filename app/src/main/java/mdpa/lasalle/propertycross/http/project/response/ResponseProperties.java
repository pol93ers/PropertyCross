package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.data.Property;

public class ResponseProperties extends Response{
    @SerializedName("properties") private ArrayList<Property> properties;

    public ArrayList<Property> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "ResponseProperties{" +
                "properties=" + properties +
                '}';
    }
}
