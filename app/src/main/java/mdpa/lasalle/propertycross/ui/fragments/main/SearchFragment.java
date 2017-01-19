package mdpa.lasalle.propertycross.ui.fragments.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;

public class SearchFragment extends FragmentBase{

    private EditText searchEditText;
    private Button buyTypeButton, rentTypeButton, searchButton;

    @NonNull @Override
    public ID getComponent() {
        return ID.SearchFragment;
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = (EditText) root.findViewById(R.id.searchEditText);
        buyTypeButton = (Button) root.findViewById(R.id.buyTypeButton);
        rentTypeButton = (Button) root.findViewById(R.id.rentTypeButton);
        searchButton = (Button) root.findViewById(R.id.searchButton);

        setListeners();

        return root;
    }

    private void setListeners(){
        buyTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rentTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
