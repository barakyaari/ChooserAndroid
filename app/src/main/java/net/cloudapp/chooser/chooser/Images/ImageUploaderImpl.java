package net.cloudapp.chooser.chooser.Images;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutionException;

/**
 * Created by t-baya on 10/4/2016.
 */

public class ImageUploaderImpl implements ImageUploader {
    @Override
    public String uploadImage(Bitmap toUpload) {
        ByteArrayInputStream inputStream = getInputStream(toUpload);
        CloudinaryClient client = new CloudinaryClient();
        return client.uploadImage(inputStream);
    }

    public ByteArrayInputStream getInputStream(Bitmap srcBitmap){
        Bitmap compressedImage = Bitmap.createScaledBitmap(srcBitmap, srcBitmap.getWidth()/3, srcBitmap.getHeight()/3,false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        compressedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }

}
