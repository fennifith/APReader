package james.apreader.activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;

import java.util.List;

import james.apreader.R;
import james.apreader.adapters.ArticleAdapter;
import james.apreader.common.Supplier;
import james.apreader.common.data.WallData;

public class MainActivity extends Activity {

    private Supplier supplier;

    private WearableRecyclerView recyclerView;
    private List<WallData> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supplier = (Supplier) getApplicationContext();

        recyclerView = (WearableRecyclerView) findViewById(R.id.recycler);

        articles = supplier.getFavoriteWallpapers();
        articles.addAll(supplier.getWallpapers());

        recyclerView.setLayoutManager(new CurvedChildLayoutManager(this));
        recyclerView.setAdapter(new ArticleAdapter(this, articles, 0));

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        recyclerView.setPadding(0, (int) (size.y / 2.5), 0, (int) (size.y / 2.5));
    }
}
