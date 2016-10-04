package net.cloudapp.chooser.chooser.Media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;

import java.util.ArrayList;

public class ImageSelector extends AppCompatActivity {
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    ArrayList<Uri> mImageUris;

    public Uri getImageUri(Context context) {
        Intent intent  = new Intent(context, ImagePickerActivity.class);
        Config config = new Config();
        config.setSelectionMin(1);
        config.setSelectionLimit(1);
        ImagePickerActivity.setConfig(config);

        startActivityForResult(intent,INTENT_REQUEST_GET_IMAGES);

        return mImageUris.get(0);
    }
}
