package mdpa.lasalle.propertycross.data;

import android.content.Context;
import android.support.annotation.Nullable;
import mdpa.lasalle.propertycross.base.preferences.PreferencesBase;

public class AppPreferences extends PreferencesBase{
    private enum PREFERENCE {
        USER_ID("user_id"),
        USER_EMAIL("user_email"),

        LOGIN_API_KEY("login_api_key"),

        DEVICE_TOKEN("device_token"),

        LAST_SEARCHES("last_searches"),
        ;

        private final String val;
        PREFERENCE(final String val) {
            this.val = val;
        }
        public String val() {
            return val;
        }
    }

    private static final int VERSION = 1;
    private static final String TAG = "propertycross_prefs";

    public AppPreferences(Context context) {
        super(context, TAG, VERSION);
    }

    public boolean setUserId(@Nullable String userId) {
        return putString(PREFERENCE.USER_ID.val(), userId);
    }

    public @Nullable String getUserId() {
        return getString(PREFERENCE.USER_ID.val(), null);
    }


    public boolean setUserEmail(@Nullable String userName) {
        return putString(PREFERENCE.USER_EMAIL.val(), userName);
    }

    public @Nullable String getUserEmail() {
        return getString(PREFERENCE.USER_EMAIL.val(), null);
    }

    public void removeUser() {
        setUserId(null);
        setUserEmail(null);
        setLoginApiKey(null);
    }

    public boolean setLoginApiKey(@Nullable String loginKey) {
        return putString(PREFERENCE.LOGIN_API_KEY.val(), loginKey);
    }

    public @Nullable String getLoginApiKey() {
        return getString(PREFERENCE.LOGIN_API_KEY.val(), null);
    }

    public void removeLogin() {
        setLoginApiKey(null);
    }

    public boolean setDeviceToken(@Nullable String loginKey) {
        return putString(PREFERENCE.DEVICE_TOKEN.val(), loginKey);
    }

    public @Nullable String getDeviceToken() {
        return getString(PREFERENCE.DEVICE_TOKEN.val(), null);
    }

    public boolean setLastSearches(@Nullable String lastSearches) {
        return putString(PREFERENCE.LAST_SEARCHES.val(), lastSearches);
    }

    public @Nullable String getLastSearches() {
        return getString(PREFERENCE.LAST_SEARCHES.val(), null);
    }
}
