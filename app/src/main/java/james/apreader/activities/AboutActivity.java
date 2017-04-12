package james.apreader.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import james.apreader.R;
import james.apreader.adapters.AboutAdapter;
import james.apreader.common.Supplier;
import james.apreader.common.data.AuthorData;
import james.apreader.common.utils.FontUtils;
import james.apreader.utils.CustomTabsBuilder;

public class AboutActivity extends AppCompatActivity {

    Supplier supplier;

    Toolbar toolbar;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        supplier = (Supplier) getApplicationContext();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        recycler = (RecyclerView) findViewById(R.id.recycler);

        View title = toolbar.getChildAt(0);
        if (title != null && title instanceof TextView)
            FontUtils.applyTypeface((TextView) toolbar.getChildAt(0));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<AboutAdapter.Item> items = new ArrayList<>();

        if (getResources().getBoolean(R.bool.show_contributors)) {
            items.add(new AboutAdapter.HeaderItem(this, getString(R.string.contributors), null, true, null));

            AuthorData author = supplier.getAuthor();
            items.add(new AboutAdapter.TextItem(this, author.name, author.description, author.url));
        }

        items.add(new AboutAdapter.HeaderItem(this, null, getString(R.string.me), false, null));
        items.add(new AboutAdapter.HeaderItem(this, null, getResources().getString(R.string.alex), false, "https://github.com/alexandrepiveteau"));

        String[] headers = getResources().getStringArray(R.array.namey);
        String[] contents = getResources().getStringArray(R.array.desc);
        String[] urls = getResources().getStringArray(R.array.uri);

        items.add(new AboutAdapter.HeaderItem(this, getString(R.string.libraries), null, true, null));

        for (int i = 0; i < headers.length; i++) {
            items.add(new AboutAdapter.TextItem(this, headers[i], contents[i], urls[i]));
        }

        recycler.setLayoutManager(new GridLayoutManager(this, 1));
        recycler.setAdapter(new AboutAdapter(this, items));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_github:
                CustomTabsBuilder.open(this, Uri.parse("https://github.com/TheAndroidMaster/APReader"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
