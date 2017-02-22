package mdpa.lasalle.propertycross.ui.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;

public class PropertyFragment extends FragmentBase{

    private ViewPager photosPager;
    private TextView numVisitsTextView, addressTextView, metersTextView, priceTextView, typeTextView, descriptionTextView;
    private ImageView shareImageView;
    private Button mapButton;

    private OnMapPropertyFragmentListener mapPropertyFragmentListener;
    public interface OnMapPropertyFragmentListener {
        void onMapPropertyFragment();
    }

    @NonNull
    @Override
    public ID getComponent() {
        return ID.PropertyFragment;
    }

    public static PropertyFragment newInstance() {
        return new PropertyFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mapPropertyFragmentListener = onAttachGetListener(OnMapPropertyFragmentListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_property, container, false);

        photosPager = (ViewPager) root.findViewById(R.id.photosPropertyPager);
        numVisitsTextView = (TextView) root.findViewById(R.id.numVisitsText);
        addressTextView = (TextView) root.findViewById(R.id.addressText);
        metersTextView = (TextView) root.findViewById(R.id.metersText);
        priceTextView = (TextView) root.findViewById(R.id.priceText);
        typeTextView = (TextView) root.findViewById(R.id.typeText);
        descriptionTextView = (TextView) root.findViewById(R.id.descriptionText);
        shareImageView = (ImageView) root.findViewById(R.id.shareImage);
        mapButton = (Button) root.findViewById(R.id.viewMapButton);

        setListeners();

        return root;
    }

    private void setListeners(){
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPropertyFragmentListener.onMapPropertyFragment();
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getText(R.string.share_text));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getText(R.string.share)));
            }
        });
    }
}
