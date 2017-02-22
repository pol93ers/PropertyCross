package mdpa.lasalle.propertycross.ui.fragments.main;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;

public class MapFragment extends FragmentBase implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private TextView numPropertiesText, emptyMapText;
    private MapView mapView;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.MapFragment;
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

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
        LatLng location = new LatLng(-23,31);

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
}
