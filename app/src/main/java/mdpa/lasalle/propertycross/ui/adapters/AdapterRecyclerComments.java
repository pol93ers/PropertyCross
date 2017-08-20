package mdpa.lasalle.propertycross.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;


public class AdapterRecyclerComments<I extends AdapterRecyclerComments.CommentsItem> extends AdapterRecyclerBase<I, AdapterRecyclerBase.BindableViewHolder<I>>{

    private Context context;

    @NonNull
    @Override
    public ID getComponent() {
        return ID.AdapterRecyclerComments;
    }

    public AdapterRecyclerComments(Context context){
        this.context = context;
    }

    public interface CommentsItem<P extends CommentsItem.Comment>{
        @NonNull P getComment();

        interface Comment{
            //@NonNull Uri getPhoto();
            @NonNull String getUsername();
            @NonNull String getTextComment();
            @NonNull String getTimeComment();
        }
    }

    @NonNull
    @Override
    protected BindableViewHolder<I> onCreateViewHolder(Context context, LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BindableViewHolder<>(inflater.inflate(R.layout.comment_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder<I> holder, int position) {
        super.onBindViewHolder(holder, position);
        ((TextView)holder.itemView.findViewById(R.id.usernameCommentText)).setText(holder.item.getComment().getUsername());
        ((TextView)holder.itemView.findViewById(R.id.commentText)).setText(holder.item.getComment().getTextComment());
        ((TextView)holder.itemView.findViewById(R.id.timeCommentText)).setText(holder.item.getComment().getTimeComment());
    }
}
