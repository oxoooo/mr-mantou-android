package ooo.oxo.mr;

import android.net.Uri;
import android.support.v4.content.FileProvider;

public class ImageFileProvider extends FileProvider {

    @Override
    public String getType(Uri uri) {
        return "image/jpeg";
    }

}
