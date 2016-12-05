package james.apreader.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class AuthorData implements Parcelable {

    public String name, description, wp;
    @Nullable
    public String url;
    public int id;

    public AuthorData(String name, String description, int id, @Nullable String url, String wp) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.url = url;
        this.wp = wp;
    }

    protected AuthorData(Parcel in) {
        name = in.readString();
        description = in.readString();
        id = in.readInt();
        url = in.readString();
        wp = in.readString();
    }

    public static final Creator<AuthorData> CREATOR = new Creator<AuthorData>() {
        @Override
        public AuthorData createFromParcel(Parcel in) {
            return new AuthorData(in);
        }

        @Override
        public AuthorData[] newArray(int size) {
            return new AuthorData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(id);
        dest.writeString(url);
        dest.writeString(wp);
    }
}
