package james.apreader.common;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import james.apreader.common.data.AuthorData;
import james.apreader.common.data.WallData;
import james.apreader.common.utils.ElementUtils;

public class Supplier extends Application {

    private String[] urls;
    private int[] pages;

    private ArrayList<AuthorData> authors;
    private ArrayList<WallData> wallpapers;

    private ArrayList<String> favWallpapers;

    private SharedPreferences prefs;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        gson = new Gson();

        urls = getResources().getStringArray(R.array.people_wps);
        pages = new int[urls.length];

        favWallpapers = new ArrayList<>();

        int size = prefs.getInt("favorites-size", 0);
        for (int i = 0; i < size; i++) {
            String json = prefs.getString("favorites-" + i, null);
            favWallpapers.add(json);
        }
    }

    public boolean getNetworkResources() {
        //download any resources needed for the voids below while the splash screen is showing
        //yes, this is thread-safe
        //no, it is not needed for the current setup since all the resources are in res/values/strings.xml

        authors = new ArrayList<>();
        wallpapers = new ArrayList<>();

        for (int i = 0; i < urls.length; i++) {
            try {
                Document document = ElementUtils.getDocument(new URL(urls[i]));
                if (document == null) continue;

                AuthorData author = new AuthorData(document.title(), ElementUtils.getDescription(document), i, urls[i].substring(0, urls[i].length() - 5), urls[i]);
                authors.add(author);

                Elements elements = document.select("item");
                for (Element element : elements) {
                    WallData data = new WallData(ElementUtils.getName(element), ElementUtils.getDescription(element), ElementUtils.getDate(element), ElementUtils.getLink(element), ElementUtils.getComments(element), ElementUtils.getImages(element), ElementUtils.getCategories(element), author.name, author.id);
                    wallpapers.add(data);
                }
                // etc
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    //get a list of the different sections
    public ArrayList<AuthorData> getAuthors() {
        return authors;
    }

    @Nullable
    public AuthorData getAuthor(int id) {
        if (id < 0 || id >= authors.size()) return null;
        else return authors.get(id);
    }

    //get a list of the different wallpapers
    public ArrayList<WallData> getWallpapers() {
        ArrayList<WallData> walls = new ArrayList<>();
        walls.addAll(wallpapers);

        return walls;
    }

    public ArrayList<WallData> getWallpapers(int authorId) {
        ArrayList<WallData> walls = new ArrayList<>();

        for (WallData wallpaper : wallpapers) {
            if (wallpaper.authorId == authorId) walls.add(wallpaper);
        }

        return walls;
    }

    public void getWallpapers(final int id, final AsyncListener<ArrayList<WallData>> listener) {
        if (id < 0 || id >= pages.length) return;

        new Thread() {
            @Override
            public void run() {
                final ArrayList<WallData> walls = new ArrayList<>();

                Document document;
                try {
                    document = ElementUtils.getDocument(new URL(urls[id] + "?paged=" + String.valueOf(pages[id] + 2)));
                } catch (IOException e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure();
                        }
                    });
                    return;
                }

                if (document == null) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFailure();
                        }
                    });
                    return;
                }

                Elements elements = document.select("item");
                for (Element element : elements) {
                    WallData data = new WallData(ElementUtils.getName(element), ElementUtils.getDescription(element), ElementUtils.getDate(element), ElementUtils.getLink(element), ElementUtils.getComments(element), ElementUtils.getImages(element), ElementUtils.getCategories(element), authors.get(id).name, id);
                    walls.add(data);
                }

                wallpapers.addAll(walls);
                pages[id]++;

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onTaskComplete(walls);
                    }
                });
            }
        }.start();
    }

    public ArrayList<WallData> getFavoriteWallpapers() {
        ArrayList<WallData> walls = new ArrayList<>();
        for (String string : favWallpapers) {
            walls.add(gson.fromJson(string, WallData.class));
        }

        return walls;
    }

    public boolean setFavoriteWallpapers() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("favorites-size", favWallpapers.size());

        for (int i = 0; i < favWallpapers.size(); i++) {
            editor.putString("favorites-" + i, favWallpapers.get(i));
        }

        return editor.commit();
    }

    public boolean isFavorite(WallData data) {
        return favWallpapers.contains(gson.toJson(data));
    }

    public boolean favoriteWallpaper(WallData data) {
        if (isFavorite(data)) return false;

        favWallpapers.add(gson.toJson(data));
        return setFavoriteWallpapers();
    }

    public boolean unfavoriteWallpaper(WallData data) {
        if (!isFavorite(data)) return false;

        favWallpapers.remove(favWallpapers.indexOf(gson.toJson(data)));
        return setFavoriteWallpapers();
    }

    public void downloadWallpaper(Context context, String name, String url) {
        //start a download
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(url));
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".png");
        r.allowScanningByMediaScanner();
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }

    //share a wallpaper
    public void shareWallpaper(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(Uri.parse(url)));
        context.startActivity(intent);
    }

    public interface AsyncListener<E> {
        void onTaskComplete(E value);

        void onFailure();
    }
}
