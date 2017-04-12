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
import james.apreader.activities.ArticleActivity;
import james.apreader.common.Supplier;
import james.apreader.common.data.ArticleData;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<ArticleData> walls;
    private Activity activity;
    private Integer artistId;

    private Supplier supplier;

    public ListAdapter(Activity activity, ArrayList<ArticleData> walls) {
        this.activity = activity;
        this.walls = walls;
        supplier = (Supplier) activity.getApplicationContext();
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
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_complex, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_progress, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ArticleData article = walls.get(position);

                holder.title.setText(article.name);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, ArticleActivity.class);
                        intent.putExtra(ArticleActivity.EXTRA_ARTICLE, walls.get(holder.getAdapterPosition()));
                        activity.startActivity(intent);
                    }
                });

                holder.content.setText(article.desc.contains(".") ? article.desc.substring(0, article.desc.indexOf(".") + 1) : article.desc);

                if (article.categories.size() > 0) {
                    holder.categories.setVisibility(View.VISIBLE);
                    holder.categories.removeAllViewsInLayout();

                    for (String category : article.categories) {
                        View v = LayoutInflater.from(activity).inflate(R.layout.layout_category, holder.categories, false);
                        ((TextView) v.findViewById(R.id.title)).setText(category.toLowerCase());

                        holder.categories.addView(v);
                    }
                } else holder.categories.setVisibility(View.GONE);

                holder.itemView.setAlpha(0);
                holder.itemView.animate().alpha(1).start();
                break;
            case 1:
                if (artistId != null) {
                    supplier.getArticles(new Supplier.AsyncListener<ArrayList<ArticleData>>() {

                        @Override
                        public void onTaskComplete(ArrayList<ArticleData> value) {
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

        private TextView title;
        private TextView content;
        private ViewGroup categories;

        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            categories = (ViewGroup) v.findViewById(R.id.categories);
        }
    }
}
