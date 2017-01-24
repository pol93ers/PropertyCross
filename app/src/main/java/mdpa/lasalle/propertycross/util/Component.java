package mdpa.lasalle.propertycross.util;

import android.support.annotation.NonNull;

public interface Component {
    enum ID {
        Application("ApplicationPropertyCross"),

        SplashActivity("SplashActivity"),
        MainActivity("MainActivity"),
        LoginActivity("LoginActivity"),

        MainFragment("MainFragment"),
        MapLandscapeFragment("MapLandscapeFragment"),
        SessionFragment("SessionFragment"),
        LoginFragment("LoginFragment"),
        SignUpFragment("SignUpFragment"),
        MapFragment("MapFragment"),
        SearchFragment("SearchFragment"),
        PropertyFragment("PropertyFragment"),
        CommentFragment("CommentFragment"),

        AdapterRecyclerMain("AdapterRecyclerMain")
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
