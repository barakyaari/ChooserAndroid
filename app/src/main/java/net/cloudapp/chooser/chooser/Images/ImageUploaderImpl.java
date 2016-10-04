package net.cloudapp.chooser.chooser.Images;

import android.graphics.Bitmap;

import java.io.InputStream;
import java.util.UUID;

/**
 * Created by t-baya on 10/4/2016.
 */

public class ImageUploaderImpl implements ImageUploader {
    @Override
    public String uploadImage(Bitmap toUpload) {
        UploadTask uploadTask = new UploadTask(toUpload);
        ImageUploaderTask uploader = new ImageUploaderTask();
        uploader.execute(uploadTask);
        return uploadTask.imageId;
    }
}
