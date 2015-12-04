package net.cloudapp.chooser.chooser;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Barak on 29/11/2015.
 */
public class Post {
    public Post(String title, Bitmap image1, String description1, Bitmap image2, String description2, int id) {
        Log.i("ChooserApp", "Creating New Post: " + title);
        this.title = title;
        this.description1 = description1;
        this.description2 = description2;
        this.id = id;
        this.image1 = image1;
        this.image2 = image2;
    }

    public String title;
    public String image1Url;
    public String image2Url;
    public String description1;
    public String description2;
    public int id;
    public Bitmap image1;
    public Bitmap image2;
}
