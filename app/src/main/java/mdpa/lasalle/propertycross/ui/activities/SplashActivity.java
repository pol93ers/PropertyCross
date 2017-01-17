package mdpa.lasalle.propertycross.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;

public class SplashActivity extends ActivityBase {
    @NonNull
    @Override
    public ID getComponent() {
        return ID.SplashActivity;
    }

    public static Intent newStartIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    private void startMainActivity() {
        startActivityAndFinish(
                MainActivity.newStartIntent(this)
        );
    }
}
