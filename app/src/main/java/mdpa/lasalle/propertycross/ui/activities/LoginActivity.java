package mdpa.lasalle.propertycross.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;
import mdpa.lasalle.propertycross.ui.fragments.login.LoginFragment;
import mdpa.lasalle.propertycross.ui.fragments.login.SessionFragment;
import mdpa.lasalle.propertycross.ui.fragments.login.SignUpFragment;
import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.FragmentHelper;
import mdpa.lasalle.propertycross.util.FragmentManagerUtils;

public class LoginActivity extends ActivityBase implements SessionFragment.OnLoginFragmentListener,
        SessionFragment.OnSignUpFragmentListener, SessionFragment.OnLoginListener,
        LoginFragment.OnLoginListener, SignUpFragment.OnSignUpListener {

    @NonNull
    @Override
    public Component.ID getComponent() {
        return ID.LoginActivity;
    }

    public static Intent newStartIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            FragmentHelper helper = getFragmentHelper();
            helper.commit(helper.replace(
                    null,
                    new FragmentHelper.Target<>(
                            R.id.activity_content,
                            SessionFragment.newInstance()
                    )
            ));
        }
    }

    @Override
    public void onLoginFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, LoginFragment.newInstance(), true, true);
    }

    @Override
    public void onSignUpFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, SignUpFragment.newInstance(), true, true);
    }

    @Override
    public void onLogin(String username, String userID, String authToken) {
        ApplicationPropertyCross.getInstance().preferences().setUserEmail(username);
        ApplicationPropertyCross.getInstance().preferences().setUserId(userID);
        ApplicationPropertyCross.getInstance().preferences().setLoginApiKey(authToken);

        startActivityAndFinish(MainActivity.newStartIntent(this));
    }

    @Override
    public void onSignUp() {

    }
}
