package net.cloudapp.chooser.chooser.Images;


import android.app.Activity;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import net.cloudapp.chooser.chooser.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageFullscreen extends Activity {
    PhotoView imageView;
    PhotoViewAttacher attacher;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_image);
        imageView =(PhotoView) findViewById(R.id.imageView);
        String image_id = getIntent().getStringExtra("image");
        String url = CloudinaryClient.bigImageUrl(image_id, false);
        Glide
                .with(getApplicationContext())
                .load(url)
                .into(imageView);
        attacher = new PhotoViewAttacher(imageView);
        attacher.update();

    }

}

