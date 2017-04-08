package james.apreader.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import james.apreader.common.utils.ImageUtils;

public class CustomImageView extends AppCompatImageView {

    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void transition(final Activity activity, final Drawable second) {
        if (second == null) return;
        if (activity == null) {
            setImageDrawable(second);
            return;
        }
        final int size = Math.min(getMeasuredWidth(), getMeasuredHeight());
        new Thread() {
            @Override
            public void run() {
                Bitmap image = null;
                try {
                    image = ImageUtils.drawableToBitmap(second);
                    if (image != null) image = ThumbnailUtils.extractThumbnail(image, size, size);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

                final Bitmap result = image;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result == null) {
                            setImageDrawable(second);
                            return;
                        }

                        Animation exitAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
                        exitAnim.setDuration(150);
                        exitAnim.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                setImageBitmap(result);
                                Animation enterAnim = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);
                                enterAnim.setDuration(150);
                                startAnimation(enterAnim);
                            }
                        });
                        startAnimation(exitAnim);
                    }
                });
            }
        }.start();
    }
}
