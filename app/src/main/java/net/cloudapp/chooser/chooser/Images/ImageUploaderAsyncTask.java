package net.cloudapp.chooser.chooser.Images;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ImageUploaderAsyncTask extends AsyncTask<UploadTask, Void, Void>{

    @Override
    protected Void doInBackground(UploadTask... tasks) {
        Log.d("Chooser", "Cloudinary uploading.");

        ByteArrayInputStream bs = getInputStream(tasks);
        String imageId = tasks[0].imageId;
        CloudinaryClient.uploadImage(bs);
        Log.d("Chooser", "Cloudinary uploaded image.");
        return null;
    }



    public ByteArrayInputStream getInputStream(UploadTask[] tasks){
        Bitmap srcBitmap = tasks[0].imageBitmap;
        Bitmap compressedImage = Bitmap.createScaledBitmap(srcBitmap, srcBitmap.getWidth()/2, srcBitmap.getHeight()/2,false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        compressedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }
}
