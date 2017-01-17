package mdpa.lasalle.propertycross.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.activity.ActivityBase;
import mdpa.lasalle.propertycross.ui.fragments.main.MainFragment;
import mdpa.lasalle.propertycross.util.Component;
import mdpa.lasalle.propertycross.util.FragmentHelper;

public class MainActivity extends ActivityBase {
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

        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

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
}
