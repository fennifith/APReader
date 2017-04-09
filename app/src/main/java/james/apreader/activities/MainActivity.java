package james.apreader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import james.apreader.R;
import james.apreader.fragments.FavFragment;
import james.apreader.fragments.ListFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private View fragmentView;
    private Fragment fragment;
    private BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragmentView = findViewById(R.id.fragment);
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);

        setSupportActionBar(toolbar);

        navigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                setSelection(fragment);
            }
        });

        if (savedInstanceState != null) {
            fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (fragment != null) {
                setSelection(fragment);
                return;
            }
        }

        toolbar.setTitle(R.string.title_articles);
        fragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, fragment).commit();
    }

    private void setSelection(Fragment fragment) {
        if (fragment instanceof ListFragment)
            navigationView.setSelectedItemId(R.id.action_articles);
        else if (fragment instanceof FavFragment)
            navigationView.setSelectedItemId(R.id.action_favorites);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                break;
            case R.id.action_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_articles:
                fragment = new ListFragment();
                toolbar.setTitle(getString(R.string.title_articles));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                return true;
            case R.id.action_favorites:
                fragment = new FavFragment();
                toolbar.setTitle(getString(R.string.title_favorites));
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();

                return true;
        }

        return false;
    }
}
