package mdpa.lasalle.propertycross.ui.fragments.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;

public class PropertyFragment extends FragmentBase{
    @NonNull
    @Override
    public ID getComponent() {
        return ID.PropertyFragment;
    }

    public static PropertyFragment newInstance() {
        return new PropertyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_property, container, false);

        setListeners();

        return root;
    }

    private void setListeners(){

    }
}