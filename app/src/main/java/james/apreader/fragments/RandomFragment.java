package james.apreader.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import james.apreader.R;
import james.apreader.adapters.ListAdapter;
import james.apreader.common.Supplier;
import james.apreader.common.data.ArticleData;

public class RandomFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        ArrayList<ArticleData> walls = ((Supplier) getContext().getApplicationContext()).getArticles();
        Collections.sort(walls, new Comparator<ArticleData>() {
            @Override
            public int compare(ArticleData first, ArticleData second) {
                //it's called RandomFragment.java but it's not random?
                //whaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAT
                return first.name.compareToIgnoreCase(second.name);
            }
        });

        recycler.setAdapter(new ListAdapter(getActivity(), walls));

        return recycler;
    }
}
