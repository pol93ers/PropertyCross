package mdpa.lasalle.propertycross.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;
import mdpa.lasalle.propertycross.ui.fragments.main.FavouritesFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.MainFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.MapFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.ProfileFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.PropertyFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.SearchFragment;
import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.FragmentHelper;
import mdpa.lasalle.propertycross.util.FragmentManagerUtils;

public class MainActivity extends ActivityBase implements BottomNavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnSearchFragmentListener, ProfileFragment.OnLoginActivityListener,
        FavouritesFragment.OnLoginActivityListener, PropertyFragment.OnMapPropertyFragmentListener,
        MainFragment.OnPropertyFragmentListener{

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

        BottomNavigationView bottomNavigationView =
                (BottomNavigationView) findViewById(R.id.activity_main_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        View view = bottomNavigationView.findViewById(R.id.action_properties);
        view.performClick();


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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_properties:
                FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, MainFragment.newInstance(), true, true);
                break;
            case R.id.action_favourites:
                FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, FavouritesFragment.newInstance(), true, true);
                break;
            case R.id.action_profile:
                FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, ProfileFragment.newInstance(), true, true);
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStackImmediate();
        }

    }

    @Override
    public void onSearchFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, SearchFragment.newInstance(), true, true);
    }

    @Override
    public void onLoginActivity() {
        startActivity(LoginActivity.newStartIntent(this));
    }

    @Override
    public void onMapPropertyFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, MapFragment.newInstance(), true, true);
    }

    @Override
    public void onPropertyFragment() {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, PropertyFragment.newInstance(), true, true);
    }
}
