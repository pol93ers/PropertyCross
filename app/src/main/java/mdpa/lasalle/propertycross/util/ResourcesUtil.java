package mdpa.lasalle.propertycross.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

public class ResourcesUtil {
    public interface ImageLoader {
        void loadImage(@NonNull ImageView view, @NonNull Uri uri);
    }

    public static int getInt(@NonNull Context context, @IntegerRes int resId)
    {
        return context.getResources().getInteger(resId);
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        return ContextCompat.getDrawable(context, resId);
    }

    public static int getColor(@NonNull Context context, @ColorRes int resId) {
        return ContextCompat.getColor(context, resId);
    }

    public static ColorStateList getColorStateList(@NonNull Context context, @ColorRes int resId) {
        return ContextCompat.getColorStateList(context, resId);
    }
}
