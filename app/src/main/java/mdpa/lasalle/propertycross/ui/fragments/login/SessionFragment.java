package mdpa.lasalle.propertycross.ui.fragments.login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;


public class SessionFragment extends FragmentBase {

    @NonNull
    @Override
    public ID getComponent() {
        return ID.SessionFragment;
    }

    public static SessionFragment newInstance() {
        return new SessionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session, container, false);

        return root;
    }


}
