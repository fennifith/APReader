package james.apreader.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import james.apreader.R;
import james.apreader.Supplier;
import james.apreader.adapters.ListAdapter;
import james.apreader.data.AuthorData;
import james.apreader.data.WallData;

public class ListFragment extends Fragment {

    GridLayoutManager manager;
    ListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_wallpapers, container, false);
        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);

        Supplier supplier = (Supplier) getContext().getApplicationContext();

        AuthorData author = supplier.getAuthor(getArguments().getInt("authorId"));
        if (author == null) return null;

        ArrayList<WallData> walls = supplier.getWallpapers(author.id);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        manager = new GridLayoutManager(getContext(), metrics.widthPixels > metrics.heightPixels ? 3 : 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || adapter.getItemViewType(position) == 1)
                    return manager.getSpanCount();
                else return 1;
            }
        });
        recycler.setLayoutManager(manager);

        adapter = new ListAdapter(getActivity(), walls);
        adapter.setArtist(author.id);
        recycler.setAdapter(adapter);

        return v;
    }
}
