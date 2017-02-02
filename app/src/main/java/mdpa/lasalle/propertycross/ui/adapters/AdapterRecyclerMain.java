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

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;



public class AdapterRecyclerMain<I extends AdapterRecyclerMain.PropertyItem> extends AdapterRecyclerBase<I, AdapterRecyclerBase.BindableViewHolder<I>>{

    private Context context;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.AdapterRecyclerMain;
    }

    public AdapterRecyclerMain(){

    }

    public interface PropertyItem<P extends PropertyItem.Property>{
        @NonNull P getProperty();

        interface Property{
            @NonNull Uri getPhoto();
            @NonNull String getAddress();
            @NonNull String getMeters();
            @NonNull String getPrice();
            @NonNull boolean isFavourite();
            @NonNull void setFavourite(boolean favourite);
        }
    }

    @NonNull
    @Override
    protected BindableViewHolder onCreateViewHolder(Context context, LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BindableViewHolder<>(inflater.inflate(R.layout.property_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder<I> holder, int position) {
        super.onBindViewHolder(holder, position);
        Glide.with(context).load(holder.item.getProperty().getPhoto()).into((ImageView) holder.itemView.findViewById(R.id.propertyImage));
        ((TextView)holder.itemView.findViewById(R.id.propertyAddressText)).setText(holder.item.getProperty().getAddress());
        ((TextView)holder.itemView.findViewById(R.id.propertyMetersText)).setText(holder.item.getProperty().getMeters());
        ((TextView)holder.itemView.findViewById(R.id.propertyPriceText)).setText(holder.item.getProperty().getPrice());
        if(holder.item.getProperty().isFavourite()){
            ((ImageView)holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            ((ImageView)holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        holder.itemView.findViewById(R.id.propertyFavouriteImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.item.getProperty().setFavourite(!holder.item.getProperty().isFavourite());
                if(holder.item.getProperty().isFavourite()){
                    ((ImageView)holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_black_24dp);
                }else{
                    ((ImageView)holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });
    }
}
