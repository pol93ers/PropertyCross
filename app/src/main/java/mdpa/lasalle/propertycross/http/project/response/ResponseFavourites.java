package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.data.Property;

public class ResponseFavourites extends Response {

    @SerializedName("favourites") private ArrayList<Property> favourites;

    public ArrayList<Property> getFavourites() {
        return favourites;
    }

    @Override
    public String toString() {
        return "ResponseFavourites{" +
                "favourites=" + favourites +
                '}';
    }
}
