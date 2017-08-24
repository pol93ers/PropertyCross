package mdpa.lasalle.propertycross.ui.fragments.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.data.Property;
import mdpa.lasalle.propertycross.data.adapter.PropertyItem;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseFavouritesByUser;
import mdpa.lasalle.propertycross.ui.activities.MainActivity;
import mdpa.lasalle.propertycross.ui.activities.SearchActivity;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerMain;

public class MainFragment extends FragmentBase implements AdapterRecyclerBase.OnItemClickListener, OnMapReadyCallback {

    private FloatingActionButton searchFAB;
    private RecyclerView recyclerProperties;
    private TextView numberPropertiesText, emptyMapText, numberPropertiesMapText;
    private TabLayout propertiesTabLayout;
    private TabItem saleTabItem, rentalTabItem;

    private GoogleMap googleMap;

    private AdapterRecyclerMain adapter;

    private final static int RESULT_SEARCH = 1;

    private ArrayList<Property> properties;
    private ArrayList<PropertyItem> propertyItems = new ArrayList<>();
    private ArrayList<PropertyItem> propertySaleItems = new ArrayList<>();
    private ArrayList<PropertyItem> propertyRentalItems = new ArrayList<>();
    private ArrayList<String> favouritePropertiesIds = new ArrayList<>();
    private String idProperty;

    @NonNull @Override
    public ID getComponent() {
        return ID.MainFragment;
    }

    private OnPropertyFragmentListener propertyFragmentListener;
    public interface OnPropertyFragmentListener {
        void onPropertyFragment(String idProperty);
    }

    private OnFavouriteUpdateListener favouriteUpdateListener;
    public interface OnFavouriteUpdateListener {
        void onFavouriteUpdate(String idProperty, boolean isMain);
    }

    private OnSessionFragmentListener sessionFragmentListener;
    public interface OnSessionFragmentListener {
        void onLoginActivity();
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.PUT_INC_VIEWS);
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_ID_FAVOURITES);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        propertyFragmentListener = onAttachGetListener(OnPropertyFragmentListener.class, context);
        favouriteUpdateListener = onAttachGetListener(OnFavouriteUpdateListener.class, context);
        sessionFragmentListener = onAttachGetListener(OnSessionFragmentListener.class, context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert ((MainActivity)getActivity()).getSupportActionBar() != null;
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.properties);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_main_properties, container, false);

        propertiesTabLayout = (TabLayout) root.findViewById(R.id.tabPropertiesLayout);
        saleTabItem = (TabItem) root.findViewById(R.id.buyPropertiesTabItem);
        rentalTabItem = (TabItem) root.findViewById(R.id.rentPropertiesTabItem);
        searchFAB = (FloatingActionButton) root.findViewById(R.id.searchFAB);
        numberPropertiesText = (TextView) root.findViewById(R.id.numberProperties);
        recyclerProperties = (RecyclerView) root.findViewById(R.id.recyclerProperties);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (recyclerProperties != null) {
            recyclerProperties.setLayoutManager(linearLayoutManager);
            adapter = new AdapterRecyclerMain(getContext(), favouriteUpdateListener, sessionFragmentListener);
            recyclerProperties.setAdapter(adapter);
        }else{
            MapView mapView = (MapView) root.findViewById(R.id.mapView);
            mapView.getMapAsync(this);
        }
        emptyMapText = (TextView) root.findViewById(R.id.emptyMapText);
        numberPropertiesMapText = (TextView) root.findViewById(R.id.numPropertiesMap);

        setListeners();

        return root;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        try {
            // Zoom googleMap camera un initial position.
            googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                            location.getLatitude(), location.getLongitude()), 15
                    ));
                    googleMap.setOnMyLocationChangeListener(null);
                }
            });
            googleMap.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            Log.e(getComponent().toString(), "Error setting location enabled", e);
        }
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
                        switch (item){
                            case 0:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return Integer.parseInt(p1.getProperty().getMeters()) - Integer.parseInt(p2.getProperty().getMeters());
                                    }
                                });
                                break;
                            case 1:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return Integer.parseInt(p2.getProperty().getMeters()) - Integer.parseInt(p1.getProperty().getMeters());
                                    }
                                });
                                break;
                            case 2:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return (int)(p1.getProperty().getDistance() - p2.getProperty().getDistance());
                                    }
                                });
                                break;
                            case 3:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return Integer.parseInt(p1.getProperty().getPrice()) - Integer.parseInt(p2.getProperty().getPrice());
                                    }
                                });
                                break;
                            case 4:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return Integer.parseInt(p2.getProperty().getPrice()) - Integer.parseInt(p1.getProperty().getPrice());
                                    }
                                });
                                break;
                        }
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
        if(searchFAB != null) {
            searchFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(SearchActivity.newStartIntent(getContext()), RESULT_SEARCH);
                }
            });
        }

        /*saleTabItem.setOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!propertySaleItems.isEmpty()){
                    propertyItems = propertySaleItems;
                    String numberProperties = propertyItems.size() + getString(R.string.properties);
                    numberPropertiesText.setText(numberProperties);
                    adapter.setItems(propertyItems);
                }else{
                    numberPropertiesText.setText(R.string.results_not_found);
                    adapter.clearItems();
                }
            }
        });

        rentalTabItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!propertySaleItems.isEmpty()){
                    propertyItems = propertySaleItems;
                    String numberProperties = propertyItems.size() + getString(R.string.properties);
                    numberPropertiesText.setText(numberProperties);
                    adapter.setItems(propertyItems);
                }else{
                    numberPropertiesText.setText(R.string.results_not_found);
                    adapter.clearItems();
                }
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    propertySaleItems = new ArrayList<>();
                    propertyRentalItems = new ArrayList<>();
                    properties = (ArrayList<Property>) data.getSerializableExtra("properties");
                    int numberProperties = properties.size();
                    for (int i=0;i<numberProperties;i++) {
                        boolean isFavourite = false;
                        for (int j=0;j<favouritePropertiesIds.size();j++){
                            if (properties.get(i).getId().equals(favouritePropertiesIds.get(j))){
                                isFavourite = true;
                                break;
                            }
                        }
                        if (properties.get(i).getPropertyType().equals(getString(R.string.sale))){
                            propertySaleItems.add(new PropertyItem(new PropertyItem.Property(properties.get(i).getId(),
                                    properties.get(i).getCity(), Uri.parse(properties.get(i).getImages().get(0)),
                                    String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                                    properties.get(i).getPropertyType(), properties.get(i).getDistance(), isFavourite)));
                        }else{
                            propertyRentalItems.add(new PropertyItem(new PropertyItem.Property(properties.get(i).getId(),
                                    properties.get(i).getCity(), Uri.parse(properties.get(i).getImages().get(0)),
                                    String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                                    properties.get(i).getPropertyType(), properties.get(i).getDistance(), isFavourite)));
                        }
                    }

                    if (propertiesTabLayout.getSelectedTabPosition() == 0){
                        if (!propertySaleItems.isEmpty()){
                            propertyItems = propertySaleItems;
                            String numProperties = propertyItems.size() + getString(R.string.properties);
                            numberPropertiesText.setText(numProperties);
                            adapter.setItems(propertyItems);
                        }
                    }else{
                        if (!propertySaleItems.isEmpty()){
                            propertyItems = propertySaleItems;
                            String numProperties = propertyItems.size() + getString(R.string.properties);
                            numberPropertiesText.setText(numProperties);
                            adapter.setItems(propertyItems);
                        }
                    }

                    if (numberPropertiesMapText != null) {
                        if (numberProperties != 0){
                            numberPropertiesMapText.setText(numberProperties);
                            emptyMapText.setVisibility(View.GONE);
                        }else{
                            numberPropertiesMapText.setVisibility(View.GONE);
                            emptyMapText.setVisibility(View.VISIBLE);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onItemClick(Object item, int position, View rowView, int viewType) {
        idProperty = propertyItems.get(position).getProperty().getId();
        getHttpManager().callStart(
                Http.RequestType.PUT,
                Requests.Values.PUT_INC_VIEWS,
                idProperty,
                null,
                null,
                null
        );
    }

    @Override
    public void onItemLongClick(Object item, int position, View rowView, int viewType) {

    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.PUT_INC_VIEWS.id)) {

        } else if(requestId.equals(Requests.Values.GET_ID_FAVOURITES.id)){

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.PUT_INC_VIEWS.id)) {
            propertyFragmentListener.onPropertyFragment(idProperty);
        } else if(requestId.equals(Requests.Values.GET_ID_FAVOURITES.id)){
            favouritePropertiesIds = ((ResponseFavouritesByUser) response).getFavouritesByUser().getFavourites();
        }
    }
}
