package james.apreader.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.animation.DecelerateInterpolator;

import james.apreader.R;
import james.apreader.adapters.SearchAdapter;

public class SearchActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv;
    SearchAdapter adapter;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView) findViewById(R.id.recycler);
        searchView = (SearchView) findViewById(R.id.searchView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.matches("") && !newText.matches(" ")) adapter.filter(newText);
                if (newText.toLowerCase().matches("do a barrel roll"))
                    rv.animate().rotationBy(360).setDuration(1000).setInterpolator(new DecelerateInterpolator()).start();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });

        rv.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new SearchAdapter(SearchActivity.this);
        rv.setAdapter(adapter);

        searchView.setIconified(false);
        searchView.requestFocus();
    }
}
