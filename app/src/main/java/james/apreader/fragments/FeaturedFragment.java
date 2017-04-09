package james.apreader.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import james.apreader.R;
import james.apreader.adapters.ListAdapter;
import james.apreader.common.Supplier;
import james.apreader.common.data.WallData;

public class FeaturedFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recycler = (RecyclerView) inflater.inflate(R.layout.fragment_recycler, container, false);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        ArrayList<WallData> walls = ((Supplier) getContext().getApplicationContext()).getWallpapers();

        Collections.sort(walls, new Comparator<WallData>() {
            @Override
            public int compare(WallData lhs, WallData rhs) {
                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                try {
                    Date lhd = format.parse(lhs.date), rhd = format.parse(rhs.date);
                    return rhd.compareTo(lhd);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        ListAdapter adapter = new ListAdapter(getActivity(), walls);
        recycler.setAdapter(adapter);

        return recycler;
    }
}
