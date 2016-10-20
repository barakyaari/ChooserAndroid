package net.cloudapp.chooser.chooser.Controller;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by t-baya on 10/18/2016.
 */

public class PostUploadTask {
    public String title;
    public String description1;
    public String description2;
    public Bitmap image1Bitmap;
    public Bitmap image2Bitmap;
    public String token;
    public Context context;

    public PostUploadTask(String title, String description1, String description2, Bitmap image1Bitmap, Bitmap image2Bitmap, String token, Context context){
        this.title = title;
        this.description1 = description1;
        this.description2 = description2;
        this.image1Bitmap = image1Bitmap;
        this.image2Bitmap = image2Bitmap;
        this.token = token;
        this.context = context;
    }
}
