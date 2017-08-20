package mdpa.lasalle.propertycross.http.project.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseFavouritesByUser extends Response {

    @SerializedName("data") private Data favouritesByUser;

    public class Data{
        @SerializedName("_id") private String id;
        @SerializedName("favourites") private ArrayList<String> favourites;

        public String getId() {
            return id;
        }

        public ArrayList<String> getFavourites() {
            return favourites;
        }
    }

    public Data getFavouritesByUser() {
        return favouritesByUser;
    }
}
