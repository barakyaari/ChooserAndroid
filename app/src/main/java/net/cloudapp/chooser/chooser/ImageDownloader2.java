package net.cloudapp.chooser.chooser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Barak on 30/11/2015.
 */
public class ImageDownloader2 implements RunnableFuture<Bitmap> {
    Bitmap mIcon = null;
    public ImageDownloader2(String url){
        InputStream in = null;
        try {
            in = new URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Bitmap get() throws InterruptedException, ExecutionException {
        return null;
    }

    @Override
    public Bitmap get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {

        return null;
    }

    @Override
    public void run() {

    }
}
