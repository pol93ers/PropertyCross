package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.data.Favourite;

public class ResponseFavourites extends Response {

    @SerializedName("data") private ArrayList<Favourite> favourites;

    public ArrayList<Favourite> getFavourites() {
        return favourites;
    }
}
