package james.apreader.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import james.apreader.R;
import james.apreader.activities.WallActivity;
import james.apreader.common.Supplier;
import james.apreader.common.data.WallData;
import james.apreader.common.utils.ImageUtils;
import james.apreader.views.CustomImageView;
import james.apreader.views.SquareImageView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<WallData> walls;
    private Activity activity;
    private int layoutMode = -1;
    private Integer artistId;
    public final static int LAYOUT_MODE_HORIZONTAL = 1, LAYOUT_MODE_COMPLEX = 2;

    private Supplier supplier;

    public ListAdapter(Activity activity, ArrayList<WallData> walls) {
        this.activity = activity;
        this.walls = walls;
        supplier = (Supplier) activity.getApplicationContext();
    }

    public void setLayoutMode(int mode) {
        layoutMode = mode;
    }

    public void setArtist(int artistId) {
        this.artistId = artistId;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < walls.size()) return 0;
        else return 1;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutMode == LAYOUT_MODE_COMPLEX ? R.layout.layout_item_complex : R.layout.layout_item, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_progress, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                CustomImageView image = (CustomImageView) holder.v.findViewById(R.id.image);
                image.setImageBitmap(null);

                ((TextView) holder.v.findViewById(R.id.title)).setText(walls.get(position).name);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, WallActivity.class);
                        intent.putExtra("wall", walls.get(holder.getAdapterPosition()));
                        intent.putExtra("up", "Flat");

                        CustomImageView image = (CustomImageView) holder.v.findViewById(R.id.image);

                        if (image.getDrawable() != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            Drawable prev = image.getDrawable();
                            if (prev instanceof TransitionDrawable)
                                prev = ((TransitionDrawable) image.getDrawable()).getDrawable(1);
                            Bitmap bitmap;
                            try {
                                bitmap = ImageUtils.drawableToBitmap(prev);
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            } catch (Exception e) {
                                e.printStackTrace();
                                activity.startActivity(intent);
                                return;
                            }
                            byte[] b = baos.toByteArray();
                            intent.putExtra("preload", b);

                            ActivityOptionsCompat options = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(v, bitmap, 5, 5);
                            ActivityOptionsCompat.makeScaleUpAnimation(v, (int) v.getX(), (int) v.getY(), v.getWidth(), v.getHeight());
                            activity.startActivity(intent, options.toBundle());
                        } else {
                            activity.startActivity(intent);
                        }
                    }
                });

                if (layoutMode == LAYOUT_MODE_HORIZONTAL && image instanceof SquareImageView)
                    ((SquareImageView) image).setOrientation(SquareImageView.HORIZONTAL);

                if (layoutMode == LAYOUT_MODE_COMPLEX) {
                    ((TextView) holder.v.findViewById(R.id.author)).setText(walls.get(position).authorName);
                }

                WallData data = walls.get(holder.getAdapterPosition());
                if (data.images.size() > 0)  {
                    Glide.with(activity).load(data.images.get(0)).listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.v.findViewById(R.id.progress).setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.v.findViewById(R.id.progress).setVisibility(View.GONE);
                            return false;
                        }
                    }).into(image);

                    holder.v.findViewById(R.id.progress).setVisibility(View.VISIBLE);
                }

                break;
            case 1:
                if (artistId != null) {
                    supplier.getWallpapers(artistId, new Supplier.AsyncListener<ArrayList<WallData>>() {

                        @Override
                        public void onTaskComplete(ArrayList<WallData> value) {
                            walls.addAll(value);
                            notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure() {
                            artistId = null;
                            notifyDataSetChanged();

                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return walls.size() + (artistId == null ? 0 : 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }
}
