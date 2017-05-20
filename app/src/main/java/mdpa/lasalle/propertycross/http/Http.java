package mdpa.lasalle.propertycross.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.burgstaller.okhttp.AuthenticationCacheInterceptor;
import com.burgstaller.okhttp.CachingAuthenticatorDecorator;
import com.burgstaller.okhttp.DispatchingAuthenticator;
import com.burgstaller.okhttp.basic.BasicAuthenticator;
import com.burgstaller.okhttp.digest.CachingAuthenticator;
import com.burgstaller.okhttp.digest.Credentials;
import com.burgstaller.okhttp.digest.DigestAuthenticator;
import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Http {
    private static final String TAG = Http.class.getSimpleName();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static Http ourInstance = new Http();
    public static Http getInstance() {
        return ourInstance;
    }

    public static final String REQUEST = "HTTP_request";
    public static final String ERROR = "HTTP_error";
    public static final String RESPONSE = "HTTP_response";

    private Credentials credentials;
    private OkHttpClient httpClient;
    private ExecutorService exService;
    private Context appContext;

    private Http() {
        exService = Executors.newCachedThreadPool();
    }

    public enum AuthType {
        NONE, BASIC, DIGEST
    }

    public enum RequestType {
        GET, POST, PUT, DELETE
    }

    public void initClient(AuthType type) {
        if (httpClient == null) {
            switch (type) {
                case NONE:
                    httpClient = new OkHttpClient();
                    break;
                case BASIC:
                    credentials = new Credentials(null, null);
                    httpClient = createClientBasic(credentials);
                    break;
                case DIGEST:
                    credentials = new Credentials(null, null);
                    httpClient = createClientDigest(credentials);
                    break;
            }
        } else {
            Log.w(TAG, "Http client only needs to be initialized once!");
        }
    }

    @NonNull
    public static OkHttpClient createClientBasic(Credentials credentials) {
        BasicAuthenticator basicAuthenticator = new BasicAuthenticator(credentials);

        DispatchingAuthenticator authenticator = new DispatchingAuthenticator.Builder()
                .with("basic", basicAuthenticator)
                .build();
        Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();

        return new OkHttpClient.Builder()
                .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                .build();
    }

    @NonNull
    public static OkHttpClient createClientDigest(Credentials credentials) {
        DigestAuthenticator digestAuthenticator = new DigestAuthenticator(credentials);

        DispatchingAuthenticator authenticator = new DispatchingAuthenticator.Builder()
                .with("digest", digestAuthenticator)
                .build();
        Map<String, CachingAuthenticator> authCache = new ConcurrentHashMap<>();

        return new OkHttpClient.Builder()
                .authenticator(new CachingAuthenticatorDecorator(authenticator, authCache))
                .addInterceptor(new AuthenticationCacheInterceptor(authCache))
                .build();
    }

    public void initContext(Context context) {
        if(appContext == null) {
            appContext = context.getApplicationContext();
        } else {
            Log.w(TAG, "Application context only needs to be set once!");
        }
    }

    public void setCredentials(String userEmail, String password) {
        credentials.setUserName(userEmail);
        credentials.setPassword(password);
    }

    public static Request createRequest(
            RequestType type, String url, @Nullable Object obj, @Nullable Map<String, String>  headers
    ) {
        Request.Builder builder = new Request.Builder()
                .url(url);

        if(headers != null) {
            for(Map.Entry<String, String> entry: headers.entrySet()){
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }

        String json = "";
        switch (type) {
            case POST:
            case PUT:
                json = new Gson().toJson(obj);
                break;
        }

        switch (type) {
            case GET:
                Log.v(TAG, REQUEST + "(GET): " + url);
                builder.get();
                break;
            case POST:
                Log.v(TAG, REQUEST + "(POST): " + url + "\nJSON" + json);
                builder.post(RequestBody.create(JSON, json));
                break;
            case PUT:
                Log.v(TAG, REQUEST + "(PUT): " + url + "\nJSON" + json);
                builder.put(RequestBody.create(JSON, json));
                break;
            case DELETE:
                Log.v(TAG, REQUEST + "(PUT): " + url + "\nJSON" + json);
                builder.delete(RequestBody.create(JSON, json));
                break;
        }

        return builder.build();
    }

    public Future call(Request request, String requestIdentifier) {
        return call(request, requestIdentifier, HttpRunnable.DEFAULT_MAX_ATTEMPTS);
    }

    public Future call(Request request, String requestIdentifier, int maxAttempts) {
        if(httpClient != null) {
            if(appContext != null) {
                return exService.submit(
                        new HttpRunnable(
                                httpClient, appContext, request, requestIdentifier,
                                HttpRunnable.DEFAULT_SLEEP_TIME, maxAttempts
                        )
                );
            } else {
                throw new NullPointerException("Application context not defined");
            }
        } else {
            throw new NullPointerException("HttpClient not initialized");
        }
    }
}
