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

import mdpa.lasalle.propertycross.ApplicationPropertyCross;
import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;
import mdpa.lasalle.propertycross.ui.fragments.main.MainFragment;


public class AdapterRecyclerMain<I extends AdapterRecyclerMain.PropertyItem> extends AdapterRecyclerBase<I, AdapterRecyclerBase.BindableViewHolder<I>>{

    private Context context;
    private MainFragment.OnFavouriteUpdateListener listener;
    private MainFragment.OnSessionFragmentListener listenerSession;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.AdapterRecyclerMain;
    }

    public AdapterRecyclerMain(Context context, MainFragment.OnFavouriteUpdateListener listener, MainFragment.OnSessionFragmentListener listenerSession){
        this.context = context;
        this.listener = listener;
        this.listenerSession = listenerSession;
    }

    public interface PropertyItem<P extends PropertyItem.Property>{
        @NonNull P getProperty();

        interface Property{
            @NonNull String getId();
            @NonNull Uri getPhoto();
            @NonNull String getAddress();
            @NonNull String getMeters();
            @NonNull String getPrice();
            @NonNull String getType();
            double getDistance();
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
                if(ApplicationPropertyCross.getInstance().preferences().getLoginApiKey() != null) {
                    holder.item.getProperty().setFavourite(!holder.item.getProperty().isFavourite());
                    if (holder.item.getProperty().isFavourite()) {
                        ((ImageView) holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_black_24dp);
                    } else {
                        ((ImageView) holder.itemView.findViewById(R.id.propertyFavouriteImage)).setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    listener.onFavouriteUpdate(holder.item.getProperty().getId(), true);
                }else{
                    listenerSession.onLoginActivity();
                }
            }
        });
    }
}
