package james.apreader.services;

import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.nio.charset.Charset;

import james.apreader.activities.WallActivity;
import james.apreader.common.Supplier;
import james.apreader.common.data.WallData;

public class WearListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if (messageEvent.getPath().equals(Supplier.WEAR_PATH)) {
            String data = new String(messageEvent.getData(), Charset.forName("UTF-8"));
            if (URLUtil.isNetworkUrl(data)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                try {
                    Intent intent = new Intent(this, WallActivity.class);
                    intent.putExtra(WallActivity.EXTRA_ARTICLE, new Gson().fromJson(data, WallData.class));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
