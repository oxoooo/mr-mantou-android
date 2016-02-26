package ooo.oxo.mr.model;

import android.os.Parcel;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;

import java.util.Date;
import java.util.Map;

import ooo.oxo.mr.net.QiniuUtil;

@AVClassName("Image")
public class Image extends AVObject {

    public static final Creator CREATOR = AVObjectCreator.instance;

    private static final String PUBLISHED_AT = "publishedAt";

    private static final String FILE = "file";

    private static final String META = "meta";

    private static final String META_TYPE = "type";
    private static final String META_WIDTH = "width";
    private static final String META_HEIGHT = "height";

    public Image() {
        super();
    }

    public Image(Parcel in) {
        super(in);
    }

    public static AVQuery<Image> all() {
        return AVObject.getQuery(Image.class)
                .orderByDescending(PUBLISHED_AT);
    }

    public static AVQuery<Image> since(Image image) {
        return AVObject.getQuery(Image.class)
                .whereGreaterThan(PUBLISHED_AT, image.getPublishedAt())
                .orderByDescending(PUBLISHED_AT);
    }

    public Date getPublishedAt() {
        return getDate(PUBLISHED_AT);
    }

    public AVFile getFile() {
        return getAVFile(FILE);
    }

    public String getUrl() {
        return getFile().getUrl();
    }

    public String getUrl(int width) {
        return QiniuUtil.getUrl(getFile(), width);
    }

    public String getMime() {
        return getString("mime");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getMeta() {
        return getMap(META);
    }

    public String getType() {
        return (String) getMeta().get(META_TYPE);
    }

    public int getWidth() {
        return (Integer) getMeta().get(META_WIDTH);
    }

    public int getHeight() {
        return (Integer) getMeta().get(META_HEIGHT);
    }

}
