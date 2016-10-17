package net.cloudapp.chooser.chooser.Images;

import android.graphics.Bitmap;

/**
 * Created by t-baya on 10/4/2016.
 */

public class ImageUploaderImpl implements ImageUploader {
    @Override
    public String uploadImage(Bitmap toUpload) {
        UploadTask uploadTask = new UploadTask(toUpload);
        ImageUploaderAsyncTask uploader = new ImageUploaderAsyncTask();
        uploader.execute(uploadTask);
        return uploadTask.imageId;
    }
}
