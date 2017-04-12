package james.apreader.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import james.apreader.R;
import james.apreader.common.Supplier;
import james.apreader.common.data.WallData;
import james.apreader.common.utils.FontUtils;
import james.apreader.common.utils.ImageUtils;
import james.apreader.util.CustomTabsBuilder;
import james.apreader.utils.CustomTabsMovementMethod;


public class WallActivity extends AppCompatActivity {

    public static final String EXTRA_ARTICLE = "wall";

    WallData data;
    Supplier supplier;

    Toolbar toolbar;

    Handler handler;
    Runnable runnable;

    TextView date, auth, desc;
    FlexboxLayout categories;

    ProgressBar progressBar;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        supplier = (Supplier) getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        data = getIntent().getParcelableExtra(EXTRA_ARTICLE);
        setTitle(data.name);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        date = (TextView) findViewById(R.id.date);
        auth = (TextView) findViewById(R.id.auth);
        desc = (TextView) findViewById(R.id.description);
        categories = (FlexboxLayout) findViewById(R.id.categories);

        FontUtils.applyTypeface(date);
        FontUtils.applyTypeface(auth);
        FontUtils.applyTypeface(desc);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handler = new Handler();
        handler.postDelayed(runnable, 5000);

        if (data.categories.size() > 0) {
            categories.setVisibility(View.VISIBLE);

            for (String category : data.categories) {
                View v = LayoutInflater.from(this).inflate(R.layout.layout_category, null);
                ((TextView) v.findViewById(R.id.title)).setText(category.toLowerCase());
                categories.addView(v);
            }
        }

        date.setText(data.date);
        desc.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(data.desc, 0) : Html.fromHtml(data.desc));
        desc.setMovementMethod(new CustomTabsMovementMethod(this));

        supplier.getFullContent(data, new Supplier.AsyncListener<String>() {
            @Override
            public void onTaskComplete(String value) {
                if (desc != null && progressBar != null) {
                    desc.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(value, 0) : Html.fromHtml(value));
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure() {
                if (progressBar != null)
                    progressBar.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.launchPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsBuilder.open(WallActivity.this, Uri.parse(data.url));
            }
        });

        findViewById(R.id.launchAuthor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsBuilder.open(WallActivity.this, Uri.parse(supplier.getAuthor().url));
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
        }
        return super.onOptionsItemSelected(item);
    }
}
