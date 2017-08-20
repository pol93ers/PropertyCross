package mdpa.lasalle.propertycross.ui.fragments.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;

public class MapFragment extends FragmentBase implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private TextView numPropertiesText, emptyMapText;
    private MapView mapView;

    private double latitude, longitude;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.MapFragment;
    }

    public static MapFragment newInstance(double latitude, double longitude) {
        MapFragment fragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latitude = getArguments().getDouble("latitude");
        longitude = getArguments().getDouble("longitude");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        setHasOptionsMenu(true);

        numPropertiesText = (TextView) root.findViewById(R.id.numPropertiesMap);
        emptyMapText = (TextView) root.findViewById(R.id.emptyMapText);
        numPropertiesText.setVisibility(View.GONE);
        emptyMapText.setVisibility(View.GONE);

        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        try{
            MapsInitializer.initialize(getContext());
        }catch (Exception e){
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng markerLocation = new LatLng(latitude, longitude);
        googleMap.clear();

        googleMap.addMarker(new MarkerOptions()
                .position(markerLocation)
        );
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(
                markerLocation
        ));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mapView != null){
            mapView.onDestroy();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mapView != null){
            mapView.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mapView != null){
            mapView.onStop();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mapView != null){
            mapView.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mapView != null){
            mapView.onResume();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mapView != null){
            mapView.onSaveInstanceState(outState);
        }
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
}
