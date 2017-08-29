package mdpa.lasalle.propertycross.http.project;

public class Requests {
    private static Requests ourInstance = new Requests();
    public static Requests getInstance() {
        return ourInstance;
    }

    private static String getBaseServerUrl() {
        return "http://ls25860.lasalle.ovh";
    }
    private static String getApiServerUrl() {
        return getBaseServerUrl() + "/api";
    }

    public enum Values {
        //Properties
        POST_COMMENT("postComment", getApiServerUrl() + "/properties/"),
        POST_INC_VIEWS("postIncViews", getApiServerUrl() + "/properties/"),
        GET_PROPERTY("getProperty", getApiServerUrl() + "/properties/"),
        GET_COMMENTS_PROPERTY("getCommentsProperty", getApiServerUrl() + "/properties/"),
        GET_PROPERTIES("getProperties", getApiServerUrl() + "/properties"),

        //Session
        DELETE_USER("deleteUser", getApiServerUrl() + "/profile"),
        GET_USER("getUser", getApiServerUrl() + "/profile/"),
        POST_LOGIN("postLogin", getApiServerUrl() + "/login"),
        POST_SIGN_UP("postSignUp", getApiServerUrl() + "/signup"),
        POST_UPDATE_USER("postUpdateUser", getApiServerUrl() + "/profile"),

        //Users
        POST_ADD_FAVOURITE("postAddFavourite", getApiServerUrl() + "/users/favourites/"),
        GET_FAVOURITES("getFavourites", getApiServerUrl() + "/users/favourites"),
        GET_IS_FAVOURITE("getIsFavourites", getApiServerUrl() + "/users/favourites/");
        public final String id;
        public String where;
        Values(String id, String where) {
            this.id = id;
            this.where = where;
        }
    }
}
