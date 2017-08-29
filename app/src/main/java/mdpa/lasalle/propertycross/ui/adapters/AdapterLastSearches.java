package mdpa.lasalle.propertycross.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;

public class AdapterLastSearches <I extends AdapterLastSearches.LastSearchItem> extends AdapterRecyclerBase<I, AdapterRecyclerBase.BindableViewHolder<I>>{

    @NonNull @Override
    public ID getComponent() {
        return ID.AdapterLastSearches;
    }

    public interface LastSearchItem<P extends LastSearchItem.LastSearch>{
        @NonNull P getLastSearch();

        interface LastSearch{
            @NonNull String getLastSearch();
        }
    }

    @NonNull
    @Override
    protected BindableViewHolder<I> onCreateViewHolder(Context context, LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BindableViewHolder<>(inflater.inflate(R.layout.last_search_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder<I> holder, int position) {
        super.onBindViewHolder(holder, position);
        ((TextView)holder.itemView.findViewById(R.id.lastSearchText)).setText(holder.item.getLastSearch().getLastSearch());
    }

}
