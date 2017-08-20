package mdpa.lasalle.propertycross;

import android.support.annotation.NonNull;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import mdpa.lasalle.propertycross.base.app.ApplicationBase;
import mdpa.lasalle.propertycross.data.AppPreferences;
import mdpa.lasalle.propertycross.http.Http;

public class ApplicationPropertyCross extends ApplicationBase{
    @NonNull
    @Override
    public ID getComponent() {
        return ID.Application;
    }

    private static ApplicationPropertyCross instance;
    public static ApplicationPropertyCross getInstance() {
        return instance;
    }

    private AppPreferences appPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        appPreferences = new AppPreferences(this);
        appPreferences.removeUser();
        appPreferences.removeLogin();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Http.getInstance().initClient(Http.AuthType.NONE);
        Http.getInstance().initContext(this);
    }

    public AppPreferences preferences() {
        return appPreferences;
    }
}
