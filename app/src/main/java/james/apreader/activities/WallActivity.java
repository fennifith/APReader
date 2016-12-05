package james.apreader.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import james.apreader.R;
import james.apreader.Supplier;
import james.apreader.adapters.ImagePagerAdapter;
import james.apreader.data.WallData;
import james.apreader.dialogs.ImageDialog;
import james.apreader.utils.ImageUtils;
import james.apreader.views.PageIndicator;


public class WallActivity extends AppCompatActivity {

    WallData data;
    Supplier supplier;

    Toolbar toolbar;
    ViewPager viewPager;
    PageIndicator indicator;

    Handler handler;
    Runnable runnable;

    TextView date, auth, desc;
    FlexboxLayout categories;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        supplier = (Supplier) getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        data = getIntent().getParcelableExtra("wall");
        setTitle(data.name);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (PageIndicator) findViewById(R.id.indicator);
        date = (TextView) findViewById(R.id.date);
        auth = (TextView) findViewById(R.id.auth);
        desc = (TextView) findViewById(R.id.description);
        categories = (FlexboxLayout) findViewById(R.id.categories);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.setAdapter(new ImagePagerAdapter(this, data));
        indicator.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() > viewPager.getChildCount())
                    viewPager.setCurrentItem(0);
                else viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        };

        handler = new Handler();
        handler.postDelayed(runnable, 5000);

        findViewById(R.id.appbar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageDialog(WallActivity.this).setImage(viewPager.getCurrentItem()).setWallpaper(data).show();
            }
        });

        if (data.categories.size() > 0) {
            categories.setVisibility(View.VISIBLE);

            for (String category : data.categories) {
                View v = LayoutInflater.from(this).inflate(R.layout.layout_category, null);
                ((TextView) v.findViewById(R.id.title)).setText(category.toLowerCase());
                categories.addView(v);
            }
        }

        date.setText(data.date);
        desc.setText(Html.fromHtml(data.desc));
        desc.setMovementMethod(new LinkMovementMethod());

        findViewById(R.id.launchPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(data.url)));
            }
        });

        findViewById(R.id.launchAuthor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(supplier.getAuthors().get(data.authorId).url)));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wallpaper, menu);
        menu.findItem(R.id.action_fav).setIcon(ImageUtils.getVectorDrawable(this, supplier.isFavorite(data) ? R.drawable.fav_added : R.drawable.fav_add));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_fav:
                if (supplier.isFavorite(data)) {
                    if (!supplier.unfavoriteWallpaper(data))
                        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    if (!supplier.favoriteWallpaper(data))
                        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                }

                item.setIcon(ImageUtils.getVectorDrawable(this, supplier.isFavorite(data) ? R.drawable.fav_added : R.drawable.fav_add));
                break;
            case R.id.action_fullscreen:
                new ImageDialog(WallActivity.this).setImage(viewPager.getCurrentItem()).setWallpaper(data).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
