package mdpa.lasalle.propertycross.ui.fragments.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.ui.activities.MainActivity;
import mdpa.lasalle.propertycross.util.Component;

public class MainFragment extends FragmentBase implements AdapterRecyclerBase.OnItemClickListener{

    private FloatingActionButton searchFAB;
    private RecyclerView recyclerProperties;
    private TextView numberPropertiesText;

    @NonNull @Override
    public ID getComponent() {
        return ID.MainFragment;
    }

    private OnSearchFragmentListener searchFragmentListener;
    public interface OnSearchFragmentListener {
        void onSearchFragment();
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.properties);
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_main_properties, container, false);

        searchFAB = (FloatingActionButton) root.findViewById(R.id.searchFAB);
        recyclerProperties = (RecyclerView) root.findViewById(R.id.recyclerProperties);
        numberPropertiesText = (TextView) root.findViewById(R.id.numberProperties);

        setListeners();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        searchFragmentListener = onAttachGetListener(OnSearchFragmentListener.class, context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        searchFragmentListener = null;
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

    private void setListeners(){
        searchFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFragmentListener.onSearchFragment();
            }
        });
    }

    @Override
    public void onItemClick(Object item, int position, View rowView, int viewType) {

    }

    @Override
    public void onItemLongClick(Object item, int position, View rowView, int viewType) {

    }
}
