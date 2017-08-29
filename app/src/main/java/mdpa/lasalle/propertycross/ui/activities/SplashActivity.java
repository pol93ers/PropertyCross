package mdpa.lasalle.propertycross.ui.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;
import mdpa.lasalle.propertycross.util.AnimationHelper;
import mdpa.lasalle.propertycross.util.ResourcesUtil;

public class SplashActivity extends ActivityBase {
    @NonNull
    @Override
    public ID getComponent() {
        return ID.SplashActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View appLogo = findViewById(R.id.app_logo);
        ObjectAnimator alphaAnimator = AnimationHelper.createOfAlpha(appLogo, true);
        alphaAnimator.setDuration(ResourcesUtil.getInt(this, R.integer.anim_millisTime_splash));
        alphaAnimator.addListener(new AnimationHelper.AnimationEndListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startMainActivity();
            }
        });
        AnimationHelper.startAnimator(alphaAnimator, false);
    }

    private void startMainActivity() {
        startActivityAndFinish(
                MainActivity.newStartIntent(this)
        );
    }
}
