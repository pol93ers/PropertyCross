package mdpa.lasalle.propertycross.ui.fragments.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;

public class MapFragment extends FragmentBase implements OnMapReadyCallback {

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

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.mapView, mapFragment, "SupportMapFragment");
        ft.commit();

        mapFragment.getMapAsync(this);

        return root;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        LatLng markerLocation = new LatLng(latitude, longitude);
        googleMap.clear();

        googleMap.addMarker(new MarkerOptions()
                .position(markerLocation)
        );
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                markerLocation, 15
        ));
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
