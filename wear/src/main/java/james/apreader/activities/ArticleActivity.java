package james.apreader.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.wearable.view.drawer.WearableActionDrawer;
import android.support.wearable.view.drawer.WearableDrawerLayout;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import james.apreader.R;
import james.apreader.common.Supplier;
import james.apreader.common.data.WallData;

public class ArticleActivity extends Activity {

    public static final String EXTRA_ARTICLE = "james.apreader.EXTRA_ARTICLE";

    private WearableDrawerLayout drawerLayout;
    private TextView content;
    private ImageView favoriteImage;

    private WallData article;
    private Supplier supplier;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        supplier = (Supplier) getApplicationContext();
        article = getIntent().getParcelableExtra(EXTRA_ARTICLE);

        drawerLayout = (WearableDrawerLayout) findViewById(R.id.drawerLayout);
        WearableActionDrawer actionDrawer = (WearableActionDrawer) findViewById(R.id.actionDrawer);
        NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        TextView title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);
        TextView date = (TextView) findViewById(R.id.date);
        final View favorite = findViewById(R.id.favorite);
        favoriteImage = (ImageView) findViewById(R.id.imageView);

        drawerLayout.peekDrawer(Gravity.BOTTOM);
        actionDrawer.lockDrawerClosed();
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0 || scrollY < oldScrollY)
                    drawerLayout.peekDrawer(Gravity.BOTTOM);
                else drawerLayout.closeDrawer(Gravity.BOTTOM);
            }
        });

        title.setText(article.name);
        content.setText(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? Html.fromHtml(article.desc, 0) : Html.fromHtml(article.desc));
        date.setText(article.date);

        favoriteImage.setImageResource(supplier.isFavorite(article) ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (supplier.isFavorite(article)) {
                    supplier.unfavoriteWallpaper(article);
                    favoriteImage.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    supplier.favoriteWallpaper(article);
                    favoriteImage.setImageResource(R.drawable.ic_favorite);
                }
            }
        });
    }

}
