package mdpa.lasalle.propertycross;

import android.support.annotation.NonNull;

import mdpa.lasalle.propertycross.base.app.ApplicationBase;
import mdpa.lasalle.propertycross.data.AppPreferences;

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
    }

    public AppPreferences preferences() {
        return appPreferences;
    }
}
