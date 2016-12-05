package james.apreader.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import james.apreader.R;
import james.apreader.fragments.FeaturedFragment;
import james.apreader.fragments.RandomFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    Context context;

    public HomePagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FeaturedFragment();
            case 1:
                return new RandomFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.section_latest);
            case 1:
                return context.getString(R.string.section_all);
            default:
                return "";
        }
    }
}
