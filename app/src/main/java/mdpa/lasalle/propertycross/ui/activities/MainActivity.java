package mdpa.lasalle.propertycross.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.ui.fragments.main.CommentFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.FavouritesFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.MainFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.MapFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.ProfileFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.PropertyFragment;
import mdpa.lasalle.propertycross.ui.fragments.search.SearchFragment;
import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.FragmentHelper;
import mdpa.lasalle.propertycross.util.FragmentManagerUtils;

public class MainActivity extends ActivityBase implements BottomNavigationView.OnNavigationItemSelectedListener,
        ProfileFragment.OnLoginActivityListener, FavouritesFragment.OnFavouriteUpdateListener,
        FavouritesFragment.OnLoginActivityListener, FavouritesFragment.OnPropertyFragmentListener,
        PropertyFragment.OnMapPropertyFragmentListener, MainFragment.OnPropertyFragmentListener,
        MainFragment.OnFavouriteUpdateListener, MainFragment.OnSessionFragmentListener,
        PropertyFragment.OnCommentFragmentListener{

    private boolean isMain;

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

        getHttpManager().receiverRegister(this, Requests.Values.POST_ADD_FAVOURITE);

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
    protected void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(this);
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
    public void onLoginActivity() {
        startActivity(LoginActivity.newStartIntent(this));
    }

    @Override
    public void onMapPropertyFragment(double latitude, double longitude) {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, MapFragment.newInstance(latitude, longitude), true, true);
    }

    @Override
    public void onPropertyFragment(String idProperty) {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, PropertyFragment.newInstance(idProperty), true, true);
    }

    @Override
    public void onFavouriteUpdate(String idProperty, boolean isMain) {
        this.isMain = isMain;
        getHttpManager().callStart(
                Http.RequestType.PUT,
                Requests.Values.POST_ADD_FAVOURITE,
                idProperty,
                null,
                ApplicationPropertyCross.getInstance().preferences().getLoginApiKey(),
                ApplicationPropertyCross.getInstance().preferences().getUserId(),
                null
        );
    }

    @Override
    public void onCommentFragment(String idProperty) {
        FragmentManagerUtils.fragmentReplace(getSupportFragmentManager(), R.id.activity_content, CommentFragment.newInstance(idProperty), true, true);
    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.POST_ADD_FAVOURITE.id)) {

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.POST_ADD_FAVOURITE.id)) {

        }
    }
}
