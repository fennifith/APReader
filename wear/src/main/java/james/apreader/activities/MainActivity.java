package james.apreader.activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.wearable.view.CurvedChildLayoutManager;
import android.support.wearable.view.WearableRecyclerView;

import james.apreader.R;
import james.apreader.adapters.ArticleAdapter;
import james.apreader.common.Supplier;

public class MainActivity extends Activity {

    private Supplier supplier;

    private WearableRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        supplier = (Supplier) getApplicationContext();

        recyclerView = (WearableRecyclerView) findViewById(R.id.recycler);

        recyclerView.setLayoutManager(new CurvedChildLayoutManager(this));
        recyclerView.setAdapter(new ArticleAdapter(this, supplier.getWallpapers(), 0));

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        recyclerView.setPadding(0, size.y / 2, 0, size.y / 2);
    }
}
