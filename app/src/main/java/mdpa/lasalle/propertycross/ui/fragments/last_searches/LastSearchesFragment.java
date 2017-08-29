package mdpa.lasalle.propertycross.ui.fragments.last_searches;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.data.adapter.LastSearchItem;
import mdpa.lasalle.propertycross.services.GeocoderIntentService;
import mdpa.lasalle.propertycross.ui.adapters.AdapterLastSearches;

public class LastSearchesFragment extends FragmentBase implements AdapterRecyclerBase.OnItemClickListener{

    private EditText searchEditText;
    private ImageView searchImage;
    private LinearLayout myLocationLayout;

    private AdapterLastSearches adapterLastSearches;
    private String location;
    private double latitude, longitude;
    private ArrayList<String> lastSearches = new ArrayList<>();
    private ArrayList<LastSearchItem> lastSearchItems = new ArrayList<>();

    private static final int PERMISSION_LOCATION = 1337;

    @NonNull @Override
    public ID getComponent() {
        return ID.LastSearchesFragment;
    }

    public static LastSearchesFragment newInstance() {
        return new LastSearchesFragment();
    }

    private OnLastSearchesListener listener;
    public interface OnLastSearchesListener {
        void onSearch(String search, boolean isLocation, double latitude, double longitude);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = onAttachGetListener(OnLastSearchesListener.class, context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_last_search, container, false);
        setHasOptionsMenu(true);

        searchEditText = (EditText) root.findViewById(R.id.searchEditText);
        myLocationLayout = (LinearLayout) root.findViewById(R.id.myLocationLayout);
        searchImage = (ImageView) root.findViewById(R.id.searchImageView);
        RecyclerView lastSearchesRecycler = (RecyclerView) root.findViewById(R.id.lastSearchesRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lastSearchesRecycler.setLayoutManager(linearLayoutManager);
        adapterLastSearches = new AdapterLastSearches();
        lastSearchesRecycler.setAdapter(adapterLastSearches);

        setListeners();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        onGetLocation();
        if(ApplicationPropertyCross.getInstance().preferences().getLastSearches() != null) {
            lastSearches = new ArrayList<>(Arrays.asList(ApplicationPropertyCross.getInstance().preferences().getLastSearches().split(",")));
            int count = 0;
            for(int i=0; i<lastSearches.size(); i++){
                if (count <= 5){
                    String lastSearchText = lastSearches.get(i);
                    lastSearchText = lastSearchText.replace("[", "").replace("]","");
                    if (lastSearchText.charAt(0) == ' ') lastSearchText = lastSearchText.substring(1, lastSearchText.length());
                    lastSearchItems.add(new LastSearchItem(new LastSearchItem.LastSearch(lastSearchText)));
                    count++;
                }else{
                    break;
                }
            }
            adapterLastSearches.setItems(lastSearchItems);
        }
    }

    private void setListeners(){
        myLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSearch(location, true, latitude, longitude);
            }
        });

        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(searchEditText)){
                    String searchText = searchEditText.getText().toString();
                    lastSearches.add(searchText);
                    ApplicationPropertyCross.getInstance().preferences().setLastSearches(lastSearches.toString());
                    listener.onSearch(searchText, false, 0, 0);
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.error);
                    builder.setMessage(R.string.empty_fields);
                    builder.setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private void onGetLocation() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
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
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
            }
        } else {
            getLocation();
        }
    }

    private void getLocation(){
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        // getting network status
        boolean isNetworkEnabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location gpsLocation;
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Check Permissions
        }
        if (!isGPSEnabled && !isNetworkEnabled){

        } else if (isGPSEnabled) {
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (gpsLocation != null) {
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();

                List<Address> addresses;
                addresses = GeocoderIntentService.getAddressesFromLocation(geocoder, gpsLocation, 4);
                if (addresses != null && addresses.size() != 0) {
                    location = addresses.get(0).getLocality();
                }
            }
        } else {
            gpsLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (gpsLocation != null) {
                latitude = gpsLocation.getLatitude();
                longitude = gpsLocation.getLongitude();

                List<Address> addresses;
                addresses = GeocoderIntentService.getAddressesFromLocation(geocoder, gpsLocation, 4);
                if (addresses != null && addresses.size() != 0) {
                    location = addresses.get(0).getLocality();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
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
    public void onItemClick(Object item, int position, View rowView, int viewType) {
        listener.onSearch(lastSearches.get(position), false, 0, 0);
    }

    @Override
    public void onItemLongClick(Object item, int position, View rowView, int viewType) {

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

    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().length() == 0;
    }
}
