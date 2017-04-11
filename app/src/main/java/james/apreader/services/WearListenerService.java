package james.apreader.services;

import android.content.Intent;
import android.net.Uri;
import android.webkit.URLUtil;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.Charset;

import james.apreader.common.Supplier;

public class WearListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if (messageEvent.getPath().equals(Supplier.WEAR_PATH)) {
            String data = new String(messageEvent.getData(), Charset.forName("UTF-8"));
            if (URLUtil.isNetworkUrl(data))
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data)));
        }
    }
}
