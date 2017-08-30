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

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.http.project.Requests;
import mdpa.lasalle.propertycross.ui.fragments.main.FavouritesFragment;

public class AdapterRecyclerFavourites<I extends AdapterRecyclerFavourites.PropertyItem> extends AdapterRecyclerBase<I, AdapterRecyclerBase.BindableViewHolder<I>>{

    private Context context;
    private FavouritesFragment.OnFavouriteUpdateListener listener;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.AdapterRecyclerFavourites;
    }

    public AdapterRecyclerFavourites(Context context, FavouritesFragment.OnFavouriteUpdateListener listener){
        this.context = context;
        this.listener = listener;
    }

    public interface PropertyItem<P extends PropertyItem.Property>{
        @NonNull P getProperty();

        interface Property{
            @NonNull ArrayList<String> getPhotos();
            @NonNull String getId();
            @NonNull String getName();
            @NonNull String getAddress();
            @NonNull String getMeters();
            @NonNull String getPrice();
            double getLatitude();
            double getLongitude();
            @NonNull String getType();
        }
    }

    @NonNull
    @Override
    protected BindableViewHolder<I> onCreateViewHolder(Context context, LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BindableViewHolder<>(inflater.inflate(R.layout.property_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder<I> holder, int position) {
        super.onBindViewHolder(holder, position);
        Glide.with(context).load(Uri.parse(Requests.getBaseServerUrl() + holder.item.getProperty().getPhotos().get(0))).into((ImageView) holder.itemView.findViewById(R.id.propertyImage));
        ((TextView)holder.itemView.findViewById(R.id.propertyNameText)).setText(holder.item.getProperty().getName());
        ((TextView)holder.itemView.findViewById(R.id.propertyAddressText)).setText(holder.item.getProperty().getAddress());
        ((TextView)holder.itemView.findViewById(R.id.propertyMetersText)).setText(holder.item.getProperty().getMeters());
        String meters = holder.item.getProperty().getMeters() + "m2";
        ((TextView)holder.itemView.findViewById(R.id.propertyMetersText)).setText(meters);
        String price = holder.item.getProperty().getPrice() + "€";
        ((TextView)holder.itemView.findViewById(R.id.propertyPriceText)).setText(price);
        ((TextView)holder.itemView.findViewById(R.id.propertyTypeText)).setText(holder.item.getProperty().getType());
        ((ImageView)holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_black_24dp);

        holder.itemView.findViewById(R.id.propertyFavouriteImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onFavouriteUpdate(holder.item.getProperty().getId(), false);
            }
        });
    }

}
