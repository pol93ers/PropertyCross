package mdpa.lasalle.propertycross.ui.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.ui.fragments.main.FavouritesFragment;
import mdpa.lasalle.propertycross.ui.fragments.main.MainFragment;


public class AdapterRecyclerProperties<I extends AdapterRecyclerProperties.PropertyItem> extends AdapterRecyclerBase<I, AdapterRecyclerBase.BindableViewHolder<I>>{

    private Context context;
    private boolean isMain;
    private MainFragment.OnFavouriteUpdateListener listener;
    private FavouritesFragment.OnFavouriteUpdateListener favouriteListener;
    private MainFragment.OnSessionFragmentListener listenerSession;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.AdapterRecyclerMain;
    }

    public AdapterRecyclerProperties(Context context, boolean isMain, MainFragment.OnFavouriteUpdateListener listener, MainFragment.OnSessionFragmentListener listenerSession){
        this.context = context;
        this.isMain = isMain;
        this.listener = listener;
        this.listenerSession = listenerSession;
    }

    public AdapterRecyclerProperties(Context context, boolean isMain, FavouritesFragment.OnFavouriteUpdateListener listener){
        this.context = context;
        this.isMain = isMain;
        this.favouriteListener = listener;
    }

    public interface PropertyItem<P extends PropertyItem.Property>{
        @NonNull P getProperty();

        interface Property{
            @NonNull String getId();
            @NonNull ArrayList<String> getPhotos();
            @NonNull String getName();
            @NonNull String getAddress();
            @NonNull String getMeters();
            @NonNull String getPrice();
            @NonNull String getType();
            double getLatitude();
            double getLongitude();
            boolean isFavourite();
            void setFavourite(boolean favourite);
        }
    }

    @NonNull
    @Override
    protected BindableViewHolder<I> onCreateViewHolder(Context context, LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BindableViewHolder<>(inflater.inflate(R.layout.property_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder<I> holder, final int position) {
        super.onBindViewHolder(holder, position);
        Glide.with(context).load(Uri.parse(Requests.getBaseServerUrl() + holder.item.getProperty().getPhotos().get(0))).into((ImageView) holder.itemView.findViewById(R.id.propertyImage));
        ((TextView)holder.itemView.findViewById(R.id.propertyNameText)).setText(holder.item.getProperty().getName());
        ((TextView)holder.itemView.findViewById(R.id.propertyAddressText)).setText(holder.item.getProperty().getAddress());
        String meters = holder.item.getProperty().getMeters() + "m2";
        ((TextView)holder.itemView.findViewById(R.id.propertyMetersText)).setText(meters);
        String price = holder.item.getProperty().getPrice() + "€";
        ((TextView)holder.itemView.findViewById(R.id.propertyPriceText)).setText(price);
        ((TextView)holder.itemView.findViewById(R.id.propertyTypeText)).setText(holder.item.getProperty().getType());
        if(holder.item.getProperty().isFavourite()){
            ((ImageView)holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            ((ImageView)holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        holder.itemView.findViewById(R.id.propertyFavouriteImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMain) {
                    if (ApplicationPropertyCross.getInstance().preferences().getLoginApiKey() != null) {
                        holder.item.getProperty().setFavourite(!holder.item.getProperty().isFavourite());
                        if (holder.item.getProperty().isFavourite()) {
                            ((ImageView) holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_black_24dp);
                        } else {
                            ((ImageView) holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        }
                        listener.onFavouriteUpdate(holder.item.getProperty().getId(), true, position);
                    } else {
                        listenerSession.onLoginActivity();
                    }
                }else{
                    favouriteListener.onFavouriteUpdate(holder.item.getProperty().getId(), false, position);
                }
            }
        });
    }
}
