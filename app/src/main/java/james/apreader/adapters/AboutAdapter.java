package james.apreader.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import james.apreader.R;
import james.apreader.common.utils.FontUtils;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> {

    Activity activity;
    ArrayList<Item> items;

    public AboutAdapter(Activity activity, ArrayList<Item> items) {
        this.activity = activity;
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof HeaderItem) return 0;
        else return 1;
    }

    @Override
    public AboutAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case 0:
                return new ViewHolder(inflater.inflate(R.layout.layout_header, null));
            case 1:
                return new ViewHolder(inflater.inflate(R.layout.layout_text, null));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(AboutAdapter.ViewHolder holder, int position) {
        items.get(position).bindView(holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class HeaderItem extends Item {

        public boolean centered;
        @Nullable
        public String name, content, url;

        public HeaderItem(Context context, @Nullable String name, @Nullable String content, boolean centered, @Nullable String url) {
            super(context);

            this.centered = centered;
            this.name = name;
            this.content = content;
            this.url = url;
        }

        @Override
        public void bindView(ViewHolder holder) {
            if (name != null && name.length() > 0) {
                TextView header = (TextView) holder.v.findViewById(R.id.header);
                header.setVisibility(View.VISIBLE);
                header.setText(name);

                if (centered) header.setGravity(Gravity.CENTER_HORIZONTAL);
                FontUtils.applyTypeface(header);
            } else holder.v.findViewById(R.id.header).setVisibility(View.GONE);

            if (content != null && content.length() > 0) {
                TextView desc = (TextView) holder.v.findViewById(R.id.content);
                desc.setVisibility(View.VISIBLE);
                desc.setText(content);

                if (centered) {
                    desc.setGravity(Gravity.CENTER_HORIZONTAL);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                        desc.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }

                FontUtils.applyTypeface(desc);
            } else holder.v.findViewById(R.id.content).setVisibility(View.GONE);

            if (url != null) {
                holder.v.setClickable(true);
                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });
            } else holder.v.setClickable(false);
        }
    }

    public static class TextItem extends Item {

        @Nullable
        public String name, content, primary;

        public TextItem(Context context, @Nullable String name, @Nullable String content, @Nullable String primary) {
            super(context);

            this.name = name;
            this.content = content;
            this.primary = primary;
        }

        @Override
        public void bindView(ViewHolder holder) {

            if (name != null && name.length() > 0) {
                TextView header = (TextView) holder.v.findViewById(R.id.header);
                header.setVisibility(View.VISIBLE);
                header.setText(name);
                FontUtils.applyTypeface(header);
            } else holder.v.findViewById(R.id.header).setVisibility(View.GONE);

            if (content != null && content.length() > 0) {
                TextView desc = (TextView) holder.v.findViewById(R.id.content);
                desc.setVisibility(View.VISIBLE);
                desc.setText(content);
                FontUtils.applyTypeface(desc);
            } else holder.v.findViewById(R.id.content).setVisibility(View.GONE);

            if (primary != null) {
                View card = holder.v.findViewById(R.id.card);
                card.setClickable(true);
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(primary)));
                    }
                });
            } else holder.v.findViewById(R.id.card).setClickable(false);
        }
    }

    public static class Item {

        private Context context;

        public Item(Context context) {
            this.context = context;
        }

        public Context getContext() {
            return context;
        }

        public void bindView(AboutAdapter.ViewHolder holder) {
        }
    }
}
