package mdpa.lasalle.propertycross.data;

import android.content.Context;
import android.support.annotation.Nullable;
import mdpa.lasalle.propertycross.base.preferences.PreferencesBase;

public class AppPreferences extends PreferencesBase{
    private enum PREFERENCE {
        USER_ID("user_id"),
        USER_EMAIL("user_email"),
        USER_PASSWORD("user_password"),

        LOGIN_API_KEY("login_api_key"),

        DEVICE_TOKEN("device_token"),

        DISCONNECT_USER("disconnect_user"),
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

    public boolean setUserId(@Nullable int userId) {
        return putInt(PREFERENCE.USER_ID.val(), userId);
    }

    public @Nullable int getUserId() {
        return getInt(PREFERENCE.USER_ID.val(), 0);
    }


    public boolean setUserEmail(@Nullable String userName) {
        return putString(PREFERENCE.USER_EMAIL.val(), userName);
    }

    public @Nullable String getUserEmail() {
        return getString(PREFERENCE.USER_EMAIL.val(), null);
    }


    public boolean setUserPassword(@Nullable String userPass) {
        return putString(PREFERENCE.USER_PASSWORD.val(), userPass);
    }

    public @Nullable String getUserPassword() {
        return getString(PREFERENCE.USER_PASSWORD.val(), null);
    }

    public void removeUser() {
        setUserId(0);
        setUserEmail(null);
        setUserPassword(null);
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

    // endregion accessors login

    // region accessors device

    public boolean setDeviceToken(@Nullable String loginKey) {
        return putString(PREFERENCE.DEVICE_TOKEN.val(), loginKey);
    }

    public @Nullable String getDeviceToken() {
        return getString(PREFERENCE.DEVICE_TOKEN.val(), null);
    }

    public boolean setDisconnectUser(@Nullable boolean disconnectBool) {
        return putBoolean(PREFERENCE.DISCONNECT_USER.val(), disconnectBool);
    }

    public @Nullable boolean getDisconnectUser() {
        return getBoolean(PREFERENCE.DISCONNECT_USER.val(), false);
    }
}
