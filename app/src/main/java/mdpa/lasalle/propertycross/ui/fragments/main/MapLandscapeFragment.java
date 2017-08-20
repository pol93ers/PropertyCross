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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;


public class MapLandscapeFragment extends FragmentBase implements OnMapReadyCallback{

    private TextView numPropertiesText, emptyMapText;
    private GoogleMap googleMap;

    private int numberProperties;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.MapLandscapeFragment;
    }

    public static MapLandscapeFragment newInstance(int numberProperties) {
        MapLandscapeFragment fragment = new MapLandscapeFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("numberProperties",numberProperties);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        numberProperties = getArguments().getInt("numberProperties");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        numPropertiesText = (TextView) root.findViewById(R.id.numPropertiesMap);
        if (numberProperties != 0){
            numPropertiesText.setText(numberProperties);
            emptyMapText.setVisibility(View.GONE);
        }else{
            numPropertiesText.setVisibility(View.GONE);
        }

        emptyMapText = (TextView) root.findViewById(R.id.emptyMapText);

        MapView mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.getMapAsync(this);

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

}
