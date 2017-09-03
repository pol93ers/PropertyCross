package mdpa.lasalle.propertycross.ui.fragments.main;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.data.Property;
import mdpa.lasalle.propertycross.data.adapter.PropertyItem;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.request.RequestEmpty;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseFavourites;
import mdpa.lasalle.propertycross.http.project.response.ResponseProperties;
import mdpa.lasalle.propertycross.ui.activities.MainActivity;
import mdpa.lasalle.propertycross.ui.activities.SearchActivity;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerProperties;

public class MainFragment extends FragmentBase implements AdapterRecyclerBase.OnItemClickListener<PropertyItem>,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener {

    private FloatingActionButton searchFAB;
    private TextView numberPropertiesText, emptyMapText, numberPropertiesMapText;
    private TabLayout propertiesTabLayout;
    private SupportMapFragment mapFragment;

    private AdapterRecyclerProperties adapter;

    private final static int RESULT_SEARCH = 1;
    private static final int PERMISSION_LOCATION = 1337;

    private ArrayList<Property> properties, propertiesSale, propertiesRental;
    private Property propertyClick;
    private ArrayList<PropertyItem> propertyItems = new ArrayList<>();
    private ArrayList<PropertyItem> propertySaleItems = new ArrayList<>();
    private ArrayList<PropertyItem> propertyRentalItems = new ArrayList<>();
    private ArrayList<Property> favouriteProperties = new ArrayList<>();
    private boolean isSearch;

    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @NonNull @Override
    public ID getComponent() {
        return ID.MainFragment;
    }

    private OnPropertyFragmentListener propertyFragmentListener;
    public interface OnPropertyFragmentListener {
        void onPropertyFragment(Property property);
    }

    private OnFavouriteUpdateListener favouriteUpdateListener;
    public interface OnFavouriteUpdateListener {
        void onFavouriteUpdate(String idProperty, boolean isMain, int positionFavourite);
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
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_FAVOURITES);
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_PROPERTIES);
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

        buildGoogleApiClient();

        View root = inflater.inflate(R.layout.fragment_main_properties, container, false);

        propertiesTabLayout = (TabLayout) root.findViewById(R.id.tabPropertiesLayout);
        searchFAB = (FloatingActionButton) root.findViewById(R.id.searchFAB);
        numberPropertiesText = (TextView) root.findViewById(R.id.numberProperties);
        RecyclerView recyclerProperties = (RecyclerView) root.findViewById(R.id.recyclerProperties);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (recyclerProperties != null) {
            recyclerProperties.setLayoutManager(linearLayoutManager);
            adapter = new AdapterRecyclerProperties(getContext(), true, favouriteUpdateListener, sessionFragmentListener);
            adapter.addOnItemClickListener(this);
            recyclerProperties.setAdapter(adapter);
        }else{
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = getChildFragmentManager().beginTransaction();
            ft.replace(R.id.mapView, mapFragment, "SupportMapFragment");
            ft.commit();

            mapFragment.getMapAsync(this);
        }
        emptyMapText = (TextView) root.findViewById(R.id.emptyMapText);
        numberPropertiesMapText = (TextView) root.findViewById(R.id.numPropertiesMap);

        setListeners();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isSearch){
            if(ApplicationPropertyCross.getInstance().preferences().getLoginApiKey() != null){
                getHttpManager().callStart(
                        Http.RequestType.GET,
                        Requests.Values.GET_FAVOURITES,
                        null,
                        null,
                        ApplicationPropertyCross.getInstance().preferences().getLoginApiKey(),
                        ApplicationPropertyCross.getInstance().preferences().getUserId(),
                        null
                );
            }else{
                getHttpManager().callStart(
                        Http.RequestType.GET,
                        Requests.Values.GET_PROPERTIES,
                        null,
                        null,
                        null,
                        null,
                        null
                );
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult arg0) {
        Toast.makeText(getContext(), "Failed to connect...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnected(Bundle arg0) {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Check Permissions
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Toast.makeText(getContext(), "Connection suspended...", Toast.LENGTH_SHORT).show();

    }

    private void getLocation(){
        if(mGoogleApiClient!= null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    if (shouldShowRequestPermissionRationale(
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showAlertDialog1bt(null,
                                getString(R.string.permission_reason, getString(R.string.app_name), getString(R.string.permission_location)),
                                getString(android.R.string.ok),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestPermissions(
                                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                                PERMISSION_LOCATION);
                                    }
                                }
                        );
                    } else {
                        showAlertDialog1bt(null,
                                getString(R.string.permission_reason_denied, getString(R.string.app_name), getString(R.string.permission_location)),
                                getString(android.R.string.ok),
                                null
                        );
                    }
                }
                break;
        }
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
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
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
                                adapter.clearItems();
                                adapter.setItems(propertyItems);
                                break;
                            case 1:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return Integer.parseInt(p2.getProperty().getMeters()) - Integer.parseInt(p1.getProperty().getMeters());
                                    }
                                });
                                adapter.clearItems();
                                adapter.setItems(propertyItems);
                                break;
                            case 2:
                                if(mLastLocation != null) {
                                    Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                        @Override
                                        public int compare(PropertyItem p1, PropertyItem p2) {
                                            Location l1 = new Location("l1");
                                            l1.setLatitude(p1.getProperty().getLatitude());
                                            l1.setLongitude(p1.getProperty().getLongitude());
                                            Location l2 = new Location("l2");
                                            l2.setLatitude(p2.getProperty().getLatitude());
                                            l2.setLongitude(p2.getProperty().getLongitude());
                                            float distance1 = l1.distanceTo(mLastLocation);
                                            float distance2 = l2.distanceTo(mLastLocation);
                                            return (int) (distance1 - distance2);
                                        }
                                    });
                                    adapter.clearItems();
                                    adapter.setItems(propertyItems);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle(R.string.enable_gps);
                                    builder.setMessage(R.string.enable_gps_message);
                                    builder.setPositiveButton(R.string.ok, null);
                                    AlertDialog dialogLocation = builder.create();
                                    dialogLocation.show();
                                }
                                break;
                            case 3:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return Integer.parseInt(p1.getProperty().getPrice()) - Integer.parseInt(p2.getProperty().getPrice());
                                    }
                                });
                                adapter.clearItems();
                                adapter.setItems(propertyItems);
                                break;
                            case 4:
                                Collections.sort(propertyItems, new Comparator<PropertyItem>() {
                                    @Override
                                    public int compare(PropertyItem p1, PropertyItem p2) {
                                        return Integer.parseInt(p2.getProperty().getPrice()) - Integer.parseInt(p1.getProperty().getPrice());
                                    }
                                });
                                adapter.clearItems();
                                adapter.setItems(propertyItems);
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

        if(propertiesTabLayout != null) {
            propertiesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    if (position == 0) {
                        if (!propertySaleItems.isEmpty()) {
                            propertyItems = propertySaleItems;
                            properties = propertiesSale;
                            String numberProperties = propertyItems.size() + " " + getString(R.string.properties);
                            numberPropertiesText.setText(numberProperties);
                            adapter.setItems(propertyItems);
                        } else {
                            numberPropertiesText.setText(R.string.results_not_found);
                            adapter.clearItems();
                        }
                    } else {
                        if (!propertyRentalItems.isEmpty()) {
                            propertyItems = propertyRentalItems;
                            properties = propertiesRental;
                            String numberProperties = propertyItems.size() + " " + getString(R.string.properties);
                            numberPropertiesText.setText(numberProperties);
                            adapter.setItems(propertyItems);
                        } else {
                            numberPropertiesText.setText(R.string.results_not_found);
                            adapter.clearItems();
                        }
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_SEARCH:
                if (resultCode == Activity.RESULT_OK) {
                    isSearch = true;
                    propertySaleItems = new ArrayList<>();
                    propertyRentalItems = new ArrayList<>();
                    propertiesSale = new ArrayList<>();
                    propertiesRental = new ArrayList<>();
                    properties = (ArrayList<Property>) data.getSerializableExtra("properties");
                    if (googleMap != null) {
                        googleMap.clear();
                    }
                    int numberProperties = properties.size();
                    for (int i=0;i<numberProperties;i++) {
                        boolean isFavourite = false;
                        if (favouriteProperties != null) {
                            for (int j=0;j<favouriteProperties.size();j++){
                                if (properties.get(i).getId().equals(favouriteProperties.get(j).getId())){
                                    isFavourite = true;
                                    break;
                                }
                            }
                        }
                        if (properties.get(i).getPropertyType().equals(getString(R.string.sale))){
                            propertiesSale.add(properties.get(i));
                            propertySaleItems.add(new PropertyItem(new PropertyItem.Property(properties.get(i).getId(),
                                    properties.get(i).getName(), properties.get(i).getAddress(), properties.get(i).getImages(),
                                    String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                                    properties.get(i).getPropertyType(), properties.get(i).getLocation().getCoordinates()[0], properties.get(i).getLocation().getCoordinates()[1], isFavourite)));
                        }else{
                            propertiesRental.add(properties.get(i));
                            propertyRentalItems.add(new PropertyItem(new PropertyItem.Property(properties.get(i).getId(),
                                    properties.get(i).getName(), properties.get(i).getAddress(), properties.get(i).getImages(),
                                    String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                                    properties.get(i).getPropertyType(), properties.get(i).getLocation().getCoordinates()[0], properties.get(i).getLocation().getCoordinates()[1], isFavourite)));
                        }

                        if(googleMap != null) {
                            LatLng latLng = new LatLng(properties.get(i).getLocation().getCoordinates()[0],
                                    properties.get(i).getLocation().getCoordinates()[1]);

                            Marker property = googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(properties.get(i).getName())
                                    .snippet(properties.get(i).getAddress()));
                            property.setTag(properties.get(i));

                            googleMap.setOnInfoWindowClickListener(this);
                        }
                    }

                    if (propertiesTabLayout.getSelectedTabPosition() == 0){
                        if (!propertySaleItems.isEmpty()){
                            propertyItems = propertySaleItems;
                            properties = propertiesSale;
                            String numProperties = propertyItems.size() + " " + getString(R.string.properties);
                            numberPropertiesText.setText(numProperties);
                            adapter.setItems(propertyItems);
                        }else{
                            numberPropertiesText.setText(R.string.results_not_found);
                            adapter.clearItems();
                        }
                    }else{
                        if (!propertyRentalItems.isEmpty()){
                            propertyItems = propertyRentalItems;
                            properties = propertiesRental;
                            String numProperties = propertyItems.size() + " " + getString(R.string.properties);
                            numberPropertiesText.setText(numProperties);
                            adapter.setItems(propertyItems);
                        }else{
                            numberPropertiesText.setText(R.string.results_not_found);
                            adapter.clearItems();
                        }
                    }

                    if (numberPropertiesMapText != null) {
                        if (numberProperties != 0){
                            numberPropertiesMapText.setVisibility(View.VISIBLE);
                            numberPropertiesMapText.setText(String.valueOf(numberProperties));
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
    public void onInfoWindowClick(Marker marker) {
        propertyFragmentListener.onPropertyFragment((Property) marker.getTag());
    }

    @Override
    public void onItemClick(PropertyItem item, int position, View rowView, int viewType) {
        propertyClick = properties.get(position);
        String idProperty = propertyItems.get(position).getProperty().getId();
        getHttpManager().callStart(
                Http.RequestType.PUT,
                Requests.Values.PUT_INC_VIEWS,
                idProperty + "/views",
                new RequestEmpty(),
                null,
                null,
                null
        );
    }

    @Override
    public void onItemLongClick(PropertyItem item, int position, View rowView, int viewType) {

    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.PUT_INC_VIEWS.id)) {

        } else if(requestId.equals(Requests.Values.GET_FAVOURITES.id)){

        } else if(requestId.equals(Requests.Values.GET_PROPERTIES.id)){

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.PUT_INC_VIEWS.id)) {
            propertyFragmentListener.onPropertyFragment(propertyClick);
        } else if(requestId.equals(Requests.Values.GET_FAVOURITES.id)){
            favouriteProperties = ((ResponseFavourites) response).getFavourites();
            getHttpManager().callStart(
                    Http.RequestType.GET,
                    Requests.Values.GET_PROPERTIES,
                    null,
                    null,
                    null,
                    null,
                    null
            );
        }else if(requestId.equals(Requests.Values.GET_PROPERTIES.id)){
            propertySaleItems = new ArrayList<>();
            propertyRentalItems = new ArrayList<>();
            propertyItems = new ArrayList<>();
            propertiesSale = new ArrayList<>();
            propertiesRental = new ArrayList<>();
            properties = ((ResponseProperties) response).getProperties();
            if (googleMap != null){
                googleMap.clear();
            }
            int numberProperties = properties.size();
            for (int i=0;i<numberProperties;i++) {
                boolean isFavourite = false;
                if (favouriteProperties != null) {
                    for (int j = 0; j < favouriteProperties.size(); j++) {
                        if (properties.get(i).getId().equals(favouriteProperties.get(j).getId())) {
                            isFavourite = true;
                            break;
                        }
                    }
                }
                if (properties.get(i).getPropertyType().equals(getString(R.string.sale))){
                    propertiesSale.add(properties.get(i));
                    propertySaleItems.add(new PropertyItem(new PropertyItem.Property(properties.get(i).getId(),
                            properties.get(i).getName(), properties.get(i).getAddress(), properties.get(i).getImages(),
                            String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                            properties.get(i).getPropertyType(), properties.get(i).getLocation().getCoordinates()[0], properties.get(i).getLocation().getCoordinates()[1], isFavourite)));
                }else{
                    propertiesRental.add(properties.get(i));
                    propertyRentalItems.add(new PropertyItem(new PropertyItem.Property(properties.get(i).getId(),
                            properties.get(i).getName(), properties.get(i).getAddress(), properties.get(i).getImages(),
                            String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                            properties.get(i).getPropertyType(), properties.get(i).getLocation().getCoordinates()[0], properties.get(i).getLocation().getCoordinates()[1], isFavourite)));
                }

                if(googleMap != null) {
                    LatLng latLng = new LatLng(properties.get(i).getLocation().getCoordinates()[0],
                            properties.get(i).getLocation().getCoordinates()[1]);

                    Marker property = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(properties.get(i).getName())
                            .snippet(properties.get(i).getAddress()));
                    property.setTag(properties.get(i));

                    googleMap.setOnInfoWindowClickListener(this);
                }
            }

            if(propertiesTabLayout != null) {
                if (propertiesTabLayout.getSelectedTabPosition() == 0) {
                    if (!propertySaleItems.isEmpty()) {
                        propertyItems = propertySaleItems;
                        properties = propertiesSale;
                        String numProperties = propertyItems.size() + " " + getString(R.string.properties);
                        numberPropertiesText.setText(numProperties);
                        adapter.setItems(propertyItems);
                    }
                } else {
                    if (!propertyRentalItems.isEmpty()) {
                        propertyItems = propertyRentalItems;
                        properties = propertiesRental;
                        String numProperties = propertyItems.size() + " " + getString(R.string.properties);
                        numberPropertiesText.setText(numProperties);
                        adapter.setItems(propertyItems);
                    }
                }
            }

            if (numberPropertiesMapText != null) {
                if (numberProperties != 0){
                    numberPropertiesMapText.setVisibility(View.VISIBLE);

                    numberPropertiesMapText.setText(numberProperties + " " + getString(R.string.properties));
                    emptyMapText.setVisibility(View.GONE);
                }else{
                    numberPropertiesMapText.setVisibility(View.GONE);
                    emptyMapText.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
