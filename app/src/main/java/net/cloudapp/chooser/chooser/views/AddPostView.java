package net.cloudapp.chooser.chooser.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.cloudapp.chooser.chooser.Controller.PostsUploadController;
import net.cloudapp.chooser.chooser.Images.ImagePicker;
import net.cloudapp.chooser.chooser.Images.CloudinaryClient;
import net.cloudapp.chooser.chooser.R;


public class AddPostView extends AppCompatActivity implements View.OnClickListener {
    private static final int SELECT_PHOTO = 100;
    private int selectedImage = 0;
    Button buttonAddPost, buttonCancel, buttonNotMyPost;
    EditText editTextTitle, editTextDescription1, editTextDescription2;
    ImageView image1, image2;
    TextView tokens;
    Bitmap image1BitMap, image2BitMap;
    CloudinaryClient cloudinaryClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post);
        setViewControls();
        setOnClickListeners();
        cloudinaryClient = new CloudinaryClient();
    }

    private void setOnClickListeners() {
        buttonAddPost.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonNotMyPost.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        editTextDescription1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextDescription1.setText("");
                }
            }
        });

        editTextDescription2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextDescription2.setText("");
                }
            }
        });

        editTextTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    editTextTitle.setText("");
                }
            }
        });
    }

    private void setViewControls() {
        tokens = (TextView) findViewById(R.id.tokens);
        image1 = (ImageView) findViewById(R.id.addPostImageView1);
        image2 = (ImageView) findViewById(R.id.addPostImageView2);
        editTextTitle = (EditText) findViewById(R.id.postTitleEditText);
        editTextDescription1 = (EditText) findViewById(R.id.description1EditText);
        editTextDescription2 = (EditText) findViewById(R.id.description2EditText);
        buttonAddPost = (Button) findViewById(R.id.postButton);
        buttonCancel = (Button) findViewById(R.id.cancelButton);
        buttonNotMyPost = (Button) findViewById(R.id.notMyPostButton);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void uploadPost() {
        String title, description1, description2;
        title = editTextTitle.getText().toString();
        description1 = editTextDescription1.getText().toString();
        description2 = editTextDescription2.getText().toString();

        PostsUploadController postsUploadController = new PostsUploadController();
        postsUploadController.uploadPost(this, title, image1BitMap, image2BitMap, description1, description2);
    }

    private void uploadNotMyPost(){
            String title, description1, description2;
            title = editTextTitle.getText().toString();
            description1 = editTextDescription1.getText().toString();
            description2 = editTextDescription2.getText().toString();

            PostsUploadController postsUploadController = new PostsUploadController();
            postsUploadController.uploadPost(this, title, image1BitMap, image2BitMap, description1, description2, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode != RESULT_CANCELED) {
            Bitmap bitmap = ImagePicker.getImageFromResult(this, resultCode, data);
            switch (selectedImage) {
                case 1:
                    image1.setImageBitmap(bitmap);
                    image1BitMap = bitmap;
                    break;
                case 2:
                    image2.setImageBitmap(bitmap);
                    image2BitMap = bitmap;
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent chooseImageIntent;
        switch (v.getId()) {
            case R.id.postButton:
                uploadPost();
                finish();
                break;

            case R.id.cancelButton:
                finish();
                break;

            case R.id.notMyPostButton:
                uploadNotMyPost();
                finish();
                break;

            case R.id.addPostImageView1:
                selectedImage = 1;
                chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, SELECT_PHOTO);
                break;

            case R.id.addPostImageView2:
                selectedImage = 2;
                chooseImageIntent = ImagePicker.getPickImageIntent(this);
                startActivityForResult(chooseImageIntent, SELECT_PHOTO);
                break;
        }
    }
}