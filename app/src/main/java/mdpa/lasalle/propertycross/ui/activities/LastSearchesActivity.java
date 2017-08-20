package mdpa.lasalle.propertycross.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;
import mdpa.lasalle.propertycross.ui.fragments.last_searches.LastSearchesFragment;
import mdpa.lasalle.propertycross.util.FragmentHelper;

public class LastSearchesActivity extends ActivityBase implements LastSearchesFragment.OnLastSearchesListener {

    @NonNull @Override
    public ID getComponent() {
        return ID.LastSearchsActivity;
    }

    public static Intent newStartIntent(Context context) {
        return new Intent(context, LastSearchesActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_searches);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(R.string.search);

        if (savedInstanceState == null) {
            FragmentHelper helper = getFragmentHelper();
            helper.commit(helper.replace(
                    null,
                    new FragmentHelper.Target<>(
                            R.id.activity_content,
                            LastSearchesFragment.newInstance()
                    )
            ));
        }
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
    public void onSearch(String search, boolean isLocation, double latitude, double longitude) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("search", search);
        returnIntent.putExtra("isLocation", isLocation);
        if (isLocation){
            returnIntent.putExtra("latitude", latitude);
            returnIntent.putExtra("longitude", longitude);
        }
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
