package net.cloudapp.chooser.chooser.Images;

import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * Created by t-baya on 10/2/2016.
 */

public interface ImageUploader {
    public String uploadImage(Bitmap toUpload);
}
