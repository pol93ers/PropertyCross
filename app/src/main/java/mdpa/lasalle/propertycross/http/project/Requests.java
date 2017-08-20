package mdpa.lasalle.propertycross.http.project;


import mdpa.lasalle.propertycross.BuildConfig;

public class Requests {
    private static Requests ourInstance = new Requests();
    public static Requests getInstance() {
        return ourInstance;
    }

    public static String getBaseServerUrl() {
        return "http://lmira.lasalle.ovh";
    }
    protected static String getApiServerUrl() {
        return getBaseServerUrl() + "/api";
    }

    public enum Values {
        POST_LOGIN("postLogin", getApiServerUrl() + "/login"),
        GET_USER("getUser", getApiServerUrl() + "/user/"),
        GET_PROPERTIES_TYPE("getPropertiesType", getApiServerUrl() + "/properties"),
        GET_PROPERTIES_LOCATION("getPropertiesLocation", getApiServerUrl() + "/properties"),
        GET_PROPERTY("getProperty", getApiServerUrl() + "/property/"),
        GET_PROPERTY_WITH_COMMENTS("getPropertyComments", getApiServerUrl() + "/propertyWithComments/"),
        PUT_INC_VIEWS("putIncViews", getApiServerUrl() + "/property/incViews/"),
        GET_FAVOURITES("getFavourites", getApiServerUrl() + "/user/"),
        GET_ID_FAVOURITES("getIDFavourites", getApiServerUrl() + "/user/"),
        PUT_UPDATE_FAVOURITE("putUpdateFavourite", getApiServerUrl() + "/user/"),
        GET_COMMENTS_PROPERTY("getCommentsProperty", getApiServerUrl() + "/comment/byProperty/"),
        POST_COMMENTS("postComments", getApiServerUrl() + "/comment/");
        public final String id;
        public String where;
        Values(String id, String where) {
            this.id = id;
            this.where = where;
        }
    }
}
