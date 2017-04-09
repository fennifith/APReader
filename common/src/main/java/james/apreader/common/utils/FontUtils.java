package james.apreader.common.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import james.apreader.common.Supplier;

public class FontUtils {

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "RobotoSlab-Regular.ttf");
    }

    public static void applyTypeface(TextView textView) {
        textView.setTypeface(((Supplier) textView.getContext().getApplicationContext()).getTypeface());
    }

}
