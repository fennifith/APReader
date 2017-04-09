package james.apreader.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import james.apreader.R;
import james.apreader.activities.WallActivity;
import james.apreader.common.Supplier;
import james.apreader.common.data.WallData;

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
                WallData article = walls.get(position);

                ((TextView) holder.v.findViewById(R.id.title)).setText(article.name);

                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, WallActivity.class);
                        intent.putExtra("wall", walls.get(holder.getAdapterPosition()));
                        intent.putExtra("up", "Flat");
                        activity.startActivity(intent);
                    }
                });

                if (layoutMode == LAYOUT_MODE_COMPLEX) {
                    ((TextView) holder.v.findViewById(R.id.content)).setText(article.desc.contains(".") ? article.desc.substring(0, article.desc.indexOf(".") + 1) : article.desc);

                    ViewGroup categories = (ViewGroup) holder.v.findViewById(R.id.categories);
                    if (article.categories.size() > 0) {
                        categories.setVisibility(View.VISIBLE);
                        categories.removeAllViewsInLayout();

                        for (String category : article.categories) {
                            View v = LayoutInflater.from(activity).inflate(R.layout.layout_category, categories, false);
                            ((TextView) v.findViewById(R.id.title)).setText(category.toLowerCase());
                            categories.addView(v);
                        }
                    } else categories.setVisibility(View.GONE);
                }

                holder.v.setAlpha(0);
                holder.v.animate().alpha(1).start();
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
