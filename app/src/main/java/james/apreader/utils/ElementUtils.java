package james.apreader.utils;

import android.support.annotation.Nullable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class ElementUtils {

    @Nullable
    public static Document getDocument(URL url) {
        try {
            String result = "";
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(connection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            in.close();
            connection.disconnect();

            return Jsoup.parse(result);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDescription(Document document) {
        Elements elements = document.select("description");
        if (elements.size() > 0) return elements.get(0).text();
        else return null;
    }

    public static String getName(Element item) {
        Elements elements = item.select("title");
        if (elements.size() > 0) return elements.get(0).text();
        else return null;
    }

    public static String getDescription(Element item) {
        Elements elements = item.select("description");
        if (elements.size() > 0) {
            Document document = Jsoup.parse(elements.get(0).text());
            document.select("img").remove();
            return document.text().split("Read More")[0];
        } else return null;
    }

    public static String getDate(Element item) {
        Elements elements = item.select("pubDate");
        if (elements.size() > 0) return elements.get(0).text();
        else return null;
    }

    public static String getLink(Element item) {
        Elements elements = item.select("guid");
        if (elements.size() > 0) return elements.get(0).text();
        else {
            elements = item.select("comments");
            if (elements.size() > 0) {
                String link = elements.get(0).text();
                if (link.contains("#")) link = link.substring(0, link.indexOf("#"));
                return link;
            } else return null;
        }
    }

    public static String getComments(Element item) {
        Elements elements = item.select("comments");
        if (elements.size() > 0) return elements.get(0).text();
        else return getLink(item);
    }

    public static ArrayList<String> getImages(Element item) {
        ArrayList<String> images = new ArrayList<>();

        Elements elements = item.select("description");
        if (elements.size() > 0) {
            Document content = Jsoup.parse(elements.get(0).text());
            Elements imgs = content.select("img");
            for (int i = 0; i < imgs.size() - 7; i++) {
                Element img = imgs.get(i);
                if (!img.hasAttr("src")) continue;
                images.add(img.attr("src"));
            }
        }

        return images;
    }

    public static ArrayList<String> getCategories(Element item) {
        ArrayList<String> categories = new ArrayList<>();

        Elements elements = item.select("category");
        for (Element element : elements) {
            categories.add(element.text());
        }

        return categories;
    }
}
