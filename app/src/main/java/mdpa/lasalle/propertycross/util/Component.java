package mdpa.lasalle.propertycross.util;

import android.support.annotation.NonNull;

public interface Component {
    enum ID {
        Application("ApplicationPropertyCross"),

        SplashActivity("SplashActivity"),
        MainActivity("MainActivity"),

        MainFragment("MainFragment"),
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
