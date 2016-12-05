package james.apreader.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import james.apreader.R;
import james.apreader.adapters.HomePagerAdapter;

public class HomeFragment extends Fragment {

    TabLayout tabLayout;
    HomePagerAdapter adapter;
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = (ViewPager) v.findViewById(R.id.pager);
        tabLayout = (TabLayout) v.findViewById(R.id.tl);

        adapter = new HomePagerAdapter(getContext(), getActivity().getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        return v;
    }
}
