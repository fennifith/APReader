package james.apreader.utils;

import android.content.Context;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import james.apreader.R;

public class CustomTabsBuilder {

    public static void open(Context context, Uri uri) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        builder.build().launchUrl(context, uri);
    }

}
