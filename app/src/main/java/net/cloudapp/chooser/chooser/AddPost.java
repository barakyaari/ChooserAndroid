package net.cloudapp.chooser.chooser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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

import static android.provider.MediaStore.Images.Media.getBitmap;

public class AddPost extends Activity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE1 = 1;
    private static final int RESULT_LOAD_IMAGE2 = 2;
    private static final int SELECT_PHOTO = 100;
    private int selectedImage = 0;
    private SessionDetails sessionDetails;
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
        sessionDetails = (SessionDetails) getIntent().getSerializableExtra("SessionDetails");
    }

    private void takeImage(int imageNumber) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra("ImageNumber", imageNumber);
        startActivityForResult(i, cameraData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case SELECT_PHOTO:
                Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
                switch (selectedImage){
                    case 1:
                        image1.setImageBitmap(bitmap);
                        image1BitMap = bitmap;
                        break;
                    case 2:
                        image2.setImageBitmap(bitmap);
                        image2BitMap = bitmap;
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void uploadPost() {
        String title, description1, description2, image1, image2;
        image1 = "";
        image2 = "";
        try {
            //Convert images to base64 string:
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Bitmap smallBitmap1 = Bitmap.createScaledBitmap(image1BitMap, image1BitMap.getWidth()/8, image1BitMap.getHeight()/8, true);
            smallBitmap1.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
            smallBitmap1.recycle();
            byte[] byteArray1 = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            image1 = Base64.encodeToString(byteArray1, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
            Bitmap smallBitmap1 = Bitmap.createScaledBitmap(image2BitMap, image2BitMap.getWidth()/8, image2BitMap.getHeight()/8, true);
            smallBitmap1.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream2);
            byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
            image2 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        title = editTextTitle.getText().toString();
        description1 = editTextDescription1.getText().toString();
        description2 = editTextDescription2.getText().toString();
        ConnectionManager connectionManager = new ConnectionManager(sessionDetails);
        connectionManager.AddPostWithBlob(title, image1, description1, image2, description2, new doAtFinish());
        ;
    }

    private class doAtFinish implements Runnable
    {
        @Override
        public void run() {
            finish();
        }
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
                selectedImage = 1;
                Intent chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, SELECT_PHOTO);
                break;

            case R.id.buttonSelectImage2:
                selectedImage = 2;
                Intent chooseImageIntent2 = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent2, SELECT_PHOTO);
                break;
        }
    }

}