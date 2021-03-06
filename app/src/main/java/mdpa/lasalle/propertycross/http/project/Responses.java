package mdpa.lasalle.propertycross.http.project;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseComments;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseFavourites;
import mdpa.lasalle.propertycross.http.project.response.ResponseGeneric;
import mdpa.lasalle.propertycross.http.project.response.ResponseLogin;
import mdpa.lasalle.propertycross.http.project.response.ResponseProperties;
import mdpa.lasalle.propertycross.http.project.response.ResponseProperty;
import mdpa.lasalle.propertycross.http.project.response.ResponseUser;

public class Responses {

    private static final String TAG = Responses.class.getSimpleName();

    private static Responses ourInstance = new Responses();
    public static Responses getInstance() {return ourInstance;}

    private Gson gson;

    private Responses() {gson = new Gson();}

    public Object handleResponse(
            okhttp3.Response httpResponse, String requestId
    ) throws InstantiationException, IllegalAccessException {
        if (requestId.equals(Http.ERROR)) {
            return getFromBodyJson(httpResponse, ResponseError.class, requestId);
        } else if (requestId.equals(Requests.Values.POST_COMMENT.id)){
            return getFromBodyJson(httpResponse, ResponseGeneric.class, requestId);
        } else if (requestId.equals(Requests.Values.PUT_INC_VIEWS.id)){
            return getFromBodyJson(httpResponse, ResponseGeneric.class, requestId);
        } else if (requestId.equals(Requests.Values.GET_PROPERTY.id)){
            return getFromBodyJson(httpResponse, ResponseProperty.class, requestId);
        } else if (requestId.equals(Requests.Values.GET_COMMENTS_PROPERTY.id)){
            return getFromBodyJson(httpResponse, ResponseComments.class, requestId);
        } else if (requestId.equals(Requests.Values.GET_PROPERTIES.id)){
            return getFromBodyJson(httpResponse, ResponseProperties.class, requestId);

        } else if (requestId.equals(Requests.Values.DELETE_USER.id)){
            return getFromBodyJson(httpResponse, ResponseGeneric.class, requestId);
        } else if (requestId.equals(Requests.Values.GET_USER.id)){
            return getFromBodyJson(httpResponse, ResponseUser.class, requestId);
        } else if (requestId.equals(Requests.Values.POST_LOGIN.id)){
            return getFromBodyJson(httpResponse, ResponseLogin.class, requestId);
        } else if (requestId.equals(Requests.Values.POST_SIGN_UP.id)){
            return getFromBodyJson(httpResponse, ResponseGeneric.class, requestId);
        } else if (requestId.equals(Requests.Values.POST_UPDATE_USER.id)){
            return getFromBodyJson(httpResponse, ResponseUser.class, requestId);

        } else if (requestId.equals(Requests.Values.PUT_ADD_FAVOURITE.id)){
            return getFromBodyJson(httpResponse, ResponseGeneric.class, requestId);
        } else if (requestId.equals(Requests.Values.GET_FAVOURITES.id)){
            return getFromBodyJson(httpResponse, ResponseFavourites.class, requestId);
        } else if (requestId.equals(Requests.Values.GET_IS_FAVOURITE.id)){
            return getFromBodyJson(httpResponse, ResponseGeneric.class, requestId);

        } else {
            httpResponse.body().close();
            throw new IllegalArgumentException("Response for " + requestId + " not implemented!");
        }
    }

    private <T extends Response> Response getFromBodyJson(
            okhttp3.Response httpResponse, Class<T> cls, String requestId
    ) throws IllegalAccessException, InstantiationException {
        Response obj = null;
        try {
            obj = gson.fromJson(httpResponse.body().charStream(), cls);
        } catch (JsonIOException | JsonSyntaxException e) {
            Log.w(TAG, "Error parsing object of type " + cls.getSimpleName(), e);
            httpResponse.body().close();
        }
        if (obj == null) obj = cls.newInstance();
        obj.setHttpStatus(httpResponse);
        obj.setRequestId(requestId);
        return obj;
    }
}
