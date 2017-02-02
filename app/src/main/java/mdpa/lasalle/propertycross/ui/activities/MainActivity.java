package mdpa.lasalle.propertycross.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;
import mdpa.lasalle.propertycross.ui.fragments.login.LoginFragment;
import mdpa.lasalle.propertycross.ui.fragments.login.SessionFragment;
import mdpa.lasalle.propertycross.ui.fragments.login.SignUpFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.MainFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.SearchFragment;
import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.FragmentHelper;
import mdpa.lasalle.propertycross.util.FragmentManagerUtils;

public class MainActivity extends ActivityBase implements MainFragment.OnSearchFragmentListener,
    SessionFragment.OnLoginFragmentListener, SessionFragment.OnSignUpFragmentListener{

    @NonNull
    @Override
    public Component.ID getComponent() {
        return Component.ID.MainActivity;
    }

    public static Intent newStartIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            FragmentHelper helper = getFragmentHelper();
            helper.commit(helper.replace(
                    null,
                    new FragmentHelper.Target<>(
                            R.id.activity_content,
                            MainFragment.newInstance()
                    )
            ));
        }
    }

    @Override
    public void onSearchFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, SearchFragment.newInstance(), true, true);
    }

    @Override
    public void onLoginFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, LoginFragment.newInstance(), true, true);
    }

    @Override
    public void onSignUpFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, SignUpFragment.newInstance(), true, true);
    }

    /*@Override
    public void onLoginActivity() {
        startActivityAndFinish(new Intent(this, LoginActivity.class));
    }*/
}
