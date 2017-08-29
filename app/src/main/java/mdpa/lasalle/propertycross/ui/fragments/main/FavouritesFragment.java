package mdpa.lasalle.propertycross.ui.fragments.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.data.Property;
import mdpa.lasalle.propertycross.data.adapter.PropertyFavouriteItem;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseFavourites;
import mdpa.lasalle.propertycross.ui.activities.MainActivity;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerFavourites;

public class FavouritesFragment extends FragmentBase implements AdapterRecyclerBase.OnItemClickListener<PropertyFavouriteItem>, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private RelativeLayout noSessionFavouritesLayout, favouritesLayout;
    private Button sessionFavouritesButton;
    private TextView numFavouritesTextView, emptyMapText, numberPropertiesMapText;
    private TabLayout favouritesTabLayout;

    private AdapterRecyclerFavourites adapter;
    private ArrayList<PropertyFavouriteItem> propertyItems = new ArrayList<>();
    private ArrayList<PropertyFavouriteItem> propertySaleItems = new ArrayList<>();
    private ArrayList<PropertyFavouriteItem> propertyRentalItems = new ArrayList<>();
    private String idProperty;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static final int PERMISSION_LOCATION = 1337;

    private OnLoginActivityListener loginActivityListener;
    public interface OnLoginActivityListener {
        void onLoginActivity();
    }

    private OnPropertyFragmentListener propertyFragmentListener;
    public interface OnPropertyFragmentListener {
        void onPropertyFragment(String idProperty);
    }

    private OnFavouriteUpdateListener favouriteUpdateListener;
    public interface OnFavouriteUpdateListener {
        void onFavouriteUpdate(String idProperty, boolean isMain);
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
        favouriteUpdateListener = onAttachGetListener(OnFavouriteUpdateListener.class, context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_FAVOURITES);
        getHttpManager().receiverRegister(getContext(), Requests.Values.POST_INC_VIEWS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(R.string.favourites);
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((MainActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(false);
        setHasOptionsMenu(true);

        View root = inflater.inflate(R.layout.fragment_main_favourites, container, false);

        buildGoogleApiClient();

        favouritesTabLayout = (TabLayout) root.findViewById(R.id.tabFavouritesLayout);
        noSessionFavouritesLayout = (RelativeLayout) root.findViewById(R.id.noSessionFavouritesLayout);
        favouritesLayout = (RelativeLayout) root.findViewById(R.id.favouritesLayout);
        sessionFavouritesButton = (Button) root.findViewById(R.id.sessionFavouritesButton);
        numFavouritesTextView = (TextView) root.findViewById(R.id.numberPropertiesFavourites);
        RecyclerView favouritesRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerFavourites);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (favouritesRecyclerView != null) {
            favouritesRecyclerView.setLayoutManager(linearLayoutManager);
            adapter = new AdapterRecyclerFavourites(getContext(), favouriteUpdateListener);
            adapter.addOnItemClickListener(this);
            favouritesRecyclerView.setAdapter(adapter);
        }else{
            MapView mapView = (MapView) root.findViewById(R.id.mapView);
            mapView.getMapAsync(this);
        }

        emptyMapText = (TextView) root.findViewById(R.id.emptyMapText);
        numberPropertiesMapText = (TextView) root.findViewById(R.id.numPropertiesMap);

        setListeners();

        return root;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

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
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            }
        } else {
            getLocation();
        }
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
    public void onStart() {
        super.onStart();
        if(ApplicationPropertyCross.getInstance().preferences().getLoginApiKey() != null){
            noSessionFavouritesLayout.setVisibility(View.GONE);
            favouritesLayout.setVisibility(View.VISIBLE);

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

        favouritesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0){
                    if (!propertySaleItems.isEmpty()){
                        propertyItems = propertySaleItems;
                        String numberProperties = propertyItems.size() + " " + getString(R.string.properties);
                        numFavouritesTextView.setText(numberProperties);
                        adapter.setItems(propertyItems);
                    }else{
                        numFavouritesTextView.setText(R.string.results_not_found);
                        adapter.clearItems();
                    }
                }else{
                    if (!propertyRentalItems.isEmpty()){
                        propertyItems = propertyRentalItems;
                        String numberProperties = propertyItems.size() + " " + getString(R.string.properties);
                        numFavouritesTextView.setText(numberProperties);
                        adapter.setItems(propertyItems);
                    }else{
                        numFavouritesTextView.setText(R.string.results_not_found);
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
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
                                        return Integer.parseInt(p1.getProperty().getMeters()) - Integer.parseInt(p2.getProperty().getMeters());
                                    }
                                });
                                adapter.clearItems();
                                adapter.setItems(propertyItems);
                                break;
                            case 1:
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
                                        return Integer.parseInt(p2.getProperty().getMeters()) - Integer.parseInt(p1.getProperty().getMeters());
                                    }
                                });
                                adapter.clearItems();
                                adapter.setItems(propertyItems);
                                break;
                            case 2:
                                if(mLastLocation != null) {
                                    Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                        @Override
                                        public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
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
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
                                        return Integer.parseInt(p1.getProperty().getPrice()) - Integer.parseInt(p2.getProperty().getPrice());
                                    }
                                });
                                adapter.clearItems();
                                adapter.setItems(propertyItems);
                                break;
                            case 4:
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
    }

    @Override
    public void onItemClick(PropertyFavouriteItem item, int position, View rowView, int viewType) {
        idProperty = propertyItems.get(position).getProperty().getId();
        getHttpManager().callStart(
                Http.RequestType.POST,
                Requests.Values.POST_INC_VIEWS,
                idProperty + "/views",
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onItemLongClick(PropertyFavouriteItem item, int position, View rowView, int viewType) {

    }

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.POST_INC_VIEWS.id)) {

        } else if (requestId.equals(Requests.Values.GET_FAVOURITES.id)) {

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.POST_INC_VIEWS.id)) {
            propertyFragmentListener.onPropertyFragment(idProperty);
        } else if (requestId.equals(Requests.Values.GET_FAVOURITES.id)) {
            propertySaleItems = new ArrayList<>();
            propertyRentalItems = new ArrayList<>();
            ArrayList<Property> properties = ((ResponseFavourites) response).getFavourites();
            if (properties.size() > 0) {
                String numberProperties = properties.size() + getString(R.string.properties);
                numFavouritesTextView.setText(numberProperties);
                for (int i = 0; i < properties.size(); i++) {
                    if (properties.get(i).getPropertyType().equals(getString(R.string.sale))){
                        propertySaleItems.add(new PropertyFavouriteItem(new PropertyFavouriteItem.Property(properties.get(i).getId(),
                                properties.get(i).getAddress(), properties.get(i).getImages(),
                                String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                                properties.get(i).getLocation().getLatitude(), properties.get(i).getLocation().getLongitude(),
                                properties.get(i).getPropertyType())));
                    }else{
                        propertyRentalItems.add(new PropertyFavouriteItem(new PropertyFavouriteItem.Property(properties.get(i).getId(),
                                properties.get(i).getAddress(), properties.get(i).getImages(),
                                String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()),
                                properties.get(i).getLocation().getLatitude(), properties.get(i).getLocation().getLongitude(),
                                properties.get(i).getPropertyType())));
                    }

                }
            }

            if (favouritesTabLayout.getSelectedTabPosition() == 0){
                if (!propertySaleItems.isEmpty()){
                    propertyItems = propertySaleItems;
                    String numProperties = propertyItems.size() + " " + getString(R.string.properties);
                    numFavouritesTextView.setText(numProperties);
                    adapter.setItems(propertyItems);
                }
            }else{
                if (!propertyRentalItems.isEmpty()){
                    propertyItems = propertyRentalItems;
                    String numProperties = propertyItems.size() + " " + getString(R.string.properties);
                    numFavouritesTextView.setText(numProperties);
                    adapter.setItems(propertyItems);
                }
            }

            if (numberPropertiesMapText != null) {
                if (properties.size() != 0){
                    numberPropertiesMapText.setText(String.valueOf(properties.size()));
                    emptyMapText.setVisibility(View.GONE);
                }else{
                    numberPropertiesMapText.setVisibility(View.GONE);
                    emptyMapText.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
