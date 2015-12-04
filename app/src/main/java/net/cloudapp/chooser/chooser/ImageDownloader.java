package net.cloudapp.chooser.chooser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ImageDownloader implements Runnable{
    private Post m_post;
    public ImageDownloader(Post post){
        this.m_post = post;
    }

    @Override
    public void run() {
        Log.i("ChooserApp", "Downloading image 1 for: " + m_post.title);
        Bitmap mIcon1 = null;
        Bitmap mIcon2 = null;
        try {
            InputStream in1 = new URL(m_post.image1Url).openStream();
            mIcon1 = BitmapFactory.decodeStream(in1);
            InputStream in2 = new URL(m_post.image2Url).openStream();
            mIcon2 = BitmapFactory.decodeStream(in2);
        } catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        m_post.image1 = mIcon1;
        m_post.image2 = mIcon2;

    }
}
