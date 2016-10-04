package net.cloudapp.chooser.chooser.Images;

import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import net.cloudapp.chooser.chooser.SessionDetails;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ImageUploaderTask extends AsyncTask<UploadTask, Void, Void>{

    @Override
    protected Void doInBackground(UploadTask... tasks) {
        Log.d("Chooser", "Cloudinary uploading.");

        ByteArrayInputStream bs = getInputStream(tasks);
        String imageId = tasks[0].imageId;
        CloudinaryClient.uploadImage(bs, imageId);
        Log.d("Chooser", "Cloudinary uploaded image.");
        return null;
    }



    public ByteArrayInputStream getInputStream(UploadTask[] tasks){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        tasks[0].imageBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        return bs;
    }
}
