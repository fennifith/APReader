package james.apreader.utils;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import james.apreader.activities.WearSenderActivity;

public class WearMovementMethod extends LinkMovementMethod {

    private Context context;

    private final GestureDetector gestureDetector;
    private TextView textView;
    private Spannable buffer;

    public WearMovementMethod(Context context) {
        this.context = context;
        gestureDetector = new GestureDetector(context, new SimpleOnGestureListener());
    }

    @Override
    public boolean onTouchEvent(TextView textView, Spannable buffer, MotionEvent event) {
        this.textView = textView;
        this.buffer = buffer;
        gestureDetector.onTouchEvent(event);
        return false;
    }

    private class SimpleOnGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= textView.getTotalPaddingLeft();
            y -= textView.getTotalPaddingTop();
            x += textView.getScrollX();
            y += textView.getScrollY();

            Layout layout = textView.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length > 0) {
                String linkText = link[0].getURL();
                if (Patterns.WEB_URL.matcher(linkText).matches() || Patterns.EMAIL_ADDRESS.matcher(linkText).matches()) {
                    Intent intent = new Intent(context, WearSenderActivity.class);
                    intent.putExtra(WearSenderActivity.EXTRA_MESSAGE, linkText);
                    context.startActivity(intent);
                }
                return true;
            }

            return false;
        }
    }

}
