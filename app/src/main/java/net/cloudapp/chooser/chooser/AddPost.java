package net.cloudapp.chooser.chooser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class AddPost extends Activity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE1 = 1;
    private static final int RESULT_LOAD_IMAGE2 = 2;

    Button buttonAddPost;
    Button buttonCancel;
    Button buttonSelect1;
    Button buttonSelect2;

    EditText editTextTitle;
    EditText editTextDescription1;
    EditText editTextDescription2;

    ImageView image1;
    ImageView image2;
    final static int cameraData = 0;
    Bitmap image1BitMap;
    Bitmap image2BitMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        buttonAddPost = (Button) findViewById(R.id.postButton);
        buttonAddPost.setOnClickListener(this);
        buttonCancel = (Button) findViewById(R.id.cancelButton);
        buttonCancel.setOnClickListener(this);

        image1 = (ImageView) findViewById(R.id.addPostImageView1);
        image2 = (ImageView) findViewById(R.id.addPostImageView2);

        editTextTitle = (EditText) findViewById(R.id.postTitleEditText);
        editTextDescription1 = (EditText) findViewById(R.id.description1EditText);
        editTextDescription2 = (EditText) findViewById(R.id.description2EditText);

        buttonSelect1 = (Button) findViewById(R.id.buttonSelectImage1);
        buttonSelect2 = (Button) findViewById(R.id.buttonSelectImage2);

        buttonSelect1.setOnClickListener(this);
        buttonSelect2.setOnClickListener(this);
    }

    private void takeImage(int imageNumber) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra("ImageNumber", imageNumber);
        startActivityForResult(i, cameraData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (requestCode == RESULT_LOAD_IMAGE1)
                image1.setImageURI(selectedImageUri);
            else if (requestCode == RESULT_LOAD_IMAGE2)
                image2.setImageURI(selectedImageUri);

        }
    }

    private void uploadPost() {
        String title, description1, description2, image1, image2;
        image1 = "";
        image2 = "";
        try {
        //Convert images to base64 string:
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap smallBitmap1 = Bitmap.createScaledBitmap(image1BitMap, 100, 100, true);

            smallBitmap1.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
            smallBitmap1.recycle();
            byte[] byteArray1 = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            byteArrayOutputStream = null;
            image1 = Base64.encodeToString(byteArray1, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            Bitmap smallBitmap1 = Bitmap.createScaledBitmap(image2BitMap, 100, 100, true);
            smallBitmap1.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream2);
            byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
            image2 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        title = editTextTitle.getText().toString();
        description1 = editTextDescription1.getText().toString();
        description2 = editTextDescription2.getText().toString();
        PostUploader uploader = new PostUploader(title, image1, description1, image2, description2);
        uploader.execute();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.postButton: {
                Bitmap bitmap1 = ((BitmapDrawable) image1.getDrawable()).getBitmap();
                image1BitMap = bitmap1;

                Bitmap bitmap2 = ((BitmapDrawable) image2.getDrawable()).getBitmap();
                image2BitMap = bitmap2;

                uploadPost();
            }
            break;

            case R.id.cancelButton: {
            }
            break;

            case R.id.buttonSelectImage1:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE1);
                break;

            case R.id.buttonSelectImage2:
                Intent galleryIntent2 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent2, RESULT_LOAD_IMAGE2);
                break;

        }
    }

}