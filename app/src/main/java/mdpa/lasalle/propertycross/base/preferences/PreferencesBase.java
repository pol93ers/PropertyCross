package mdpa.lasalle.propertycross.base.preferences;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import net.grandcentrix.tray.TrayPreferences;

public abstract class PreferencesBase {
    private final String PREFS_NAME;
    private final TrayPreferences preferences;

    public PreferencesBase(Context context, String preferencesName, int version) {
        PREFS_NAME = preferencesName;
        preferences = new TrayPreferences(context, PREFS_NAME, version) {
            @Override
            protected void onCreate(final int initialVersion) {
                PreferencesBase.this.onCreate(initialVersion);
            }

            @Override
            protected void onUpgrade(final int oldVersion, final int newVersion) {
                if(!PreferencesBase.this.onUpgrade(oldVersion, newVersion)) {
                    super.onUpgrade(oldVersion, newVersion);
                }
            }

            @Override
            protected void onDowngrade(final int oldVersion, final int newVersion) {
                if(!PreferencesBase.this.onDowngrade(oldVersion, newVersion)) {
                    super.onDowngrade(oldVersion, newVersion);
                }
            }
        };
    }

    protected void onCreate(final int initialVersion) {

    }

    protected boolean onUpgrade(final int oldVersion, final int newVersion) {
        return false;
    }

    protected boolean onDowngrade(final int oldVersion, final int newVersion) {
        return false;
    }

    public final String getPreferencesName() {
        return PREFS_NAME;
    }

    protected boolean putString(@NonNull String key, String val) {
        boolean isSuccessful = preferences.put(key, val);
        if (!isSuccessful) { onUnsuccessfulPut(key); }
        return isSuccessful;
    }

    protected @Nullable
    String getString(@NonNull String key, String def) {
        return preferences.getString(key, def);
    }

    protected boolean putInt(@NonNull String key, int val) {
        boolean isSuccessful = preferences.put(key, val);
        if (!isSuccessful) { onUnsuccessfulPut(key); }
        return isSuccessful;
    }

    protected int getInt(@NonNull String key, int def) {
        return preferences.getInt(key, def);
    }

    protected boolean putBoolean(@NonNull String key, boolean val) {
        boolean isSuccessful = preferences.put(key, val);
        if (!isSuccessful) { onUnsuccessfulPut(key); }
        return isSuccessful;
    }

    protected boolean getBoolean(@NonNull String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    protected void onUnsuccessfulPut(String key) {
        Log.e(PREFS_NAME, "Unable to save preference '" + key + "'");
    }
}
