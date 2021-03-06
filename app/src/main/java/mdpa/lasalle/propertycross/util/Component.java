package mdpa.lasalle.propertycross.util;

import android.support.annotation.NonNull;

public interface Component {
    enum ID {
        Application("ApplicationPropertyCross"),

        SplashActivity("SplashActivity"),
        MainActivity("MainActivity"),
        LoginActivity("LoginActivity"),
        SearchActivity("SearchActivity"),
        LastSearchesActivity("LastSearchesActivity"),

        MainFragment("MainFragment"),
        FavouritesFragment("FavouritesFragment"),
        ProfileFragment("ProfileFragment"),
        MapFragment("MapFragment"),
        PropertyFragment("PropertyFragment"),
        CommentFragment("CommentFragment"),

        SessionFragment("SessionFragment"),
        LoginFragment("LoginFragment"),
        SignUpFragment("SignUpFragment"),

        SearchFragment("SearchFragment"),

        LastSearchesFragment("LastSearchesFragment"),

        AdapterRecyclerMain("AdapterRecyclerProperties"),
        AdapterRecyclerFavourites("AdapterRecyclerFavourites"),
        AdapterRecyclerComments("AdapterRecyclerComments"),
        AdapterLastSearches("AdapterLastSearches"),
        ;

        private String id;
        ID(String id) {
            this.id = id;
        }
        public String id() {
            return id;
        }

        @Override
        public String toString() {
            return id();
        }
    }
    @NonNull
    ID getComponent();
}
