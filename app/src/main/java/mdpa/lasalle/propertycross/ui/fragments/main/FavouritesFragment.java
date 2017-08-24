package mdpa.lasalle.propertycross.ui.fragments.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import mdpa.lasalle.propertycross.data.adapter.PropertyItem;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseProperties;
import mdpa.lasalle.propertycross.ui.activities.MainActivity;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerFavourites;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerMain;

public class FavouritesFragment extends FragmentBase implements AdapterRecyclerBase.OnItemClickListener, OnMapReadyCallback {

    private RelativeLayout noSessionFavouritesLayout, favouritesLayout;
    private Button sessionFavouritesButton;
    private TextView numFavouritesTextView, emptyMapText, numberPropertiesMapText;
    private RecyclerView favouritesRecyclerView;
    private GoogleMap googleMap;

    private AdapterRecyclerFavourites adapter;
    private ArrayList<Property> properties;
    private ArrayList<PropertyFavouriteItem> propertyItems = new ArrayList<>();
    private String idProperty;

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
        getHttpManager().receiverRegister(getContext(), Requests.Values.PUT_INC_VIEWS);
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
        if (favouritesRecyclerView != null) {
            favouritesRecyclerView.setLayoutManager(linearLayoutManager);
            adapter = new AdapterRecyclerFavourites(getContext(), favouriteUpdateListener);
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
                        switch (item){
                            case 0:
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
                                        return Integer.parseInt(p1.getProperty().getMeters()) - Integer.parseInt(p2.getProperty().getMeters());
                                    }
                                });
                                break;
                            case 1:
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
                                        return Integer.parseInt(p2.getProperty().getMeters()) - Integer.parseInt(p1.getProperty().getMeters());
                                    }
                                });
                                break;
                            case 2:
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
                                        return (int)(p1.getProperty().getDistance() - p2.getProperty().getDistance());
                                    }
                                });
                                break;
                            case 3:
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
                                        return Integer.parseInt(p1.getProperty().getPrice()) - Integer.parseInt(p2.getProperty().getPrice());
                                    }
                                });
                                break;
                            case 4:
                                Collections.sort(propertyItems, new Comparator<PropertyFavouriteItem>() {
                                    @Override
                                    public int compare(PropertyFavouriteItem p1, PropertyFavouriteItem p2) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
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

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.PUT_INC_VIEWS.id)) {
            propertyFragmentListener.onPropertyFragment(idProperty);
        } else if (requestId.equals(Requests.Values.GET_FAVOURITES.id)) {
            properties = ((ResponseProperties) response).getProperties();
            String numberProperties = properties.size() + getString(R.string.properties);
            numFavouritesTextView.setText(numberProperties);
            for (int i=0; i<properties.size();i++){
                propertyItems.add(new PropertyFavouriteItem(new PropertyFavouriteItem.Property(properties.get(i).getId(),
                        properties.get(i).getCity(), Uri.parse(properties.get(i).getImages().get(0)),
                        String.valueOf(properties.get(i).getPrice()), String.valueOf(properties.get(i).getArea()), properties.get(i).getDistance(), properties.get(i).getPropertyType())));
            }
            adapter.setItems(propertyItems);
        }
    }
}
