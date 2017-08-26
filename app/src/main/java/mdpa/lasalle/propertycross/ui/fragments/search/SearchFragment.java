package mdpa.lasalle.propertycross.ui.fragments.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.data.Property;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseProperties;
import mdpa.lasalle.propertycross.ui.activities.LastSearchesActivity;
import mdpa.lasalle.propertycross.ui.activities.SearchActivity;

public class SearchFragment extends FragmentBase{

    private TextView searchText;
    private Button buyTypeButton, rentTypeButton, searchButton;

    private String type = "buy";
    private String search;
    private boolean isLocation;
    private double latitude, longitude;

    private final static int RESULT_LAST_SEARCH = 1;

    @NonNull @Override
    public ID getComponent() {
        return ID.SearchFragment;
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    private OnSearchListener listener;
    public interface OnSearchListener {
        void onSearch(ArrayList<Property> properties);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = onAttachGetListener(OnSearchListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_PROPERTIES);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((SearchActivity)getActivity()).getSupportActionBar().setTitle(R.string.search);
        ((SearchActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((SearchActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        searchText = (TextView) root.findViewById(R.id.searchTextView);
        buyTypeButton = (Button) root.findViewById(R.id.buyTypeButton);
        rentTypeButton = (Button) root.findViewById(R.id.rentTypeButton);
        searchButton = (Button) root.findViewById(R.id.searchButton);

        setListeners();

        return root;
    }

    private void setListeners(){

        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(LastSearchesActivity.newStartIntent(getContext()), RESULT_LAST_SEARCH);
            }
        });

        buyTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "buy";
                buyTypeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                buyTypeButton.setTextColor(getResources().getColor(R.color.colorAccent));
                rentTypeButton.setBackgroundResource(R.drawable.bg_border_black);
                rentTypeButton.setTextColor(getResources().getColor(android.R.color.black));
            }
        });

        rentTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "rent";
                rentTypeButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                rentTypeButton.setTextColor(getResources().getColor(R.color.colorAccent));
                buyTypeButton.setBackgroundResource(R.drawable.bg_border_black);
                buyTypeButton.setTextColor(getResources().getColor(android.R.color.black));
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLocation){
                    String urlSearch = "?type=" + type + "&matchSearch=" + search;
                    getHttpManager().callStart(
                            Http.RequestType.GET,
                            Requests.Values.GET_PROPERTIES,
                            urlSearch,
                            null,
                            null,
                            null,
                            null
                    );
                }else{
                    String urlSearch = "?lat=" + latitude + "&lng=" + longitude + "&type=" + type;
                    getHttpManager().callStart(
                            Http.RequestType.GET,
                            Requests.Values.GET_PROPERTIES,
                            urlSearch,
                            null,
                            null,
                            null,
                            null
                    );
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_LAST_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    search = data.getStringExtra("search");
                    isLocation = data.getBooleanExtra("isLocation", false);
                    if(isLocation){
                        latitude = data.getLongExtra("latitude", 0);
                        longitude = data.getLongExtra("longitude", 0);
                    }
                    searchText.setText(search);
                }
                break;
        }
    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.GET_PROPERTIES.id)) {

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.GET_PROPERTIES.id)) {
            ArrayList<Property> properties = ((ResponseProperties) response).getProperties();
            listener.onSearch(properties);
        }
    }
}
