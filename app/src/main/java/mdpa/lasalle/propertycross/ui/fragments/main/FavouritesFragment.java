package mdpa.lasalle.propertycross.ui.fragments.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.ui.activities.MainActivity;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerFavourites;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerMain;

public class FavouritesFragment extends FragmentBase{

    private RelativeLayout noSessionFavouritesLayout, favouritesLayout;
    private Button sessionFavouritesButton;
    private TextView numFavouritesTextView;
    private RecyclerView favouritesRecyclerView;

    private AdapterRecyclerFavourites adapter;

    private OnLoginActivityListener loginActivityListener;
    public interface OnLoginActivityListener {
        void onLoginActivity();
    }

    private OnPropertyFragmentListener propertyFragmentListener;
    public interface OnPropertyFragmentListener {
        void onPropertyFragment();
    }

    @NonNull
    @Override
    public ID getComponent() {
        return ID.FavouritesFragment;
    }

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        loginActivityListener = onAttachGetListener(OnLoginActivityListener.class, context);
        propertyFragmentListener = onAttachGetListener(OnPropertyFragmentListener.class, context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.favourites);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_main_favourites, container, false);

        noSessionFavouritesLayout = (RelativeLayout) root.findViewById(R.id.noSessionFavouritesLayout);
        favouritesLayout = (RelativeLayout) root.findViewById(R.id.favouritesLayout);
        sessionFavouritesButton = (Button) root.findViewById(R.id.sessionFavouritesButton);
        numFavouritesTextView = (TextView) root.findViewById(R.id.numberPropertiesFavourites);
        favouritesRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerFavourites);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        favouritesRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AdapterRecyclerFavourites(getContext(), propertyFragmentListener);

        setListeners();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(ApplicationPropertyCross.getInstance().preferences().getLoginApiKey() != null){
            noSessionFavouritesLayout.setVisibility(View.GONE);
            favouritesLayout.setVisibility(View.VISIBLE);
        }else{
            noSessionFavouritesLayout.setVisibility(View.VISIBLE);
            favouritesLayout.setVisibility(View.GONE);
        }
    }

    private void setListeners(){
        sessionFavouritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginActivityListener.onLoginActivity();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main:
                final CharSequence[] type_order = {getString(R.string.less_more_meters), getString(R.string.more_less_meters),
                        getString(R.string.less_more_distance), getString(R.string.less_more_price), getString(R.string.more_less_price)};
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                dialogBuilder.setTitle(getString(R.string.title_order));
                dialogBuilder.setItems(type_order, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                    }
                });
                AlertDialog alertDialogObject = dialogBuilder.create();
                alertDialogObject.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
