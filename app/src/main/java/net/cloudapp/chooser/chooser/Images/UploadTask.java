package net.cloudapp.chooser.chooser.Images;

import android.graphics.Bitmap;

import java.util.UUID;

/**
 * Created by t-baya on 10/4/2016.
 */

public class UploadTask {
    public String imageId;
    public Bitmap imageBitmap;

    public UploadTask(Bitmap bitmap){
        imageId = UUID.randomUUID().toString();
        imageBitmap = bitmap;
    }
}
