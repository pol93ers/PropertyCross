package mdpa.lasalle.propertycross.ui.fragments.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.fragment.FragmentBase;
import mdpa.lasalle.propertycross.data.Comment;
import mdpa.lasalle.propertycross.data.Property;
import mdpa.lasalle.propertycross.data.adapter.CommentItem;
import mdpa.lasalle.propertycross.http.Http;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.http.project.request.RequestEmpty;
import mdpa.lasalle.propertycross.http.project.response.Response;
import mdpa.lasalle.propertycross.http.project.response.ResponseComments;
import mdpa.lasalle.propertycross.http.project.response.ResponseError;
import mdpa.lasalle.propertycross.http.project.response.ResponseGeneric;
import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerComments;

import static android.content.Context.TELEPHONY_SERVICE;

public class PropertyFragment extends FragmentBase implements ViewPager.OnPageChangeListener{

    private TextView numVisitsTextView, nameTextView, addressTextView, cityTextView, metersTextView,
            priceTextView, typeTextView, descriptionTextView, noCommentsTextView;
    private ImageView shareImageView, favouriteImageView;
    private Button mapButton, callButton, mailButton, addCommentButton;
    private RecyclerView commentsRecycler;
    private AdapterRecyclerComments adapterRecyclerComments;

    private Property property;
    private boolean isFavourite;

    private OnMapPropertyFragmentListener mapPropertyFragmentListener;
    public interface OnMapPropertyFragmentListener {
        void onMapPropertyFragment(double latitude, double longitude);
    }

    private OnCommentFragmentListener commentFragmentListener;
    public interface OnCommentFragmentListener {
        void onCommentFragment(String idProperty);
    }

    @NonNull
    @Override
    public ID getComponent() {
        return ID.PropertyFragment;
    }

    public static PropertyFragment newInstance(Property property) {
        PropertyFragment fragment = new PropertyFragment();
        Bundle args = new Bundle();
        args.putSerializable("property", property);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mapPropertyFragmentListener = onAttachGetListener(OnMapPropertyFragmentListener.class, context);
        commentFragmentListener = onAttachGetListener(OnCommentFragmentListener.class, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_COMMENTS_PROPERTY);
        getHttpManager().receiverRegister(getContext(), Requests.Values.PUT_ADD_FAVOURITE);
        getHttpManager().receiverRegister(getContext(), Requests.Values.GET_IS_FAVOURITE);
        property = (Property)getArguments().getSerializable("property");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHttpManager().receiverUnregister(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_property, container, false);
        setHasOptionsMenu(true);

        ViewPager photosPager = (ViewPager) root.findViewById(R.id.photosPropertyPager);
        PagerImagesAdapter adapter = new PagerImagesAdapter(getContext());
        photosPager.setAdapter(adapter);
        photosPager.addOnPageChangeListener(this);
        numVisitsTextView = (TextView) root.findViewById(R.id.numVisitsText);
        nameTextView = (TextView) root.findViewById(R.id.namePropertyText);
        cityTextView = (TextView) root.findViewById(R.id.cityPropertyText);
        addressTextView = (TextView) root.findViewById(R.id.addressText);
        metersTextView = (TextView) root.findViewById(R.id.metersText);
        priceTextView = (TextView) root.findViewById(R.id.priceText);
        typeTextView = (TextView) root.findViewById(R.id.typeText);
        descriptionTextView = (TextView) root.findViewById(R.id.descriptionText);
        shareImageView = (ImageView) root.findViewById(R.id.shareImage);
        mapButton = (Button) root.findViewById(R.id.viewMapButton);
        favouriteImageView = (ImageView) root.findViewById(R.id.favouritePropertyImage);
        callButton = (Button) root.findViewById(R.id.callButton);
        mailButton = (Button) root.findViewById(R.id.mailButton);
        noCommentsTextView = (TextView) root.findViewById(R.id.noCommentText);
        addCommentButton = (Button) root.findViewById(R.id.addCommentButton);
        commentsRecycler = (RecyclerView) root.findViewById(R.id.commentsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        commentsRecycler.setLayoutManager(linearLayoutManager);
        adapterRecyclerComments = new AdapterRecyclerComments(getContext());
        commentsRecycler.setAdapter(adapterRecyclerComments);

        setViews();
        setListeners();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        getHttpManager().callStart(
                Http.RequestType.GET,
                Requests.Values.GET_COMMENTS_PROPERTY,
                property.getId() + "/comments",
                null,
                null,
                null,
                null
        );

        if (ApplicationPropertyCross.getInstance().preferences().getLoginApiKey() != null){
            getHttpManager().callStart(
                    Http.RequestType.GET,
                    Requests.Values.GET_IS_FAVOURITE,
                    property.getId(),
                    null,
                    ApplicationPropertyCross.getInstance().preferences().getLoginApiKey(),
                    ApplicationPropertyCross.getInstance().preferences().getUserId(),
                    null
            );
        }
    }

    private void setViews(){
        addressTextView.setText(property.getAddress());
        nameTextView.setText(property.getName());
        cityTextView.setText(property.getCity());
        String visits = property.getViews() + " " + getString(R.string.views);
        numVisitsTextView.setText(visits);
        String price = property.getPrice() + "€";
        priceTextView.setText(price);
        String area = property.getArea() + "m2";
        metersTextView.setText(area);
        descriptionTextView.setText(property.getDescription());
        typeTextView.setText(property.getPropertyType());
    }

    private void setListeners(){
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPropertyFragmentListener.onMapPropertyFragment(property.getLocation().getCoordinates()[0],
                        property.getLocation().getCoordinates()[1]);
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

        favouriteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHttpManager().callStart(
                        Http.RequestType.PUT,
                        Requests.Values.PUT_ADD_FAVOURITE,
                        property.getId(),
                        new RequestEmpty(),
                        ApplicationPropertyCross.getInstance().preferences().getLoginApiKey(),
                        ApplicationPropertyCross.getInstance().preferences().getUserId(),
                        null
                );
            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTelephonyEnabled()) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + property.getUser().getPhone()));
                    startActivity(intent);
                }
            }
        });

        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{property.getUser().getMail()});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_owner));

                startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentFragmentListener.onCommentFragment(property.getId());
            }
        });
    }

    private boolean isTelephonyEnabled(){
        TelephonyManager telephonyManager = (TelephonyManager)getContext().getSystemService(TELEPHONY_SERVICE);
        return telephonyManager != null && telephonyManager.getSimState()==TelephonyManager.SIM_STATE_READY;
    }

    private class PagerImagesAdapter extends PagerAdapter {

        private Context context;

        private PagerImagesAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return property.getImages().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(context);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.property_image_view, container, false);
            ImageView propertyImage = (ImageView) layout.findViewById(R.id.propertyItemImage);
            Glide.with(getContext()).load(Requests.getBaseServerUrl() + property.getImages().get(position)).into(propertyImage);
            container.addView(layout);
            return layout;
        }

        @Override
        public void	destroyItem(ViewGroup container, int position, Object view){
            container.removeView((View) view);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {}

    @Override
    public void onHttpBroadcastError(String requestId, ResponseError response) {
        super.onHttpBroadcastError(requestId, response);
        if (requestId.equals(Requests.Values.PUT_ADD_FAVOURITE.id)) {

        } else if(requestId.equals(Requests.Values.GET_IS_FAVOURITE.id)){

        } else if(requestId.equals(Requests.Values.GET_COMMENTS_PROPERTY.id)){

        }
    }

    @Override
    public void onHttpBroadcastSuccess(String requestId, Response response) {
        super.onHttpBroadcastSuccess(requestId, response);
        if (requestId.equals(Requests.Values.PUT_ADD_FAVOURITE.id)) {
            isFavourite = !isFavourite;
            if (isFavourite){
                favouriteImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
            }else{
                favouriteImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
        } else if(requestId.equals(Requests.Values.GET_IS_FAVOURITE.id)){
            String favourite = ((ResponseGeneric)response).getMessage();
            if(favourite.equals(getString(R.string.favourite))){
                isFavourite = true;
            }

            if (isFavourite){
                favouriteImageView.setImageResource(R.drawable.ic_favorite_black_24dp);
            }else{
                favouriteImageView.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
        } else if(requestId.equals(Requests.Values.GET_COMMENTS_PROPERTY.id)){
            ArrayList<Comment> comments = ((ResponseComments) response).getComments();
            if(comments.size() > 0){
                noCommentsTextView.setVisibility(View.GONE);
                commentsRecycler.setVisibility(View.VISIBLE);
                ArrayList<CommentItem> commentItems = new ArrayList<>();
                for (int i=0; i<comments.size(); i++){
                    commentItems.add(new CommentItem(new CommentItem.Comment(comments.get(i).getUser().getUsername(),
                            comments.get(i).getContent(),comments.get(i).getTime())));
                }
                adapterRecyclerComments.setItems(commentItems);
            }else{
                noCommentsTextView.setVisibility(View.VISIBLE);
                commentsRecycler.setVisibility(View.GONE);
            }
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
