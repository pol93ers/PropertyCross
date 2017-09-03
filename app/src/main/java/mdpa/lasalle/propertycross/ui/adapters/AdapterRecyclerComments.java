package mdpa.lasalle.propertycross.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mdpa.lasalle.propertycross.R;
import mdpa.lasalle.propertycross.base.adapter.AdapterRecyclerBase;

public class AdapterRecyclerComments<I extends AdapterRecyclerComments.CommentsItem> extends AdapterRecyclerBase<I, AdapterRecyclerBase.BindableViewHolder<I>>{

    @NonNull
    @Override
    public ID getComponent() {
        return ID.AdapterRecyclerComments;
    }

    public interface CommentsItem<P extends CommentsItem.Comment>{
        @NonNull P getComment();

        interface Comment{
            @NonNull String getUsername();
            @NonNull String getTextComment();
            @NonNull String getTimeComment();
        }
    }

    private Context context;
    public AdapterRecyclerComments(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    protected BindableViewHolder<I> onCreateViewHolder(Context context, LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BindableViewHolder<>(inflater.inflate(R.layout.comment_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final BindableViewHolder<I> holder, int position) {
        super.onBindViewHolder(holder, position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date = dateFormat.parse(holder.item.getComment().getTimeComment());
            Calendar calMemory = Calendar.getInstance();
            calMemory.setTimeInMillis(date.getTime() /*+ TimeZone.getDefault().getRawOffset()*/);
            int dayComment = calMemory.get(Calendar.DAY_OF_MONTH);
            int monthComment = calMemory.get(Calendar.MONTH);
            int yearComment = calMemory.get(Calendar.YEAR);
            int hourComment = calMemory.get(Calendar.HOUR_OF_DAY);
            int minuteComment = calMemory.get(Calendar.MINUTE);
            String dateFinal = String.format(context.getString(R.string.dateTimeFormat), new DecimalFormat("00").format(dayComment), new DecimalFormat("00").format(monthComment + 1), new DecimalFormat("0000").format(yearComment), new DecimalFormat("00").format(hourComment), new DecimalFormat("00").format(minuteComment));
            ((TextView)holder.itemView.findViewById(R.id.timeCommentText)).setText(dateFinal);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ((TextView)holder.itemView.findViewById(R.id.usernameCommentText)).setText(holder.item.getComment().getUsername());
        ((TextView)holder.itemView.findViewById(R.id.commentText)).setText(holder.item.getComment().getTextComment());

    }
}
