package james.apreader.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import james.apreader.data.WallData;
import james.apreader.views.CustomImageView;

public class ImagePagerAdapter extends PagerAdapter {

    Activity activity;
    WallData data;
    boolean centerCrop;

    public ImagePagerAdapter(Activity activity, WallData data) {
        this.activity = activity;
        this.data = data;
        centerCrop = true;
    }

    public ImagePagerAdapter(Activity activity, WallData data, boolean centerCrop) {
        this.activity = activity;
        this.data = data;
        this.centerCrop = centerCrop;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView image = new CustomImageView(activity);
        if (centerCrop) image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(activity).load(data.images.get(position)).into(image);
        container.addView(image);

        return image;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return data.images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
